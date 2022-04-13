package com.huqz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.exception.FileTypeException;
import com.huqz.model.*;
import com.huqz.pojo.imgDTO.FileDTO;
import com.huqz.pojo.imgDTO.PageDTO;
import com.huqz.pojo.imgDTO.UpdateDTO;
import com.huqz.pojo.imgDTO.UploadDTO;
import com.huqz.service.*;
import com.huqz.utils.FilesUtils;
import com.huqz.utils.UrnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imgs")
public class ImageController {

    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageTagsService imageTagsService;

    @Autowired
    private ShareImageService shareImageService;

    @Autowired
    private UserService userService;

    @Value("${imgs.upload.tags.maxNum}")
    private Integer tagMaxNum;

    @Value("${imgs.view.notFound}")
    private String notFoundImage;

    @PostMapping
    public Result upload(UploadDTO uploadDTO, @RequestParam("file") MultipartFile file) throws FileTypeException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResultGenerator.fail(ResultCode.UNAUTHORIZED, "请先登录");
        }
        User principal = (User) authentication.getPrincipal();
        FileDTO fileDTO = filesUtils.save(file, "image");
        if (uploadDTO.getCategoryId() == null) {
            uploadDTO.setCategoryId(1);
        } else {
            // 查询是否有此分类
            Category c = categoryService.getById(uploadDTO.getCategoryId());
            if (c == null) {
                return ResultGenerator.fail(ResultCode.UNKNOWN_SORT_ID, "不存在的分类");
            }
        }


        // 设置缩略图
        String thumbUrn = filesUtils.createThumb(fileDTO.getPath());

        Image image = new Image();
        image.setUserId(principal.getId());
        image.setUrl(fileDTO.getPath());
        image.setUrn(UrnUtils.genUrn());
        image.setThumbUrn(thumbUrn);
        image.setCategoryId(uploadDTO.getCategoryId());

        imageService.save(image);                    // 上传图片

        // 处理标签 (根据maxNum来限制传递个数)
        List<String> tags = uploadDTO.getTags();
        if (tags != null)
            for (int i = 0; i < min(tags.size(), tagMaxNum); i++) {
                // 查询是否已经有该标签
                Tag tag = tagService.getByTagName(tags.get(i));
                // 没有标签则先存入
                if (tag == null) {
                    tag = new Tag().setTagName(tags.get(i));
                    tagService.save(tag);// 上传标签
                }
                // 关联图片和标签
                imageTagsService.save(
                        new ImageTags().setImgId(image.getId())
                                .setTagId(tag.getId())
                );
            }

        return ResultGenerator.ok(fileDTO);
    }

    @GetMapping
    public Result list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                       @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId,
                       @RequestParam(value = "tag", required = false) String tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(pageSize);
        pageDTO.setPageNumber(pageNumber);
        pageDTO.setCategoryId(categoryId);
        pageDTO.setTag(tag);

        IPage<Image> page = imageService.getPageByAnyCondition(pageDTO, userId);
        return ResultGenerator.ok(page);
    }

    @DeleteMapping
    public Result del(@RequestParam("id") Integer imgId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        if (imgId == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        Image image = imageService.getByImgIdAndUserId(imgId, userId);
        // 存在该图片，则删除
        if (image != null) imageService.removeById(imgId);
        return ResultGenerator.ok();
    }

    @PutMapping
    public Result update(@RequestBody UpdateDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        Integer categoryId = updateDTO.getCategoryId();
        Integer imgId = updateDTO.getImgId();
        List<String> tags = updateDTO.getTags();

        // 该用户是否存在该图片
        Image image = imageService.getByImgIdAndUserId(imgId, userId);
        if (image == null) return ResultGenerator.fail(ResultCode.UNKNOWN_IMG_ID, "无效的图片ID");

        // 该用户是否存在该分类 (排除 默认分类)
        if (categoryId != 1) {
            Category c = categoryService.getByCategoryIdAndUserId(categoryId, userId);
            if (c == null) return ResultGenerator.fail(ResultCode.UNKNOWN_SORT_ID, "不存在的分类");
        }
        // 不管分类是否发生改变，都执行更新
        imageService.updateCategoryByImgIdAndUserId(imgId, userId, categoryId);

        if (tags == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        // 解绑imgId的所有标签
        imageTagsService.removeAllTagsByImgId(imgId);

        // 处理标签
        for (int i = 0; i < min(tags.size(), tagMaxNum); i++) {

            Tag tag = tagService.getByTagName(tags.get(i));
            // 新标签则添加
            if (tag == null) {
                tag = new Tag().setTagName(tags.get(i));
                tagService.save(tag);
            }
            // 关联图片和标签
            imageTagsService.save(
                    new ImageTags().setImgId(imgId).setTagId(tag.getId())
            );
        }

        return ResultGenerator.ok();

    }

    @GetMapping(value = "/view/{urn}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] view(@PathVariable String urn) throws IOException {

        String path = notFoundImage;
        String url = imageService.getUrlByUrn(urn);
        if (url != null) path = filesUtils.getAbsolutePath(url);

        FileInputStream fis = new FileInputStream(new File(path));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }

    @GetMapping(value = "/view/share/{urn}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] viewShare(@PathVariable String urn) throws IOException {

        String path = notFoundImage;
        if (urn.length() == 32) {
            ShareImage shareImage = shareImageService.getImageByUrn(urn);
            if (shareImage != null && shareImage.getStatus()) {
                String url = imageService.getUrlByImgId(shareImage.getImgId());
                path = filesUtils.getAbsolutePath(url);
            }
        }

        FileInputStream fis = new FileInputStream(new File(path));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }

    @GetMapping(value = "/thumb/{urn}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] viewThumb(@PathVariable String urn) throws IOException {
        String path = notFoundImage;
        if (urn != null) {
            String url = imageService.getUrlByThumbUrn(urn);
            if (url != null) {
                String filename = url.substring(url.lastIndexOf(File.separator) + 1);
                String filepath = url.replace(filename, "thumb-" + filename);
                path = filesUtils.getAbsolutePath(filepath);
            }
        }

        FileInputStream fis = new FileInputStream(new File(path));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }

    @GetMapping(value = "/avatar/{urn}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] viewAvatar(@PathVariable String urn) throws IOException {
        String path = notFoundImage;
        if (urn != null) {
            User user = userService.getByHead(urn);
            if (user != null) {
                path = filesUtils.getHeadAbsolutePath(urn);
            }
        }
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }

    @PostMapping("/confirm_to_visit")
    public void recover() {

    }

    @GetMapping("/refresh_uri")
    public void refreshUrl() {

    }

    @GetMapping("/download/{urn}")
    public void download() {

    }

    public Integer min(Integer a, Integer b) {
        if (a > b) return b;
        return a;
    }
}

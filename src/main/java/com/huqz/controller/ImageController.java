package com.huqz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.exception.FileTypeException;
import com.huqz.model.*;
import com.huqz.pojo.imgDTO.FileDTO;
import com.huqz.pojo.imgDTO.PageDTO;
import com.huqz.pojo.imgDTO.UploadDTO;
import com.huqz.service.CategoryService;
import com.huqz.service.ImageService;
import com.huqz.service.ImageTagsService;
import com.huqz.service.TagService;
import com.huqz.utils.FileUtils;
import com.huqz.utils.ImageUtils;
import com.huqz.utils.UrnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imgs")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageTagsService imageTagsService;

    @Value("${imgs.upload.tags.maxNum}")
    private Integer tagMaxNum;

    @PostMapping("/upload")
    public Result upload(UploadDTO uploadDTO, @RequestParam("file") MultipartFile file) throws FileTypeException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ResultGenerator.fail(ResultCode.UNAUTHORIZED, "请先登录");
        }
        User principal = (User) authentication.getPrincipal();
        FileDTO fileDTO = FileUtils.upload(file);
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
        String thumbUrn = ImageUtils.createThumb(fileDTO.getPath());

        Image image = new Image();
        image.setUserId(principal.getId());
        image.setUrl(fileDTO.getPath());
        image.setUrn(UrnUtils.genUrn());
        image.setThumbUrn(thumbUrn);
        image.setCategoryId(uploadDTO.getCategoryId());

        imageService.save(image);                    // 上传图片

        // 处理标签 (根据maxNum来限制传递个数)
        List<String> tags = uploadDTO.getTags();
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

        return ResultGenerator.ok();
    }

    @GetMapping("/list")
    public Result list(@RequestBody PageDTO pageDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();

        IPage<Image> page = imageService.getPageByAnyCondition(pageDTO, userId);
        return ResultGenerator.ok(page);
    }

    @PostMapping("/del")
    public void del() {

    }

    @PostMapping("/update")
    public void update() {

    }

    @GetMapping("/refresh_uri")
    public void refreshUrl() {

    }

    @GetMapping("/download/{urn}")
    public void download() {

    }

    @GetMapping("/view/{urn}")
    public void view() {

    }

    @PostMapping("/confirm_to_visit")
    public void recover() {

    }


    public Integer min(Integer a, Integer b) {
        if (a > b) return b;
        return a;
    }
}

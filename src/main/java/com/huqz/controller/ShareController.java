package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.model.*;
import com.huqz.service.*;
import com.huqz.utils.DesensitizedUtils;
import com.huqz.utils.UrnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ShareListService shareListService;

    @Autowired
    private ShareImageService shareImageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    private final String SHARE_PREFIX = "?sharedId=";

    @PostMapping
    public Result createSort(@RequestBody Map<String, Integer> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        Integer categoryId = map.get("categoryId");
        if (categoryId == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        Category category = categoryService.getByCategoryIdAndUserId(categoryId, userId);
        if (category == null) return ResultGenerator.fail(ResultCode.UNKNOWN_SORT_ID, "无效的sortId");

        if (category.getShared()) return ResultGenerator.fail(ResultCode.SHARED_SORT_ID, "该分类已经分享");

        // 查询在该分类下是否有图片
        List<Image> images = imageService.getImageByUserIdAndCategoryId(userId, categoryId);
        if (images.size() < 1) return ResultGenerator.fail(ResultCode.NOT_EXISTED_ANY_IMG, "该分类下无任何图片");

        // 新建分享分类
        ShareList shareList = new ShareList();
        shareList.setCategoryId(categoryId);
        shareList.setUserId(userId);
        shareList.setType("category");
        // 保存分享
        shareListService.save(shareList);
        // 给当前类设置分享属性
        category.setShared(true);
        category.setShareId(shareList.getId());
        categoryService.updateById(category);

        // todo 感觉要是某个分类图片很多的话，对数据库打击就很大了
        for (Image image : images) {
            ShareImage shareImage = new ShareImage();
            shareImage.setImgId(image.getId());
            shareImage.setShareId(shareList.getId());
            shareImage.setStatus(true);
            // 图片地址加密，防止拿到原图地址
            String urn = DigestUtils.md5DigestAsHex((image.getUrn() + new Date().getTime()).getBytes());
            shareImage.setUrn(urn);
            shareImageService.save(shareImage);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("shareId", shareList.getId());
        res.put("url", SHARE_PREFIX + shareList.getId());

        return ResultGenerator.ok(res);

    }

    @GetMapping("/query")
    public Result query(@RequestParam("shareId") Integer shareId) {

        ShareList shareList = shareListService.getById(shareId);
        if (shareList == null) return ResultGenerator.fail(ResultCode.INVALID_SHARE_ID, "无效的shareId");

        if (!shareList.getStatus()) return ResultGenerator.fail(ResultCode.EXPIRED_SHARE, "该分享已失效");

        User shareUser = userService.getById(shareList.getUserId());
        String nickname = shareUser.getNickname();
        if (nickname == null || "".equals(nickname)) {
            // 对用户名脱敏处理
            nickname = DesensitizedUtils.deUsername(shareUser.getUsername());
        }

        List<ShareImage> shareImages = shareImageService.getImageByShareIdAndStatus(shareList.getId(), true);

        List<Map<String, Object>> arr = new ArrayList<>();

        for (ShareImage shareImage : shareImages) {
            Map<String, Object> image = new HashMap<>();
            image.put("uri", "imgs/view/share/" + shareImage.getUrn());
            image.put("tags", tagService.getTagsByImgId(shareImage.getImgId()));
            arr.add(image);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sharedImages", arr);
        map.put("shareUser", nickname);

        return ResultGenerator.ok(map);


    }

    @DeleteMapping
    public Result delete(@RequestBody Map<String, Integer> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        Integer shareId = map.get("shareId");

        if (shareId == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "不合法的参数");

        ShareList shareList = shareListService.getByShareIdAndUserId(shareId, userId);
        // 该用户不存在该分享ID
        if (shareList == null) return ResultGenerator.ok();

        if (!shareList.getStatus()) return ResultGenerator.fail(ResultCode.EXPIRED_SHARE, "该分享已失效");

        // 设置已分享的图片 分享状态为 false
        shareImageService.updateStatusManyByShareId(shareId, false);
        // 移除 category 库该分类的 shareId
        if ("category".equals(shareList.getType())) {
            categoryService.cancelShareByCategoryId(shareList.getCategoryId());
        }
        // 设置该shareId的状态为false
        shareListService.cancelShareById(shareList.getId());

        return ResultGenerator.ok();
    }
}

package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.model.*;
import com.huqz.service.*;
import com.huqz.utils.CodeUtils;
import com.huqz.utils.DesensitizedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
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

    @Autowired
    private CodeUtils codeUtils;

    @Autowired
    private AuthTokenService authTokenService;

    private final String SHARE_PREFIX = "?shareId=";

    @GetMapping
    public Result getAllShare() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        List<ShareList> res = shareListService.getByUserId(userId);

        return ResultGenerator.ok(res);
    }

    @PostMapping("{categoryId}")
    public Result createShare(@PathVariable Integer categoryId, @RequestBody Map<String, Object> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();

        String time = (String) map.get("time");
        Timestamp shareTime = getExpireDay(time);
        Boolean code = (Boolean) map.get("code");  // todo ??????
        String description = (String) map.get("description");

        if (categoryId == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "??????????????????");

        if (categoryId != 1) {
            Category category = categoryService.getByCategoryIdAndUserId(categoryId, userId);
            if (category == null) return ResultGenerator.fail(ResultCode.UNKNOWN_CATEGORY_ID, "?????????categoryId");
        }

//        if (category.getShared()) return ResultGenerator.fail(ResultCode.SHARED_CATEGORY_ID, "?????????????????????");

        // ????????????????????????????????????
        List<Image> images = imageService.getImageByUserIdAndCategoryId(userId, categoryId);
        if (images.size() < 1) return ResultGenerator.fail(ResultCode.NOT_EXISTED_ANY_IMG, "???????????????????????????");

        // ??????????????????
        ShareList shareList = new ShareList();
        shareList.setId(authTokenService.genToken());
        shareList.setUserId(userId);
        shareList.setCategoryId(categoryId);
        shareList.setType("category");
        if (code) shareList.setCode(codeUtils.genShareCode());
        if (shareTime != null) {
            shareList.setExpireTime(shareTime);
        }
        if (description != null && !"".equals(description)) {
            shareList.setDescription(description);
        }

        // ????????????
        shareListService.save(shareList);
        // ??????????????????????????????
//        category.setShared(true);
//        category.setShareId(shareList.getId());
//        categoryService.updateById(category);

        // todo ???????????????????????????????????????????????????????????????????????????
        for (Image image : images) {
            ShareImage shareImage = new ShareImage();
            shareImage.setImgId(image.getId());
            shareImage.setShareId(shareList.getId());
            shareImage.setStatus(true);
            // ?????????????????????????????????????????????
            String urn = DigestUtils.md5DigestAsHex((image.getUrn() + new Date().getTime()).getBytes());
            shareImage.setUrn(urn);
            shareImageService.save(shareImage);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("shareId", shareList.getId());
        res.put("url", SHARE_PREFIX + shareList.getId());
        res.put("code", shareList.getCode());

        return ResultGenerator.ok(res);

    }

    @DeleteMapping("{shareId}")
    public Result deleteShare(@PathVariable("shareId") String shareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();

        if (shareId == null) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "??????????????????");

        ShareList shareList = shareListService.getByShareIdAndUserId(shareId, userId);
        // ???????????????????????????ID
        if (shareList == null) return ResultGenerator.ok();

//        if (!shareList.getStatus()) return ResultGenerator.fail(ResultCode.EXPIRED_SHARE, "??????????????????");


        // ???????????????????????? ??????????????? false
        shareImageService.updateStatusManyByShareId(shareId, false);
        // ?????? category ??????????????? shareId
//        if ("category".equals(shareList.getType())) {
//            categoryService.cancelShareByCategoryId(shareList.getCategoryId());
//        }
        // ?????????shareId????????????false
//        shareListService.cancelShareById(shareList.getId());
        shareListService.removeById(shareId);

        return ResultGenerator.ok();
    }

    @GetMapping("/s/{shareId}")
    public Result query(@PathVariable String shareId) {

        ShareList shareList = shareListService.getById(shareId);
        if (shareList == null) return ResultGenerator.fail(ResultCode.INVALID_SHARE_ID, "?????????shareId");

        if (!shareList.getStatus()) return ResultGenerator.fail(ResultCode.EXPIRED_SHARE, "??????????????????");

        Timestamp expireTime = shareList.getExpireTime();
        if (expireTime != null) {
            if (new Date(expireTime.getTime()).before(new Date())){
                shareListService.cancelShareById(shareId);
                // ???????????????????????? ??????????????? false
                shareImageService.updateStatusManyByShareId(shareId, false);
                return ResultGenerator.fail(ResultCode.EXPIRED_SHARE, "??????????????????");
            }
        }

        User shareUser = userService.getById(shareList.getUserId());
        String nickname = shareUser.getNickname();
        if (nickname == null || "".equals(nickname)) {
            // ????????????????????????
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

    public Timestamp getExpireDay(String s) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int day = 30;
        if ("day".equals(s)) day = 1;
        else if ("week".equals(s)) day = 7;
        else if ("permanent".equals(s)) return null;

        calendar.add(Calendar.DATE, day);
        return new Timestamp(calendar.getTime().getTime());

    }

}

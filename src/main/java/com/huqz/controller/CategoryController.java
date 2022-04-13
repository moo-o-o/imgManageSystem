package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.model.Category;
import com.huqz.model.DefaultLoadCategory;
import com.huqz.model.Image;
import com.huqz.model.User;
import com.huqz.pojo.CategoryDTO;
import com.huqz.service.CategoryService;
import com.huqz.service.DefaultLoadCategoryService;
import com.huqz.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private DefaultLoadCategoryService defaultLoadCategoryService;

    @GetMapping
    public Result list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer userId = principal.getId();
        List<Category> list = categoryService.getByUserId(userId);
        list.add(0, categoryService.getDefault());

        Integer defaultId = 1;
        DefaultLoadCategory defaultLoadCategory = defaultLoadCategoryService.getByUserId(userId);
        if (defaultLoadCategory != null) defaultId = defaultLoadCategory.getCategoryId();

        Map<String, Object> map = new HashMap<>();
        map.put("records", list);
        map.put("defaultLoadCategoryId", defaultId);

        return ResultGenerator.ok(map);
    }

    @PostMapping
    public Result add(@RequestBody Map<String, String> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        String categoryName = map.get("categoryName");
        if (categoryName == null || "".equals(categoryName))
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
        Integer userId = principal.getId();

        Category c = categoryService.getByCategoryNameAndUserId(categoryName, userId);
        if (c != null) return ResultGenerator.fail(ResultCode.EXISTING_SORT_NAME, "此分类已存在");

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setUserId(userId);
        categoryService.save(category);

        return ResultGenerator.ok();

    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer categoryId = categoryDTO.getCategoryId();
        String categoryName = categoryDTO.getCategoryName();
        Integer userId = principal.getId();

        Category c = categoryService.getByCategoryIdAndUserId(categoryId, userId);
        if (c == null) return ResultGenerator.ok();

        c.setCategoryName(categoryName);
        categoryService.updateById(c);
        return ResultGenerator.ok();
    }

    @DeleteMapping
    public Result delete(@RequestParam("categoryId")Integer categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        if (categoryId == null)
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");

        // 通过分类ID 查询该用户是否存在该分类
        Integer userId = principal.getId();
        Category c = categoryService.getByCategoryIdAndUserId(categoryId, userId);
        if (c == null) return ResultGenerator.ok();

        // 处理该分类下的图片 保存到 -617 的分类下
        List<Image> imageList = imageService.getImageByUserIdAndCategoryId(userId, categoryId);
        for (Image image : imageList) {
            image.setCategoryId(-617);
            imageService.updateById(image);
        }

        categoryService.removeById(categoryId);
        return ResultGenerator.ok();
    }

    @PostMapping("/set")
    public Result setDefault(@RequestBody Map<String, Integer> map ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Integer categoryId = map.get("categoryId");
        Integer userId = principal.getId();

        // categoryId 为空 表示不设置默认加载分类
        if (categoryId == null) {
            defaultLoadCategoryService.removeByUserId(userId);
            return ResultGenerator.ok();
        }

        // 查询用户是否有 该分类
        Category c = categoryService.getByCategoryIdAndUserId(categoryId, userId);
        // categoryId 保证可以设置默认为0
        if (c == null && categoryId != 0) return ResultGenerator.ok();

        DefaultLoadCategory defaultLoad = defaultLoadCategoryService.getByUserId(userId);
        // 第一次创建默认加载
        if (defaultLoad == null) {
            defaultLoad = new DefaultLoadCategory();
            defaultLoad.setUserId(userId);
            defaultLoad.setCategoryId(categoryId);
            defaultLoadCategoryService.save(defaultLoad);
            return ResultGenerator.ok();
        }
        defaultLoad.setCategoryId(categoryId);

        // 更新默认加载分类
        defaultLoadCategoryService.updateById(defaultLoad);

        return ResultGenerator.ok();
    }


}

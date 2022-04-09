package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    Category getDefault();

    Category getByCategoryNameAndUserId(String categoryName, Integer userId);

    Category getByCategoryIdAndUserId(Integer categoryId, Integer userId);

    List<Category> getByUserId(Integer userId);
}

package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.CategoryMapper;
import com.huqz.model.Category;
import com.huqz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category getDefault() {
        return categoryMapper.selectDefault();
    }

    @Override
    public Category getByCategoryNameAndUserId(String categoryName, Integer userId) {
        if (userId == 0) return null;
        if ("默认分类".equals(categoryName)) return getDefault();
        return categoryMapper.selectByCategoryNameAndUserId(categoryName, userId);
    }

    @Override
    public List<Category> getByUserId(Integer userId) {
        return categoryMapper.selectByUserId(userId);
    }

    @Override
    public Boolean cancelShareByCategoryId(Integer categoryId) {
        return categoryMapper.updateShareByCategoryId(categoryId) > 0;
    }

    @Override
    public Category getByCategoryIdAndUserId(Integer categoryId, Integer userId) {
        if (userId == 0) return null;
        return categoryMapper.selectByCategoryIdAndUserId(categoryId, userId);
    }
}

package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.CategoryMapper;
import com.huqz.model.Category;
import com.huqz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "categoryCache")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
//    @Cacheable(key = "methodName")
    public Category getDefault() {
        return categoryMapper.selectDefault();
    }

    @Override
//    @Cacheable(key = "#userId")
    public Category getByCategoryNameAndUserId(String categoryName, Integer userId) {
        if (userId == 0) return null;
        if ("默认分类".equals(categoryName)) return getDefault();
        return categoryMapper.selectByCategoryNameAndUserId(categoryName, userId);
    }

    @Override
//    @Cacheable(key = "#userId")
    public List<Category> getByUserId(Integer userId) {
        return categoryMapper.selectByUserId(userId);
    }

    @Override
//    @Cacheable(key = "#categoryId")
    public Boolean cancelShareByCategoryId(Integer categoryId) {
        return categoryMapper.updateShareByCategoryId(categoryId) > 0;
    }

    @Override
//    @Cacheable(key = "#categoryId")
    public Category getByCategoryIdAndUserId(Integer categoryId, Integer userId) {
        if (userId == 0) return null;
        return categoryMapper.selectByCategoryIdAndUserId(categoryId, userId);
    }
}

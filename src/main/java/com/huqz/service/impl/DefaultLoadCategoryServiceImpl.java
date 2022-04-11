package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.DefaultLoadCategoryMapper;
import com.huqz.model.DefaultLoadCategory;
import com.huqz.service.DefaultLoadCategoryService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultLoadCategoryServiceImpl extends ServiceImpl<DefaultLoadCategoryMapper, DefaultLoadCategory> implements DefaultLoadCategoryService {

    @Autowired
    private DefaultLoadCategoryMapper defaultLoadCategoryMapper;

    public boolean removeByUserId(Integer userId) {
        return defaultLoadCategoryMapper.deleteByUserId(userId) > 0;
    }

    @Override
    public DefaultLoadCategory getByUserId(Integer userId) {
        return defaultLoadCategoryMapper.selectByUserId(userId);
    }

}

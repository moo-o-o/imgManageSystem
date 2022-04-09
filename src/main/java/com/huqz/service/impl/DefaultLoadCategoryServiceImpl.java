package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.DefaultLoadCategoryMapper;
import com.huqz.model.DefaultLoadCategory;
import com.huqz.service.DefaultLoadCategoryService;
import org.springframework.stereotype.Service;

@Service
public class DefaultLoadCategoryServiceImpl extends ServiceImpl<DefaultLoadCategoryMapper, DefaultLoadCategory> implements DefaultLoadCategoryService {
}

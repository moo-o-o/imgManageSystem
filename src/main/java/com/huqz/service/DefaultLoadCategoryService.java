package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.DefaultLoadCategory;

public interface DefaultLoadCategoryService extends IService<DefaultLoadCategory> {

    boolean removeByUserId(Integer userId);

    DefaultLoadCategory getByUserId(Integer userId);
}

package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.ApiKey;

import java.util.List;

public interface ApiKeyService extends IService<ApiKey>{

    List<ApiKey> getByUserId(Integer userId);

    ApiKey getByApiKey(String key);

    ApiKey getByUserIdAndApiKey(Integer userId, String key);
}

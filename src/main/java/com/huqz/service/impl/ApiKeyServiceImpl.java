package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ApiKeyMapper;
import com.huqz.model.ApiKey;
import com.huqz.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiKeyServiceImpl extends ServiceImpl<ApiKeyMapper, ApiKey> implements ApiKeyService {

    @Autowired
    private ApiKeyMapper apiKeyMapper;

    @Override
    public List<ApiKey> getByUserId(Integer userId) {
        return apiKeyMapper.selectByUserId(userId);
    }

    @Override
    public ApiKey getByApiKey(String key) {
        return apiKeyMapper.selectByApiKey(key);
    }

    @Override
    public ApiKey getByUserIdAndApiKey(Integer userId, String key) {
        return apiKeyMapper.selectByUserIdAndApiKey(userId, key);
    }
}

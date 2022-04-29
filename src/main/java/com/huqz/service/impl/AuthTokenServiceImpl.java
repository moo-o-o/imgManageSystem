package com.huqz.service.impl;

import com.huqz.service.AuthTokenService;
import com.huqz.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Autowired
    private CacheService cacheService;

    public String genToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

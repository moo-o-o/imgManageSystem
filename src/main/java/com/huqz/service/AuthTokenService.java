package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.AuthToken;

public interface AuthTokenService {

    String genToken();
}

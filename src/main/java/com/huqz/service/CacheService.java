package com.huqz.service;

import com.huqz.exception.MailCodeException;
import com.huqz.pojo.userDTO.MailDTO;

public interface CacheService {

    MailDTO storeMailCode(MailDTO mailDTO, String token);

    MailDTO getByToken(String token) throws MailCodeException;

    void delMailCode(String token);
}

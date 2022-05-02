package com.huqz.service.impl;

import com.huqz.pojo.userDTO.MailDTO;
import com.huqz.exception.MailCodeException;
import com.huqz.service.CacheService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    // 注意：返回值要一致

    @Override
    @CachePut(value = "mailCode", key = "#token")
    public MailDTO storeMailCode(MailDTO mailDTO, String token) {
        System.out.println(token);
        return mailDTO;
    }

    // 只取cache中的token对应的用户信息。取不到则表示验证码过期或未获取验证码
    @Override
    @Cacheable(value = "mailCode", key = "#token")
    public MailDTO getByToken(String token) throws MailCodeException {
        System.out.println(token);
        throw new MailCodeException();

    }

    @Override
    @CacheEvict(value = "mailCode", key = "#token")
    public void delMailCode(String token) {}

    @CachePut(value = "visit#86400", key = "#ip + '::' + #imgId")
    public Boolean storeVisit(String ip, Integer imgId) {
        System.out.println(ip + "@@@" + imgId);
        return true;
    }

    @Cacheable(value = "visit", key = "#ip + '::' + #imgId")
    public Boolean getVisit(String ip, Integer imgId) throws Exception {
        System.out.println(ip + "$$$" + imgId);
        throw new Exception();
    }
}

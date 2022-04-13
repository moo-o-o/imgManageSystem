package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends IService<User>, UserDetailsService {

    User getByUsername(String username);

    User getByMail(String mail);

    boolean updatePasswordById(String password, Integer id);

    User getByHead(String urn);
}

package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.UserMapper;
import com.huqz.model.User;
import com.huqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
//    @Cacheable(value = "user", key = "#mail")
    public User getByMail(String mail) {
        return userMapper.selectUserByMail(mail);
    }

    @Override
//    @Cacheable(value = "user", key = "#username")
    public User getByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
//    @Cacheable(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }
        user.setRoles(userMapper.getUserRolesByUid(user.getId()));
        return user;

        // todo 通过邮箱查找
    }

    @Override
    public boolean save(User entity) {
        String password = entity.getPassword();
        String encodePassword = new BCryptPasswordEncoder(10).encode(password);
        entity.setPassword(encodePassword);
        System.out.println(entity.toString());
        return userMapper.insert(entity) > 0;
//        return false;
    }

    @Override
    public boolean updatePasswordById(String password, Integer id) {
        String encodePassword = new BCryptPasswordEncoder(10).encode(password);
        return userMapper.updatePasswordById(encodePassword, id) > 0;
    }
}

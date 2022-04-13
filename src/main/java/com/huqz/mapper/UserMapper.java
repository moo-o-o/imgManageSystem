package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Role;
import com.huqz.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username} limit 1")
    User selectUserByUsername(String username);

    @Select("select * from user where mail = #{mail} limit 1")
    User selectUserByMail(String mail);

    @Select("select * from role r, user_role ur where r.id = ur.rid and ur.uid = #{id}")
    List<Role> getUserRolesByUid(Integer id);

    @Update("update user set password = #{password} where id = ${id}")
    int updatePasswordById(String password, Integer id);

    @Select("select * from user where head_img = #{urn}")
    User selectByHead(String urn);
}

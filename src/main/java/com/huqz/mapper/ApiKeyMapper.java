package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ApiKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ApiKeyMapper extends BaseMapper<ApiKey> {

    @Select("select id, user_id, category_id, token, create_time from api_key where user_id = #{userId}")
    List<ApiKey> selectByUserId(Integer userId);

    @Select("select id, user_id, category_id, token, create_time from api_key where token = #{key}")
    ApiKey selectByApiKey(String key);
}

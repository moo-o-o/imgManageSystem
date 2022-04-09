package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.DefaultLoadCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DefaultLoadCategoryMapper extends BaseMapper<DefaultLoadCategory> {

    @Delete("delete from default_load_category where user_id = #{userId}")
    int deleteByUserId(Integer userId);

}

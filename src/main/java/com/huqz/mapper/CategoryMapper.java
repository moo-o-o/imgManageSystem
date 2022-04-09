package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select * from category where id")
    Integer selectDefaultId();
}

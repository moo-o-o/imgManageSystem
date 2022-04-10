package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select * from category where user_id = 0")
    Category selectDefault();

    @Select("select * from category where category_name = #{categoryName} and user_id = #{userId}")
    Category selectByCategoryNameAndUserId(String categoryName, Integer userId);

    @Select("select * from category where id = #{categoryId} and user_id = #{userId}")
    Category selectByCategoryIdAndUserId(Integer categoryId, Integer userId);

    @Select("select * from category where user_id = #{userId}")
    List<Category> selectByUserId(Integer userId);

    @Update("update category set share_id = null, shared = 0 where id = #{categoryId}")
    Integer updateShareByCategoryId(Integer categoryId);
}

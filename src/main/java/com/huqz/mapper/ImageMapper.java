package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ImageMapper extends BaseMapper<Image> {

    // todo 更换*
    @Select("select * from image where id = #{imgId} and user_id = #{userId}")
    Image selectByImgIdAndUserId(Integer imgId, Integer userId);

    @Update("update image set category_id = #{categoryId} where id = #{imgId} and user_id = #{userId}")
    Integer updateCategoryByImgIdAndUserId(Integer imgId, Integer userId, Integer categoryId);

    @Select("select category_id from image where id = #{imgId} and user_id = #{userId}")
    Integer selectCategoryIdByImgIdAndUserId(Integer imgId, Integer userId);

    @Select("select url from image where urn = #{urn}")
    String selectUrlByUrn(String urn);

}

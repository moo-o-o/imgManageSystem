package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ImageTags;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImageTagsMapper extends BaseMapper<ImageTags> {

    @Select("select tag_id from image_tags where img_id = #{imgId}")
    List<Integer> selectTagIdByImgId(Integer imgId);

    @Select("select id from image_tags where tag_id = #{tagId} and img_id = #{imgId}")
    Integer selectByTagIdAndImgId(Integer tagId, Integer imgId);

    @Delete("delete from image_tags where img_id = #{imgId}")
    int deleteAllTagsByImgId(Integer imgId);

    List<Integer> selectImgIdsByTagIds(List<Integer> tagIds);

    @Select("select img_id from image_tags where tag_id = #{tagId}")
    List<Integer> selectImgIdsByTagId(Integer tagId);
}

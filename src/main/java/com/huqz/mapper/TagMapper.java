package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    @Select("select id, tag_name, create_time, update_time from tag where tag_name = #{tagName}")
    Tag selectByTagName(String tagName);

    @Select("select id from tag where tag_name = #{tagName}")
    Integer selectIdByTagName(String tagName);

    @Select("SELECT tag_name FROM tag WHERE tag.id IN (SELECT tag_id FROM image_tags WHERE img_id = #{imgId})")
    List<String> selectTagsByImgId(Integer imgId);

    @Select("select id from tag where tag_name like CONCAT(#{partTagName},'%')")
    List<Integer> selectIdsByPartTagName(String partTagName);
}

package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    @Select("select id, tag_name, create_time, update_time from tag where tag_name = #{tagName}")
    Tag selectByTagName(String tagName);

    @Select("select tag_name from tag where id = #{tagId}")
    String selectTagNameById(Integer tagId);
}

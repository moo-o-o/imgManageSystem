package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ImageTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImageTagsMapper extends BaseMapper<ImageTags> {

    @Select("select tag_id from image_tags where img_id = #{imgId}")
    List<Integer> selectTagIdByImgId(Integer imgId);
}

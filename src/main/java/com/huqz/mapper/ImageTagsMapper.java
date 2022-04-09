package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ImageTags;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ImageTagsMapper extends BaseMapper<ImageTags> {
}

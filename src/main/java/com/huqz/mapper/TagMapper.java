package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag> {
}

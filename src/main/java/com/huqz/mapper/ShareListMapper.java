package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ShareList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShareListMapper extends BaseMapper<ShareList> {

    @Select("select * from share_list where id = #{shareId} and user_id = #{userId}")
    ShareList selectByShareIdAndUserId(Integer shareId, Integer userId);

    @Update("update share_list set status = 0 where id = #{shareId}")
    int updateShareById(Integer shareId);
}

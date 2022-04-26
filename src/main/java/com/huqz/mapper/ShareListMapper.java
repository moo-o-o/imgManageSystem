package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.ShareList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShareListMapper extends BaseMapper<ShareList> {

    @Select("select * from share_list where id = #{shareId} and user_id = #{userId}")
    ShareList selectByShareIdAndUserId(String shareId, Integer userId);

    @Update("update share_list set status = 0 where id = #{shareId}")
    int updateShareById(String shareId);

    @Select("select * from share_list where user_id = #{userId}")
    List<ShareList> selectByUserId(Integer userId);
}

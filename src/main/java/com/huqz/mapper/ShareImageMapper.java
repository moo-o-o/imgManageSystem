package com.huqz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huqz.model.Image;
import com.huqz.model.ShareImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShareImageMapper extends BaseMapper<ShareImage> {

    @Select("select * from share_image where share_id = #{shareId} and status = #{status}")
    List<ShareImage> selectImageByShareIdAndStatus(Integer shareId, Boolean status);

    @Select("select img_id from share_image where urn = #{urn}")
    Integer selectImageIdByUrn(String urn);

    @Select("select * from share_image where urn = #{urn}")
    ShareImage selectImageByUrn(String urn);

    @Update("update share_image set status = #{status} where share_id = #{shareId}")
    Integer updateStatusManyByShareId(Integer shareId, Boolean status);
}

package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.ImageTags;

import java.util.List;

public interface ImageTagsService extends IService<ImageTags> {

    List<Integer> getTagIdByImgId(Integer imgId);

    Integer getByTagIdAndImgId(Integer tagId, Integer imgId);

    Boolean removeAllTagsByImgId(Integer imgId);

    Boolean saveManyTags(Integer imgId, List<Integer> tagIds);

    List<Integer> getImgIdsByTagIds(List<Integer> tagIds);

    List<Integer> getImgIdsByTagId(Integer tagId);
}

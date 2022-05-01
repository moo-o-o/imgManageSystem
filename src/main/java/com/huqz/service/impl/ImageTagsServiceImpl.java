package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageTagsMapper;
import com.huqz.model.ImageTags;
import com.huqz.service.ImageTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageTagsServiceImpl extends ServiceImpl<ImageTagsMapper, ImageTags> implements ImageTagsService {

    @Autowired
    private ImageTagsMapper imageTagsMapper;

    public List<Integer> getTagIdByImgId(Integer imgId) {
        return imageTagsMapper.selectTagIdByImgId(imgId);
    }

    @Override
    public Integer getByTagIdAndImgId(Integer tagId, Integer imgId) {
        return imageTagsMapper.selectByTagIdAndImgId(tagId, imgId);
    }

    @Override
    public Boolean removeAllTagsByImgId(Integer imgId) {
        return imageTagsMapper.deleteAllTagsByImgId(imgId) > 0;
    }

    @Override
    public Boolean saveManyTags(Integer imgId, List<Integer> tagIds) {
        for (Integer tagId : tagIds) {
            imageTagsMapper.insert(new ImageTags().setImgId(imgId).setTagId(tagId));
        }
        return true;
    }

    @Override
    public List<Integer> getImgIdsByTagIds(List<Integer> tagIds) {
        return imageTagsMapper.selectImgIdsByTagIds(tagIds);
    }

    @Override
    public List<Integer> getImgIdsByTagId(Integer tagId) {
        return imageTagsMapper.selectImgIdsByTagId(tagId);
    }
}

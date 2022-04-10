package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.TagMapper;
import com.huqz.model.Tag;
import com.huqz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<String> getTagsByImgId(Integer imgId) {
        return tagMapper.selectTagsByImgId(imgId);
    }

    @Override
    public Integer getIdByTagName(String tagName) {
        return tagMapper.selectIdByTagName(tagName);
    }

    @Override
    public Tag getByTagName(String tagName) {
        return tagMapper.selectByTagName(tagName);
    }
}

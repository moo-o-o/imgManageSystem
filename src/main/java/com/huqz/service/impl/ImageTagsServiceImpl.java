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
}

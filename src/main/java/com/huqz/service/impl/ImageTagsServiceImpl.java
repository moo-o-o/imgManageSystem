package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageTagsMapper;
import com.huqz.model.ImageTags;
import com.huqz.service.ImageTagsService;
import org.springframework.stereotype.Service;

@Service
public class ImageTagsServiceImpl extends ServiceImpl<ImageTagsMapper, ImageTags> implements ImageTagsService {
}

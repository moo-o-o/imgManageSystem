package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.TagMapper;
import com.huqz.model.Tag;
import com.huqz.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}

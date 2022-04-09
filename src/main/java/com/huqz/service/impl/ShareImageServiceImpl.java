package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ShareImageMapper;
import com.huqz.model.ShareImage;
import com.huqz.service.ShareImageService;
import org.springframework.stereotype.Service;

@Service
public class ShareImageServiceImpl extends ServiceImpl<ShareImageMapper, ShareImage> implements ShareImageService {
}

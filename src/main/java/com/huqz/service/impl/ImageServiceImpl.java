package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageMapper;
import com.huqz.model.Image;
import com.huqz.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

}

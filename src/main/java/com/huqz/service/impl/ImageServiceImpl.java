package com.huqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageMapper;
import com.huqz.model.Image;
import com.huqz.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    IPage<Image> getPageByCategoryId(Integer pageNumber, Integer pageSize, Integer categoryId) {

        LambdaQueryWrapper<Image> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Image::getCategoryId, categoryId);

        IPage<Image> page = new Page<>(pageNumber, pageSize);
        imageMapper.selectPage(page, lqw);

        return page;
    }

//    IPage<Image> getPageByCategoryIdAndTag(Integer pageNumber, Integer pageSize, Integer categoryId, String tag) {

//        imageMapper.
//    }
}

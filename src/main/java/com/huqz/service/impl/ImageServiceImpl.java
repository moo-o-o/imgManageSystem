package com.huqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageMapper;
import com.huqz.model.Image;
import com.huqz.model.ImageTags;
import com.huqz.model.Tag;
import com.huqz.pojo.ImageVO;
import com.huqz.pojo.imgDTO.PageDTO;
import com.huqz.service.ImageService;
import com.huqz.service.ImageTagsService;
import com.huqz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageTagsService imageTagsService;

    IPage<Image> getPageByCategoryId(Integer pageNumber, Integer pageSize, Integer categoryId) {

        LambdaQueryWrapper<Image> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Image::getCategoryId, categoryId);

        IPage<Image> page = new Page<>(pageNumber, pageSize);
        imageMapper.selectPage(page, lqw);

        return page;
    }

    IPage<Image> getPageByUserId(Integer pageNumber, Integer pageSize, Integer userId) {

        LambdaQueryWrapper<Image> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Image::getUserId, userId);

        IPage<Image> page = new Page<>(pageNumber, pageSize);
        return imageMapper.selectPage(page, lqw);
    }

    @Override
    public IPage<Image> getPageByAnyCondition(PageDTO pageDTO, Integer userId) {

        LambdaQueryWrapper<Image> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Image::getUserId, userId);
        lqw.eq(pageDTO.getCategoryId() != null, Image::getCategoryId, pageDTO.getCategoryId());

        // 查询tagName所对应的tagId是否与图片关联
        // 查询tagId
        Tag tag = tagService.getByTagName(pageDTO.getTag());

        // 查询图片与tagId 的关联情况
        // 查询 image_tags 中 对应 tagId 的图片id  --> 带有该标签的图片id
        // 比对 image 的 Id 是否在 该标签的图片id中
        Integer tagId = null;
        if (tag != null) {
            tagId = tag.getId();
        }
        lqw.inSql(tag != null, Image::getId, "select img_id from image_tags where tag_id = " + tagId);


        IPage<Image> page = new Page<>(pageDTO.getPageNumber(), pageDTO.getPageSize());
        imageMapper.selectPage(page, lqw);
        List<Image> records = page.getRecords();
        for (Image record : records) {
            ImageVO imageVO = new ImageVO(record);
            Integer imgId = record.getId();
            List<Integer> tagIds = imageTagsService.getTagIdByImgId(imgId);
            for (Integer id : tagIds) {
                String tagName = tagService.getTagNameByTagId(id);
                imageVO.getTags().add(tagName);
            }
        }

        System.out.println(page.getRecords());

        return page;
    }


}

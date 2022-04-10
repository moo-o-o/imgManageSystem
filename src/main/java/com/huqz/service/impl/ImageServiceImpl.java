package com.huqz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ImageMapper;
import com.huqz.model.Image;
import com.huqz.pojo.imgDTO.PageDTO;
import com.huqz.service.ImageService;
import com.huqz.service.TagService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private TagService tagService;

    @Override
    public IPage<Image> getPageByAnyCondition(PageDTO pageDTO, Integer userId) {

        LambdaQueryWrapper<Image> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Image::getUserId, userId);
        lqw.eq(pageDTO.getCategoryId() != null, Image::getCategoryId, pageDTO.getCategoryId());

        // 根据tagName查询tagId
        Integer tagId = tagService.getIdByTagName(pageDTO.getTag());

        // 剔除掉没有tagId的图片
        lqw.inSql(tagId != null, Image::getId, "select img_id from image_tags where tag_id = " + tagId);

        IPage<Image> page = new Page<>(pageDTO.getPageNumber(), pageDTO.getPageSize());
        imageMapper.selectPage(page, lqw);

        // 添加标签
        List<Image> records = page.getRecords();
        for (Image record : records) {
            List<String> tags = tagService.getTagsByImgId(record.getId());
            record.setTags(tags);
            // 不展示真实 url， 替换为urn访问
            String urn = record.getUrn();
            record.setUrl(urn);
        }
        return page;
    }

    @Override
    public Image getByImgIdAndUserId(Integer imgId, Integer userId) {
        return imageMapper.selectByImgIdAndUserId(imgId, userId);
    }

    @Override
    public boolean updateCategoryByImgIdAndUserId(Integer imgId, Integer userId, Integer categoryId) {
        return imageMapper.updateCategoryByImgIdAndUserId(imgId, userId, categoryId) > 0;
    }

    @Override
    public Integer getCategoryIdByImgIdAndUserId(Integer imgId, Integer userId) {
        return imageMapper.selectCategoryIdByImgIdAndUserId(imgId, userId);
    }

    @Override
    public String getUrlByUrn(String urn) {
        return imageMapper.selectUrlByUrn(urn);
    }

    @Override
    public List<Image> getImageByUserIdAndCategoryId(Integer userId, Integer categoryId) {
        return imageMapper.selectByUserIdAnCategoryId(userId, categoryId);
    }

    @Override
    public String getUrlByImgId(Integer imgId) {
        return imageMapper.selectUrlByImgId(imgId);
    }
}

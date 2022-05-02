package com.huqz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.Image;
import com.huqz.pojo.imgDTO.PageDTO;

import java.util.List;

public interface ImageService extends IService<Image> {

    IPage<Image> getPageByAnyCondition(PageDTO pageDTO, Integer userId);

    Image getByImgIdAndUserId(Integer imgId, Integer userId);

    boolean updateCategoryByImgIdAndUserId(Integer imgId, Integer userId, Integer categoryId);

    Integer getCategoryIdByImgIdAndUserId(Integer imgId, Integer userId);

    String getUrlByUrn(String urn);

    List<Image> getImageByUserIdAndCategoryId(Integer userId, Integer categoryId);

    String getUrlByImgId(Integer imgId);

    String getUrlByThumbUrn(String thumbUrn);

    Image getByUrn(String urn);
}

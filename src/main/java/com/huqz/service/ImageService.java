package com.huqz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.Image;
import com.huqz.pojo.imgDTO.PageDTO;

public interface ImageService extends IService<Image> {

    IPage<Image> getPageByAnyCondition(PageDTO pageDTO, Integer userId);

}

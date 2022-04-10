package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.ImageTags;

import java.util.List;

public interface ImageTagsService extends IService<ImageTags> {

    List<Integer> getTagIdByImgId(Integer imgId);
}

package com.huqz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService extends IService<Tag> {

    Tag getByTagName(String tagName);

    Integer getIdByTagName(String tagName);

    List<String> getTagsByImgId(Integer imgId);

    List<Integer> saveMany(List<String> tags);

    List<Integer> getIdsByPartTagName(String partTagName);
}

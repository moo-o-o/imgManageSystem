package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.TagMapper;
import com.huqz.model.Tag;
import com.huqz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Value("${imgs.upload.tags.maxNum}")
    private Integer tagMaxNum;

    @Override
    public List<String> getTagsByImgId(Integer imgId) {
        return tagMapper.selectTagsByImgId(imgId);
    }

    @Override
    public Integer getIdByTagName(String tagName) {
        return tagMapper.selectIdByTagName(tagName);
    }

    @Override
    public Tag getByTagName(String tagName) {
        return tagMapper.selectByTagName(tagName);
    }

    @Override
    public List<Integer> saveMany(List<String> tags) {
        List<Integer> arr = new ArrayList<>();
        for (String tag : tags) {
            Tag t = tagMapper.selectByTagName(tag);
            if (t == null) {
                t = new Tag().setTagName(tag);
                tagMapper.insert(t);
            }
            arr.add(t.getId());
            // 只保存前 3 个标签
            // 处理标签 (根据maxNum来限制传递个数)
            if (arr.size() == tagMaxNum) {
                break;
            }
        }
        return arr;
    }
}

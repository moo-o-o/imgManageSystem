package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ShareImageMapper;
import com.huqz.mapper.ShareListMapper;
import com.huqz.model.ShareImage;
import com.huqz.model.ShareList;
import com.huqz.service.ShareListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareListServiceImpl extends ServiceImpl<ShareListMapper, ShareList> implements ShareListService {

    @Autowired
    private ShareListMapper shareListMapper;

    @Override
    public ShareList getByShareIdAndUserId(Integer shareId, Integer userId) {
        return shareListMapper.selectByShareIdAndUserId(shareId, userId);
    }

    @Override
    public Boolean cancelShareById(Integer shareId) {
        return shareListMapper.updateShareById(shareId) > 0;
    }
}

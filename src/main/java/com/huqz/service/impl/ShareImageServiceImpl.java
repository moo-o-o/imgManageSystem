package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ShareImageMapper;
import com.huqz.model.Image;
import com.huqz.model.ShareImage;
import com.huqz.service.ShareImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareImageServiceImpl extends ServiceImpl<ShareImageMapper, ShareImage> implements ShareImageService {

    @Autowired
    private ShareImageMapper shareImageMapper;

    @Override
    public List<ShareImage> getImageByShareIdAndStatus(Integer shareId, Boolean status) {
        return shareImageMapper.selectImageByShareIdAndStatus(shareId, status);
    }

    @Override
    public Integer getImageIdByUrn(String urn) {
        return shareImageMapper.selectImageIdByUrn(urn);
    }

    @Override
    public ShareImage getImageByUrn(String urn) {
        return shareImageMapper.selectImageByUrn(urn);
    }

    @Override
    public boolean updateStatusManyByShareId(Integer shareId, Boolean status) {
        return shareImageMapper.updateStatusManyByShareId(shareId, status) > 0;
    }
}

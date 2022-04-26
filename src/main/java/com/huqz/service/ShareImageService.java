package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.Image;
import com.huqz.model.ShareImage;

import java.util.List;

public interface ShareImageService extends IService<ShareImage> {

    List<ShareImage> getImageByShareIdAndStatus(String shareId, Boolean status);

    Integer getImageIdByUrn(String urn);

    ShareImage getImageByUrn(String urn);

    boolean updateStatusManyByShareId(String shareId, Boolean status);
}

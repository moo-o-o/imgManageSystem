package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.ShareImage;
import com.huqz.model.ShareList;

public interface ShareListService extends IService<ShareList> {

    ShareList getByShareIdAndUserId(Integer shareId, Integer userId);

    Boolean cancelShareById(Integer shareId);
}

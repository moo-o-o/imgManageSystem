package com.huqz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huqz.model.ShareImage;
import com.huqz.model.ShareList;

import java.util.List;

public interface ShareListService extends IService<ShareList> {

    ShareList getByShareIdAndUserId(String shareId, Integer userId);

    Boolean cancelShareById(String shareId);

    List<ShareList> getByUserId(Integer userId);
}

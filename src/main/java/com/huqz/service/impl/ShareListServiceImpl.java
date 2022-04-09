package com.huqz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huqz.mapper.ShareListMapper;
import com.huqz.model.ShareList;
import com.huqz.service.ShareListService;
import org.springframework.stereotype.Service;

@Service
public class ShareListServiceImpl extends ServiceImpl<ShareListMapper, ShareList> implements ShareListService {
}

package com.huqz.service.impl;

import com.huqz.service.CodeService;
import com.huqz.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeUtils codeUtils;

    @Override
    public String genCode() {
        return codeUtils.genCode();
    }

}

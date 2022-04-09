package com.huqz.controller;

import com.huqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public void getInfo() {

    }

    @PutMapping
    public void changePwd() {

    }

    @PostMapping
    public void updateInfo() {

    }

}

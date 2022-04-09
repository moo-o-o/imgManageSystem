package com.huqz.controller;

import com.huqz.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imgs")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public void upload() {

    }

    @GetMapping("/list")
    public void list() {

    }

    @PostMapping("/del")
    public void del(){

    }

    @PostMapping("/update")
    public void update() {

    }

    @GetMapping("/refresh_uri")
    public void refreshUrl() {

    }

    @GetMapping("/download/{urn}")
    public void download() {

    }

    @GetMapping("/view/{urn}")
    public void view() {

    }

    @PostMapping("/confirm_to_visit")
    public void recover() {

    }






}

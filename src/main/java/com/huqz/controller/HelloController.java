package com.huqz.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hell() {
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }

    @GetMapping("/db/hello")
    public String dba() {
        return "hello dba";
    }

    @GetMapping("/admin/register")
    public Object register() {
        String encode = new BCryptPasswordEncoder(10).encode("123");
        return encode;
    }

}

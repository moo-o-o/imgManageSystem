package com.huqz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImgsSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgsSecurityApplication.class, args);
    }

}

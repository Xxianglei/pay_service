package com.xianglei.reserve_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //开启路径后缀匹配 自己可以取任意名字后缀
        configurer.setUseRegisteredSuffixPatternMatch(true).setUseTrailingSlashMatch(true);
    }
}
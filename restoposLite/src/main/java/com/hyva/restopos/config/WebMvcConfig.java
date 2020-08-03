package com.hyva.restopos.config;

import com.hyva.restopos.Interceptor.Interceptor.UserInterceptor;
import com.hyva.restopos.util.FileSystemOperations;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

/**
 * @Author udaybhaskar
 * Created on 6/29/17.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    Logger logger = Logger.getLogger(WebMvcConfig.class);
    @Autowired
    UserInterceptor requestInterceptor;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imageDir = FileSystemOperations.getImagesDirWithoutTenant();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imageDir + File.separator);
        String imageDir1 = FileSystemOperations.getBackupDir();
        registry.addResourceHandler("/BackupFolder/**")
                .addResourceLocations("file:" + imageDir1 + File.separator);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**");
    }
}

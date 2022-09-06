package com.example.cachecontrol;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {


    @Bean
    public CacheControlInterceptor cacheControlInterceptor() {
        return new CacheControlInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry
                .addInterceptor(cacheControlInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**");
    }
}

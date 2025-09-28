package com.embula.embula_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELEET";

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){


            public void addCorsMapping(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedMethods(GET,POST,PUT,DELETE)
                        .allowedOriginPatterns("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);

            }
        };
    }

}

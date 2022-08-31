package com.project.jiguhada.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig: WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://127.0.0.1:5173/")
            .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
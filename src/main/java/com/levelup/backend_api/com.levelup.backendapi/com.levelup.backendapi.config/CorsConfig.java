package com.levelup.backendapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Spring Boot detecta esta clase automáticamente
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplicar CORS a TODAS las rutas que empiezan con /api/
                .allowedOrigins("http://localhost:3000") // Permite SÓLO a tu React acceder
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*"); // Permite cualquier tipo de encabezado
    }
}
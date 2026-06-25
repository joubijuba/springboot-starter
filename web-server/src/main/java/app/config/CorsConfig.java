package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Cors config and preflight handler.
     * IMPORTANT : allowCredentials doesn't work with allowedOrigins = *
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) { 
        registry.addMapping("/**") 
            .allowedOrigins("http://localhost")
            .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
            .allowedHeaders("Content-Type","Authorization")
            .allowCredentials(true); 
    }
}
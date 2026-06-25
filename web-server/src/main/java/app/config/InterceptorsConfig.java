package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.interceptors.AuthInterceptor;
import core.services.JwtService;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    public InterceptorsConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Interceptor for JWT authentication. 
     * All guarded routes must be added in "addPathPatterns"
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtService)).addPathPatterns("/products/**");
    }
}

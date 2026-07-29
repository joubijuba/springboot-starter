package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.interceptors.AuthInterceptor;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public InterceptorsConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * Interceptor for JWT authentication. 
     * All guarded routes must be added in "addPathPatterns"
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authInterceptor).addPathPatterns("/products/**");
    }
}

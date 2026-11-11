package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.interceptors.AuthInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InterceptorsConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    /**
     * Interceptor for JWT authentication. 
     * All guarded routes must be added in the addPathPatterns method
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authInterceptor).addPathPatterns("/products/**");
    }
}

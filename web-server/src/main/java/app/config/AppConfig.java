package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import core.services.JwtService;

@Configuration
public class AppConfig {

    /**
     * Generates bean (singleton) of JwtService to be injected in any class
     * @return jwt service 
     */
    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

}

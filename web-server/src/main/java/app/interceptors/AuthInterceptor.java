package app.interceptors;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import app.exceptions.UnauthorizedException;
import core.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor to verify JWT token from the "auth_token" cookie. Applied to guarded routes declared in InterceptorsConfig.java
 */
@Component // To make instance of AuthInterceptor a bean
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Interceptor middleware to check token validity. Will throw if token is missing, invalid or expired
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();

        try {
            // Extract auth_token from cookies (will throw if cookies is null or empty)
            Cookie cookie = Arrays.stream(cookies)
                .filter(item -> item.getName().equals("auth_token"))
                .findFirst()
                .get();

            Claims claims = this.jwtService.decodeToken(cookie.getValue());

            // Now insert cookie content (proper claims object) into the request
            // TO DO : use Spring Security
            request.setAttribute("cookieContent", claims);

            return true;
        } catch (Exception e){
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
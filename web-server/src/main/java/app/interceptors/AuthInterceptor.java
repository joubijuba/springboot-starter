package app.interceptors;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import app.exceptions.UnauthorizedException;
import core.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Interceptor to verify jwt extracted from the "auth_token" cookie
 */
@Component 
@RequiredArgsConstructor 
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    /**
     * Interceptor middleware to check token validity. Will throw if token is missing, invalid or expired
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Cookie[] cookies = request.getCookies();

            Cookie cookie = Arrays.stream(cookies)
                .filter(item -> item.getName().equals("auth_token"))
                .findFirst()
                .get();

            Map<String, Object> claims = jwtService.decodeToken(cookie.getValue());

            // Now insert cookie content (proper claims object) into the request
            request.setAttribute("cookieContent", claims);

            return true;
        } catch (Exception e){
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
package app.interceptors;

import java.util.Arrays;

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
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Interceptor middleware to check token validity. Will throw if token is missing, invalid or expired
     */
    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // Cookie[] cookies = request.getCookies();

    // try {
    // // Extract auth_token from cookies (will throw if cookies is null or empty)
    // Cookie cookie = Arrays.stream(cookies)
    // .filter(item -> item.getName().equals("auth_token"))
    // .findFirst()
    // .orElse(null);

    // Claims claims = this.jwtService.decodeToken(cookie.getValue());

    // // Now insert cookie content (proper claims object) into the request
    // // TO DO : use Spring Security
    // request.setAttribute("cookieContent", claims);

    // return true;
    // } catch (Exception e){
    // throw new UnauthorizedException("Invalid or expired token");
    // }
    // }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler){

        Cookie[] cookies = req.getCookies();

        Cookie authCookie = Arrays.stream(cookies).filter(o -> o.getName() == "auth_token").findFirst().get();

        Claims decodedCookie = this.jwtService.decodeToken(authCookie.getValue());

        req.setAttribute("decodedCookie", decodedCookie);

        return true;
    }
}
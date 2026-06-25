package app.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.UserCredentialsDto;
import app.services.AuthService;
import app.utils.HttpWrapper;
import core.utils.ResponseWrapper;
import core.dtos.WrappedResponseDto;
import jakarta.validation.Valid;

/**
 * Controller for authentication logic
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login route
     * @param userCredentials 
     * @return response with authentication cookie in header 
     */
    @PostMapping("/login")
    HttpEntity<WrappedResponseDto<?>> login(@Valid @RequestBody UserCredentialsDto userCredentials) {
        String jwt = authService.login(userCredentials);
        return HttpWrapper.fromWrToHttpWithCookie(ResponseWrapper.OK("You successfully logged in"), jwt);
    }
}
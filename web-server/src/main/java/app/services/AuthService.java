package app.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.dtos.UserCredentialsDto;
import app.entities.User;
import app.exceptions.InternalServerError;
import app.exceptions.NotFoundException;
import app.exceptions.UnauthorizedException;
import app.repositories.UserRepository;
import core.services.JwtService;

/**
 * Service for authentication logic
 */
@Service 
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Basic login service, returns authentification token
     * @param userCredentials
     * @return JWT 
     */
    public String login(UserCredentialsDto userCredentials) {
        // check if user exists
        Optional<User> userDatas = this.userRepository.findById(userCredentials.email());

        if (userDatas.isEmpty()) {
            throw new NotFoundException("Incorrect username or password");
        }

        String concatenation = String.format("%s%s", userDatas.get().getSalt(), userCredentials.password());
        Boolean isPwCorrect = encoder.matches(concatenation, userDatas.get().getHash());

        if (!isPwCorrect) {
            throw new UnauthorizedException("Incorrect username or password");
        }

        try {
            // generate a map object that matches with CookieContentDto 
            Map<String, String> map = new HashMap<>();
            map.put("userId", userDatas.get().getId());
            map.put("companyId", userDatas.get().getCompanyId().toString()); // JWT Claim object accepts only basic types 

            return this.jwtService.generateToken(map);
        } catch (Exception e) {
            System.err.println("AuthService:login: ERROR 01: " + userCredentials.email() + ": " + e.toString());
            throw new InternalServerError("Server side error while generating token, please retry");
        }
    }
}

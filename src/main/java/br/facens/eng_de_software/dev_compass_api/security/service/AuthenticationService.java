package br.facens.eng_de_software.dev_compass_api.security.service;

import br.facens.eng_de_software.dev_compass_api.security.repository.UserRepository;

import javax.naming.AuthenticationException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationService(
            JwtService jwtService,
            UserRepository baseUserRepository) {
        this.jwtService = jwtService;
        this.userRepository = baseUserRepository;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public BaseUser getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByAuthenticationDataUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationException("User not authenticated."));
    }
}

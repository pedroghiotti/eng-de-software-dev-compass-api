package br.facens.eng_de_software.dev_compass_api.security.service;
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;

@Service
public class AuthenticationService {
    private final BaseUserRepository baseUserRepository;
    private final JwtService jwtService;

    public AuthenticationService(
        JwtService jwtService,
        BaseUserRepository baseUserRepository
    ) {
        this.jwtService = jwtService;
        this.baseUserRepository = baseUserRepository;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public Optional<BaseUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        return Optional.of(baseUserRepository.findByAuthenticationDataUsername(authentication.getName())
            .orElseThrow(() -> new IllegalStateException("Authentication error")));
    }
}

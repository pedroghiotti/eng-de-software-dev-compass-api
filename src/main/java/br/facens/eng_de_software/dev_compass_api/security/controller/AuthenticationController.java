package br.facens.eng_de_software.dev_compass_api.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import br.facens.eng_de_software.dev_compass_api.security.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(
        @Autowired AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
    
}

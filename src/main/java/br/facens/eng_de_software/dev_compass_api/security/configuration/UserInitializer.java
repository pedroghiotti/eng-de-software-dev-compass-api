package br.facens.eng_de_software.dev_compass_api.security.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.security.model.Admin;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class UserInitializer {

    private final BaseUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(BaseUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        BaseUser business = new Business(
            "business",
            passwordEncoder.encode("business@123")
        );
        userRepository.save(business);

        BaseUser business2 = new Business(
            "business2",
            passwordEncoder.encode("business2@123")
        );
        userRepository.save(business2);
        
        BaseUser candidate = new Candidate(
            "candidate",
            passwordEncoder.encode("candidate@123")
        );
        userRepository.save(candidate);

        BaseUser admin = new Admin(
            "admin",
            passwordEncoder.encode("admin@123")
        );
        userRepository.save(admin);
    }
}
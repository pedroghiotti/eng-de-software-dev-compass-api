package br.facens.eng_de_software.dev_compass_api.security.configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.security.model.Admin;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
@Profile("dev")
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

        BaseUser admin = new Admin(
            "admin",
            passwordEncoder.encode("admin@123")
        );
        userRepository.save(admin);
    }
}
package br.facens.eng_de_software.dev_compass_api.security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;

public interface GenericUserRepository<TUser extends BaseUser> extends JpaRepository<TUser, UUID> {
    public Optional<TUser> findByAuthenticationDataUsername(String username);
}

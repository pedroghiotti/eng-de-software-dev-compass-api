package br.facens.eng_de_software.dev_compass_api.security.model;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private AuthenticationData authenticationData;

    @Enumerated(EnumType.STRING)
    private Role role;

    public BaseUser(String username, String password, Role role) {
        this.authenticationData = new AuthenticationData(username, password);
        this.role = role;
    }

    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.authenticationData.getUsername();
    }

    public void setUsername(String username) {
        this.authenticationData.setUsername(username);
    }

    public String getPassword() {
        return this.authenticationData.getPassword();
    }

    public void setPassword(String password) {
        this.authenticationData.setPassword(password);
    }

    public Role getRole() {
        return this.role;
    }

    public String getAuthority() {
        return this.role.getAuthority();
    }
}

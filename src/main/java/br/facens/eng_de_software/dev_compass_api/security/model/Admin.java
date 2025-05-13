package br.facens.eng_de_software.dev_compass_api.security.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends BaseUser{
    public Admin(String username, String password) {
        super(username, password, Role.ADMIN);
    }
}

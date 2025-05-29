package br.facens.eng_de_software.dev_compass_api.model;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import jakarta.persistence.Entity;

@Entity
public class Business extends BaseUser {
    public Business(String username, String password) {
        super(username, password, Role.BUSINESS);
    }

    public Business() {
        this(null, null);
    }
}

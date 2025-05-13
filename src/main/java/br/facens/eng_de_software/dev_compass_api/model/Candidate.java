package br.facens.eng_de_software.dev_compass_api.model;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import jakarta.persistence.Entity;

@Entity
public class Candidate extends BaseUser {
    public Candidate(String username, String password) {
        super(username, password, Role.CANDIDATE);
    }

    public Candidate() {
        this(null, null);
    }
}

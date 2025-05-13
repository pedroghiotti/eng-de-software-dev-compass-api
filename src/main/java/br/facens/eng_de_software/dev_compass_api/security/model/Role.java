package br.facens.eng_de_software.dev_compass_api.security.model;

public enum Role {
    CANDIDATE,
    BUSINESS,
    ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }

    public String getAuthority() {
        return this.name();
    }
}

package br.facens.eng_de_software.dev_compass_api.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AuthenticationData {
    @Column(unique = true)
    private String username;
    private String password;

    public AuthenticationData() {
        this.username = null;
        this.password = null;
    }

    public AuthenticationData(String username, String password) {
        this.username = username;
        this.password = password;
        this.validate();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void validate() {
        /*
         * Método não implementado.
         * Aqui, seriam realizadas validações de validez de senha e nome de usuário
         * conforme regras de negócio a respeito.
         */
    }
}

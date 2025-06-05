package br.facens.eng_de_software.dev_compass_api.security.dto;

import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import lombok.Getter;

@Getter
public class BaseUserCreateDto {
    private final String username;
    private final String password;
    private final Role role;

    public BaseUserCreateDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

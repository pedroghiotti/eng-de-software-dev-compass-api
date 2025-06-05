package br.facens.eng_de_software.dev_compass_api.security.dto;

import java.util.UUID;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import lombok.Getter;

@Getter
public class BaseUserResponseDto {
    private final UUID id;
    private final String username;
    private final Role role;

    public BaseUserResponseDto(UUID id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static BaseUserResponseDto fromUser(BaseUser baseUser) {
        return new BaseUserResponseDto(
            baseUser.getId(),
            baseUser.getUsername(),
            baseUser.getRole()
        );
    }
}

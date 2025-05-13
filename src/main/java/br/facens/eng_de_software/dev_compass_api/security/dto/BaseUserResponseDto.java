package br.facens.eng_de_software.dev_compass_api.security.dto;

import java.util.UUID;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;

public record BaseUserResponseDto(UUID id, String username, Role role) {
    public static BaseUserResponseDto fromBaseUser(BaseUser baseUser) {
        return new BaseUserResponseDto(
            baseUser.getId(),
            baseUser.getUsername(),
            baseUser.getRole()
        );
    }
}

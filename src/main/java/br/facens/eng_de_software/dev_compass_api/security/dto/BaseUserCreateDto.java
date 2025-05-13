package br.facens.eng_de_software.dev_compass_api.security.dto;

import br.facens.eng_de_software.dev_compass_api.security.model.Role;

public record BaseUserCreateDto(String username, String password, Role role) {

}

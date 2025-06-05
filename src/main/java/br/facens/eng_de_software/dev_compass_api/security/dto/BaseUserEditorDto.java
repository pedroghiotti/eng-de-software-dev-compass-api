package br.facens.eng_de_software.dev_compass_api.security.dto;

import lombok.Getter;

@Getter
public class BaseUserEditorDto {
    private final String username;
    private final String password;

    public BaseUserEditorDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

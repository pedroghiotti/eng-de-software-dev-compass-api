package br.facens.eng_de_software.dev_compass_api.dto;

import br.facens.eng_de_software.dev_compass_api.model.Technology;

import java.util.UUID;

public record TechnologyResponseDto(UUID id, String name) {
    public static TechnologyResponseDto fromTechnology(Technology technology) {
        return new TechnologyResponseDto(
            technology.getId(),
            technology.getName()
        );
    }
}

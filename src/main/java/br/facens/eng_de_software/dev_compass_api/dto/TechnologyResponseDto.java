package br.facens.eng_de_software.dev_compass_api.dto;

import br.facens.eng_de_software.dev_compass_api.model.Technology;

public record TechnologyResponseDto(String name) {
    public static TechnologyResponseDto fromTechnology(Technology technology) {
        return new TechnologyResponseDto(
            technology.getName()
        );
    }
}

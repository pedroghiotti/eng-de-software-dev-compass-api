package br.facens.eng_de_software.dev_compass_api.dto.response;

import br.facens.eng_de_software.dev_compass_api.model.Category;

import java.util.UUID;

public record CategoryResponseDto(UUID id, String name) {
    public static CategoryResponseDto fromCategory(Category category) {
        return new CategoryResponseDto(
            category.getId(),
            category.getName()
        );
    }
}

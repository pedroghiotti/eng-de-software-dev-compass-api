package br.facens.eng_de_software.dev_compass_api.dto;

import br.facens.eng_de_software.dev_compass_api.model.Region;

public record RegionResponseDto(String name) {
    public static RegionResponseDto fromRegion(Region region) {
        return new RegionResponseDto(
            region.getName()
        );
    }
}

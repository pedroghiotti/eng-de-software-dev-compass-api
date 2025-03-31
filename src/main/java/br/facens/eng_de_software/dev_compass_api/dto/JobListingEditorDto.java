package br.facens.eng_de_software.dev_compass_api.dto;

import java.util.List;
import java.util.UUID;

public record JobListingEditorDto(UUID regionId, List<UUID> technologyIds) {
}

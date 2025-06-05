package br.facens.eng_de_software.dev_compass_api.dto.request;

import java.util.Set;
import java.util.UUID;

import br.facens.eng_de_software.dev_compass_api.model.Benefit;
import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;

public record JobListingEditorDto(String title, String description, UUID regionId, Set<UUID> technologyIds, Set<UUID> categoryIds, CompensationPackageEditorDto compensationPackageEditorDto) {
    public JobListingEditorDto {
        validate();
    }

    private void validate() {
        if (
            this.title == null || this.title.isBlank() ||
            this.description == null || this.description.isBlank() ||
            this.regionId == null ||
            this.technologyIds == null || this.technologyIds.isEmpty() ||
            this.categoryIds == null || this.categoryIds.isEmpty()
        ) throw new IllegalArgumentException();

        new CompensationPackage(
            compensationPackageEditorDto.salary(),
            compensationPackageEditorDto.benefits().toArray(Benefit[]::new)
        );
    }
}

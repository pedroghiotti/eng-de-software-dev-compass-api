package br.facens.eng_de_software.dev_compass_api.dto;

import java.util.List;
import java.util.UUID;

import br.facens.eng_de_software.dev_compass_api.model.JobListingState;

public record JobListingEditorDto(String title, String description, JobListingState state, UUID regionId,
        List<UUID> technologyIds) {
    public JobListingEditorDto(
            String title,
            String description,
            JobListingState state,
            UUID regionId,
            List<UUID> technologyIds) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.regionId = regionId;
        this.technologyIds = technologyIds;

        this.validate();
    }

    private void validate() {
        if (this.title == null || this.title.isBlank() ||
                this.description == null || this.description.isBlank() ||
                this.state == null ||
                this.regionId == null ||
                this.technologyIds == null || this.technologyIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

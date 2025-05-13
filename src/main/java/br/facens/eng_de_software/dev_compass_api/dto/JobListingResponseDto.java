package br.facens.eng_de_software.dev_compass_api.dto;

import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.JobListingState;

import java.util.List;
import java.util.UUID;

public record JobListingResponseDto(UUID id, String title, String description, JobListingState state, RegionResponseDto region, List<TechnologyResponseDto> technologyIds) {
    public static JobListingResponseDto fromJobListing(JobListing jobListing) {
        return new JobListingResponseDto(
            jobListing.getId(),
            jobListing.getTitle(),
            jobListing.getDescription(),
            jobListing.getState(),
            RegionResponseDto.fromRegion(jobListing.getRegion()),
            jobListing.getTechnologies().stream().map(TechnologyResponseDto::fromTechnology).toList()
        );
    }
}

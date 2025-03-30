package br.facens.eng_de_software.dev_compass_api.dto;

import br.facens.eng_de_software.dev_compass_api.model.JobListing;

import java.util.List;
import java.util.UUID;

public record JobListingResponseDto(UUID id, RegionResponseDto region, List<TechnologyResponseDto> technologies) {
    public static JobListingResponseDto fromJobListing(JobListing jobListing) {
        return new JobListingResponseDto(
            jobListing.getId(),
            RegionResponseDto.fromRegion(jobListing.getRegion()),
            jobListing.getTechnologies().stream().map(TechnologyResponseDto::fromTechnology).toList()
        );
    }
}

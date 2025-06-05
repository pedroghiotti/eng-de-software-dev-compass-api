package br.facens.eng_de_software.dev_compass_api.dto.response;

import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.JobListingState;

import java.util.List;
import java.util.UUID;

public record JobListingResponseDto(UUID id, String title, String description, JobListingState state, RegionResponseDto region, List<TechnologyResponseDto> technologies, List<CategoryResponseDto> categories, CompensationPackageResponseDto compensationPackageResponseDto) {
    public static JobListingResponseDto fromJobListing(JobListing jobListing) {
        return new JobListingResponseDto(
            jobListing.getId(),
            jobListing.getTitle(),
            jobListing.getDescription(),
            jobListing.getState(),
            RegionResponseDto.fromRegion(jobListing.getRegion()),
            jobListing.getTechnologies().stream().map(TechnologyResponseDto::fromTechnology).toList(),
            jobListing.getCategories().stream().map(CategoryResponseDto::fromCategory).toList(),
            CompensationPackageResponseDto.fromCompensationPackage(jobListing.getCompensationPackage())
        );
    }
}

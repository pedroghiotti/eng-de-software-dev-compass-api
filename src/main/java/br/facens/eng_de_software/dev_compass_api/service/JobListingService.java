package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.JobListingEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.JobListingResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobListingService {
    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

     public JobListingResponseDto create(UUID id, JobListingEditorDto editorDto) throws Exception{
        Region newJobListingRegion = regionRepository.findById(editorDto.regionId())
                .orElseThrow(RuntimeException::new);

        List<Technology> newJobListingTechnologies = editorDto.technologyIds().stream().map(
                technologyName -> technologyRepository.findById(technologyName)
                        .orElseThrow(RuntimeException::new)
        ).toList();

        JobListing newJobListing = new JobListing(
                id,
                newJobListingRegion,
                newJobListingTechnologies
        );

        jobListingRepository.save(newJobListing);

        return JobListingResponseDto.fromJobListing(newJobListing);
    }

    public JobListingResponseDto create(JobListingEditorDto editorDto) throws Exception {
        UUID newJobListingId = UUID.randomUUID();
        return create(newJobListingId, editorDto);
    }

    public JobListingResponseDto getById(UUID id) throws Exception{
        return JobListingResponseDto.fromJobListing(
            jobListingRepository.findById(id)
                .orElseThrow(Exception::new)
        );
    }

    public List<JobListingResponseDto> getAll() {
        return jobListingRepository.findAll().stream()
            .map(JobListingResponseDto::fromJobListing).toList();
    }

    public JobListingResponseDto update(UUID id, JobListingEditorDto editorDto) throws Exception {
        JobListing jobListing = jobListingRepository.findById(id)
            .orElseThrow(Exception::new);

        jobListing.setTechnologies(
            editorDto.technologyIds().stream().map(technologyId ->
                technologyRepository.findById(technologyId).orElseThrow(RuntimeException::new)
            ).toList()
        );

        jobListing.setRegion(
            regionRepository.findById(editorDto.regionId()).orElseThrow(Exception::new)
        );

        jobListingRepository.save(jobListing);

        return JobListingResponseDto.fromJobListing(jobListing);
    }

    public void deleteById(UUID id) {
        jobListingRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return jobListingRepository.existsById(id);
    }
}

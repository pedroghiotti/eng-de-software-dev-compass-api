package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.JobListingEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.JobListingResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import br.facens.eng_de_software.dev_compass_api.security.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobListingService {
    private final AuthenticationService authenticationService;
    private final JobListingRepository jobListingRepository;
    private final RegionRepository regionRepository;
    private final TechnologyRepository technologyRepository;

    public JobListingService(
            JobListingRepository jobListingRepository,
            RegionRepository regionRepository,
            TechnologyRepository technologyRepository,
            AuthenticationService authenticationService) {
        this.jobListingRepository = jobListingRepository;
        this.regionRepository = regionRepository;
        this.technologyRepository = technologyRepository;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    public JobListingResponseDto create(UUID id, JobListingEditorDto editorDto) throws Exception {
        if (!regionRepository.existsById(editorDto.regionId()))
            throw new RuntimeException();
        Region region = regionRepository.getReferenceById(editorDto.regionId());
        List<Technology> technologies = editorDto.technologyIds().stream().map(
                technologyName -> {
                    if (!technologyRepository.existsById(technologyName))
                        throw new RuntimeException();
                    return technologyRepository.getReferenceById(technologyName);
                }).toList();
        Business owner = (Business) authenticationService.getCurrentUser();
        JobListing newJobListing = new JobListing(
                id,
                editorDto.title(),
                editorDto.description(),
                region,
                owner,
                technologies);
        jobListingRepository.save(newJobListing);
        return JobListingResponseDto.fromJobListing(newJobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    public JobListingResponseDto create(JobListingEditorDto createDto) throws Exception {
        UUID newJobListingId = UUID.randomUUID();
        return create(newJobListingId, createDto);
    }

    public JobListingResponseDto getById(UUID id) throws Exception {
        return JobListingResponseDto.fromJobListing(_getById(id));
    }

    public List<JobListingResponseDto> getAll(String regionName) {
        List<JobListing> jobListings;
        if (!(regionName == null) && !(regionName.isBlank()))
            jobListings = jobListingRepository.findAllByRegionName(regionName);
        else
            jobListings = jobListingRepository.findAll();
        return jobListings.stream().map(JobListingResponseDto::fromJobListing).toList();
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public JobListingResponseDto update(UUID id, JobListingEditorDto editorDto) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);

        List<Technology> technologies = editorDto.technologyIds().stream().distinct().map(
                technologyId -> technologyRepository.findById(technologyId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Technology not found with id %s", id))))
                .collect(Collectors.toCollection(ArrayList::new));

        Region region = regionRepository.findById(editorDto.regionId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Region not found with id %s", id)));

        jobListing.setTitle(editorDto.title());
        jobListing.setDescription(editorDto.description());
        jobListing.setTechnologies(technologies);
        jobListing.setRegion(region);

        jobListingRepository.save(jobListing);
        return JobListingResponseDto.fromJobListing(jobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void deleteById(UUID id) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);

        jobListingRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return jobListingRepository.existsById(id);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void unpublish(UUID id) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);
        jobListing.unpublish();
        jobListingRepository.save(jobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void publish(UUID id) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);
        jobListing.publish();
        jobListingRepository.save(jobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void startSelection(UUID id) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);
        jobListing.beginCandidateSelection();
        jobListingRepository.save(jobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void close(UUID id) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);
        jobListing.close();
        jobListingRepository.save(jobListing);
    }

    private JobListing _getById(UUID id) {
        return jobListingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Job listing not found with id %s", id)));
    }

    private void verifyOwnership(JobListing jobListing) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(jobListing.getOwner().getId()))
            throw new AuthorizationDeniedException("User not authorized. Operation disallowed.");
    }
}

package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.request.JobListingEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.JobListingResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Benefit;
import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.notifications.model.JobListingPublishedEvent;
import br.facens.eng_de_software.dev_compass_api.repository.CategoryRepository;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import br.facens.eng_de_software.dev_compass_api.security.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobListingService {
    private final AuthenticationService authenticationService;
    private final JobListingRepository jobListingRepository;
    private final RegionRepository regionRepository;
    private final TechnologyRepository technologyRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public JobListingService(
        AuthenticationService authenticationService,
        JobListingRepository jobListingRepository,
        RegionRepository regionRepository,
        TechnologyRepository technologyRepository,
        CategoryRepository categoryRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.authenticationService = authenticationService;
        this.jobListingRepository = jobListingRepository;
        this.regionRepository = regionRepository;
        this.technologyRepository = technologyRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    public JobListingResponseDto create(UUID id, JobListingEditorDto editorDto) throws Exception {
        if (!regionRepository.existsById(editorDto.regionId()))
            throw new RuntimeException();

        Region region = regionRepository.getReferenceById(editorDto.regionId());

        Set<Technology> technologies = editorDto.technologyIds().stream().map(technologyId -> {
                if (!technologyRepository.existsById(technologyId)) throw new RuntimeException();
                return technologyRepository.getReferenceById(technologyId);
            }
        ).collect(Collectors.toCollection(HashSet::new));

        Set<Category> categories = editorDto.categoryIds().stream().map(categoryId -> {
                if (!categoryRepository.existsById(categoryId)) throw new RuntimeException();
                return categoryRepository.getReferenceById(categoryId);
            }
        ).collect(Collectors.toCollection(HashSet::new));

        Business owner = (Business) authenticationService.getCurrentUser();

        CompensationPackage compensationPackage = new CompensationPackage(
            editorDto.compensationPackageEditorDto().salary(),
            editorDto.compensationPackageEditorDto().benefits().toArray(Benefit[]::new)
        );

        JobListing newJobListing = new JobListing(
            id,
            editorDto.title(),
            editorDto.description(),
            region,
            owner,
            technologies,
            categories,
            compensationPackage
        );
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

    public List<JobListingResponseDto> getAllByRegionId(UUID regionId) {
        List<JobListing> jobListings;
        if (regionId != null)
            jobListings = jobListingRepository.findAllByRegionId(regionId);
        else
            jobListings = jobListingRepository.findAll();
        return jobListings.stream().map(JobListingResponseDto::fromJobListing).toList();
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public JobListingResponseDto update(UUID id, JobListingEditorDto editorDto) throws Exception {
        JobListing jobListing = _getById(id);
        verifyOwnership(jobListing);

        Set<Technology> technologies = editorDto.technologyIds().stream().distinct().map(
                technologyId -> technologyRepository.findById(technologyId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Technology not found with id %s", id))))
                .collect(Collectors.toCollection(HashSet::new));

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
        eventPublisher.publishEvent(new JobListingPublishedEvent(this, jobListing));
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

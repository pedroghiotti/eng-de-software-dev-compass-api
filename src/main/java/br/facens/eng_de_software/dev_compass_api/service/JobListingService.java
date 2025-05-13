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
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;
import br.facens.eng_de_software.dev_compass_api.security.service.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobListingService {

    private final BaseUserRepository baseUserRepository;
    private final AuthenticationService authenticationService;
    private final JobListingRepository jobListingRepository;
    private final RegionRepository regionRepository;
    private final TechnologyRepository technologyRepository;

    public JobListingService(
        JobListingRepository jobListingRepository,
        RegionRepository regionRepository,
        TechnologyRepository technologyRepository,
        AuthenticationService authenticationService,
        BaseUserRepository baseUserRepository
    ) {
        this.jobListingRepository = jobListingRepository;
        this.regionRepository = regionRepository;
        this.technologyRepository = technologyRepository;
        this.authenticationService = authenticationService;
        this.baseUserRepository = baseUserRepository;
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public JobListingResponseDto create(UUID id, JobListingEditorDto editorDto) throws Exception {
        if (!regionRepository.existsById(editorDto.regionId())) throw new RuntimeException();
        Region region = regionRepository.getReferenceById(editorDto.regionId());
        
        List<Technology> technologies = editorDto.technologyIds().stream().map(
            technologyName -> {
                if(!technologyRepository.existsById(technologyName)) throw new RuntimeException();
                return technologyRepository.getReferenceById(technologyName);
            }
        ).toList();
    
        Business owner = (Business) authenticationService.getCurrentUser().orElseThrow(Exception::new);
        
        JobListing newJobListing = new JobListing(
            id,
            editorDto.title(),
            editorDto.description(),
            editorDto.state(),
            region,
            owner,
            technologies
        );
        jobListingRepository.save(newJobListing);

        owner.addManagedJobListing(newJobListing);
        baseUserRepository.save(owner);

        return JobListingResponseDto.fromJobListing(newJobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public JobListingResponseDto create(JobListingEditorDto createDto) throws Exception {
        UUID newJobListingId = UUID.randomUUID();
        return create(newJobListingId, createDto);
    }

    public JobListingResponseDto getById(UUID id) throws Exception {
        return JobListingResponseDto.fromJobListing(
            jobListingRepository.findById(id)
                .orElseThrow(Exception::new)
        );
    }

    public List<JobListingResponseDto> getAll(String regionName) {
        List<JobListing> jobListings;
        
        if (!(regionName == null) && !(regionName.isBlank())) {
            jobListings = jobListingRepository.findAllByRegionName(regionName);
        } else {
            jobListings = jobListingRepository.findAll();
        }
        
        return jobListings.stream().map(JobListingResponseDto::fromJobListing).toList();
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public JobListingResponseDto update(UUID id, JobListingEditorDto editorDto) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser().orElseThrow(Exception::new);
        JobListing jobListing = jobListingRepository.findById(id).orElseThrow(Exception::new);

        if (
            !currentUser.getId().equals(jobListing.getBusiness().getId()) &&
            !currentUser.getRole().equals(Role.ADMIN)
        ) throw new Exception();

        jobListing.setTechnologies(
            editorDto.technologyIds().stream().distinct().map(technologyId ->
                technologyRepository.findById(technologyId).orElseThrow(RuntimeException::new)
            ).toList()
        );

        jobListing.setRegion(
            regionRepository.findById(editorDto.regionId()).orElseThrow(Exception::new)
        );

        jobListingRepository.save(jobListing);

        return JobListingResponseDto.fromJobListing(jobListing);
    }

    @PreAuthorize("hasAuthority('BUSINESS') || hasAuthority('ADMIN')")
    public void deleteById(UUID id) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser().orElseThrow(Exception::new);
        Optional<JobListing> jobListing = jobListingRepository.findById(id);

        if (jobListing.isEmpty()) return;

        if (
            !currentUser.getId().equals(jobListing.get().getBusiness().getId()) &&
            !currentUser.getRole().equals(Role.ADMIN)
        ) {
            throw new Exception();
        }

        jobListingRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return jobListingRepository.existsById(id);
    }
}

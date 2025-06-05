package br.facens.eng_de_software.dev_compass_api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.dto.request.CandidateCreateDto;
import br.facens.eng_de_software.dev_compass_api.dto.request.CandidateEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.CandidateResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.CandidateRepository;
import br.facens.eng_de_software.dev_compass_api.repository.CategoryRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.service.BaseUserService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final BaseUserService baseUserService;
    private final CategoryRepository categoryRepository;
    private final TechnologyRepository technologyRepository;
    private final RegionRepository regionRepository;

    public CandidateService(
        CandidateRepository candidateRepository,
        PasswordEncoder passwordEncoder,
        BaseUserService baseUserService,
        CategoryRepository categoryRepository,
        TechnologyRepository technologyRepository,
        RegionRepository regionRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.baseUserService = baseUserService;
        this.categoryRepository = categoryRepository;
        this.technologyRepository = technologyRepository;
        this.regionRepository = regionRepository;
    }

    public CandidateResponseDto create(CandidateCreateDto createDto) throws Exception {
        String encodedPassword = passwordEncoder.encode(createDto.getPassword());
        Candidate newCandidate = new Candidate(
            createDto.getUsername(),
            encodedPassword,
            createDto.getPreferredNotificationChannels(),
            createDto.getPreferredTechnologyIds().stream()
                .map(technologyId -> technologyRepository
                    .findById(technologyId).orElseThrow(() -> new EntityNotFoundException(String.format("Technology not found with ID %s.", technologyId)))
                ).collect(Collectors.toCollection(HashSet<Technology>::new)),
            createDto.getPreferredCategoryIds().stream()
                .map(categoryId -> categoryRepository
                    .findById(categoryId).orElseThrow(() -> new EntityNotFoundException(String.format("Category not found with ID %s.", categoryId)))
                ).collect(Collectors.toCollection(HashSet<Category>::new)),
            regionRepository.findById(createDto.getPreferredRegionId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Region not found with ID %s.", createDto.getPreferredRegionId())))
        );
        
        candidateRepository.save(newCandidate);
        return CandidateResponseDto.fromUser(newCandidate);
    }

    public CandidateResponseDto getById(UUID id) throws Exception {
        return CandidateResponseDto.fromUser(_getById(id));
    }

    public List<CandidateResponseDto> getAll() {
        return candidateRepository.findAll().stream()
            .map(x -> CandidateResponseDto.fromUser((Candidate) x)).toList();
    }

    public CandidateResponseDto update(UUID id, CandidateEditorDto editorDto) throws Exception {
        Candidate candidate = _getById(id);
        baseUserService.verifyOwnership(candidate);

        Set<Category> preferredCategories = categoryRepository.findAllByIdIn(editorDto.getPreferredCategoryIds());
        Set<Technology> preferredTechnologies = technologyRepository.findAllByIdIn(editorDto.getPreferredTechnologyIds());
        Region preferredRegion = regionRepository.findById(editorDto.getPreferredRegionId()).orElseThrow();

        candidate.setUsername(editorDto.getUsername());
        candidate.setPassword(passwordEncoder.encode(editorDto.getPassword()));
        candidate.setPreferredCategories(preferredCategories);
        candidate.setPreferredTechnologies(preferredTechnologies);
        candidate.setPreferredNotificationChannels(editorDto.getPreferredNotificationChannels());
        candidate.setPreferredRegion(preferredRegion);

        candidateRepository.save(candidate);
        return CandidateResponseDto.fromUser(candidate);
    }

    public void deleteById(UUID id) throws Exception {
        Candidate candidate = _getById(id);
        baseUserService.verifyOwnership(candidate);
        candidateRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return candidateRepository.existsById(id);
    }

    private Candidate _getById(UUID id) {
        return (Candidate) candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Candidate not found with id %s", id)));
    }
}

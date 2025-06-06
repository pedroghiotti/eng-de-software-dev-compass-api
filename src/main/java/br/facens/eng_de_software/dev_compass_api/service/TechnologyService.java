package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.request.TechnologyEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.TechnologyResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TechnologyService {
    @Autowired
    TechnologyRepository technologyRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnologyResponseDto create(UUID id, TechnologyEditorDto editorDto) {
        Technology newTechnology = new Technology(id, editorDto.name());
        technologyRepository.save(newTechnology);
        return TechnologyResponseDto.fromTechnology(newTechnology);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnologyResponseDto create(TechnologyEditorDto editorDto) {
        UUID newTechnologyId = UUID.randomUUID();
        return this.create(newTechnologyId, editorDto);
    }

    public TechnologyResponseDto getById(UUID id) {
        return TechnologyResponseDto.fromTechnology(
                technologyRepository.findById(id)
                        .orElseThrow(RuntimeException::new)
        );
    }

    public List<TechnologyResponseDto> getAll() {
        return technologyRepository.findAll().stream()
                .map(TechnologyResponseDto::fromTechnology).toList();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public TechnologyResponseDto update(UUID id, TechnologyEditorDto editorDto) throws Exception {
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(Exception::new);

        BeanUtils.copyProperties(editorDto, technology);

        technologyRepository.save(technology);

        return TechnologyResponseDto.fromTechnology(technology);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(UUID id) {
        technologyRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return technologyRepository.existsById(id);
    }
}

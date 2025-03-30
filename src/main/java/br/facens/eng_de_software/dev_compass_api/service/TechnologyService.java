package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.TechnologyEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.TechnologyResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyService {
    @Autowired
    TechnologyRepository technologyRepository;

    public TechnologyResponseDto create(TechnologyEditorDto editorDto) {
        Technology newTechnology = new Technology(editorDto.name());
        technologyRepository.save(newTechnology);
        return TechnologyResponseDto.fromTechnology(newTechnology);
    }

    public TechnologyResponseDto getById(String id) {
        return TechnologyResponseDto.fromTechnology(
                technologyRepository.findById(id)
                        .orElseThrow(RuntimeException::new)
        );
    }

    public List<TechnologyResponseDto> getAll() {
        return technologyRepository.findAll().stream()
                .map(TechnologyResponseDto::fromTechnology).toList();
    }

    public TechnologyResponseDto update(String id, TechnologyEditorDto editorDto) throws Exception {
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(Exception::new);

        BeanUtils.copyProperties(editorDto, technology);

        technologyRepository.save(technology);

        return TechnologyResponseDto.fromTechnology(technology);
    }

    public void delete(String id) {
        technologyRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return technologyRepository.existsById(id);
    }
}

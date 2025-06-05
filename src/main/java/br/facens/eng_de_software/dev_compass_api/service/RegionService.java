package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.request.RegionEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.RegionResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RegionService {
    @Autowired
    RegionRepository regionRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public RegionResponseDto create(UUID id, RegionEditorDto editorDto) {
        Region newRegion = new Region(id, editorDto.name());
        regionRepository.save(newRegion);
        return RegionResponseDto.fromRegion(newRegion);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public RegionResponseDto create(RegionEditorDto editorDto) {
        UUID newRegionId = UUID.randomUUID();
        return this.create(newRegionId, editorDto);
    }

    public RegionResponseDto getById(UUID id) {
        return RegionResponseDto.fromRegion(
            regionRepository.findById(id)
                .orElseThrow(RuntimeException::new)
        );
    }

    public List<RegionResponseDto> getAll() {
        return regionRepository.findAll().stream()
            .map(RegionResponseDto::fromRegion).toList();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public RegionResponseDto update(UUID id, RegionEditorDto editorDto) throws Exception {
        Region region = regionRepository.findById(id)
            .orElseThrow(Exception::new);

        BeanUtils.copyProperties(editorDto, region);

        regionRepository.save(region);

        return RegionResponseDto.fromRegion(region);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(UUID id) {
        regionRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return regionRepository.existsById(id);
    }
}

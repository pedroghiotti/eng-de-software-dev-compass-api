package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.response.ReportResponseDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.TechnologyResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    public ReportResponseDto getByRegionName(String regionName) {
        Region targetRegion = regionRepository.findByName(regionName).orElseThrow(RuntimeException::new);

        List<Technology> topTechnologies = technologyRepository.findTopTechnologiesByRegion(targetRegion.getId(), PageRequest.ofSize(3));

        return new ReportResponseDto(
            topTechnologies.stream().map(TechnologyResponseDto::fromTechnology).toList()
        );
    }
}

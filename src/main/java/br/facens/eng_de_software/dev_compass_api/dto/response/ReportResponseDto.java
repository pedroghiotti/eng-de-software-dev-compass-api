package br.facens.eng_de_software.dev_compass_api.dto.response;

import java.util.List;

public record ReportResponseDto(List<TechnologyResponseDto> topTechnologies) {
}

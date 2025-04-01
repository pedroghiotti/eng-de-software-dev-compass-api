package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.ReportResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/{regionName}")
    public ResponseEntity<ReportResponseDto> getByRegionName(@PathVariable String regionName) {
        return ResponseEntity.ok(reportService.getByRegionName(regionName));
    }
}

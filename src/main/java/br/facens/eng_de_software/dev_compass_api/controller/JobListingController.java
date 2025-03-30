package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.JobListingEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.JobListingResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("job-listings")
public class JobListingController {
    @Autowired
    JobListingService jobListingService;

    @PostMapping
    public ResponseEntity<JobListingResponseDto> create(@RequestBody JobListingEditorDto editorDto) throws Exception {
        JobListingResponseDto responseDto = jobListingService.create(editorDto);
        return ResponseEntity.created(URI.create("/job-listings" + responseDto.id())).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobListingResponseDto> getById(@PathVariable UUID id) throws Exception {
        JobListingResponseDto responseDto = jobListingService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<JobListingResponseDto>> getAll() {
        List<JobListingResponseDto> responseDtos = jobListingService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobListingResponseDto> update(@PathVariable UUID id, @RequestBody JobListingEditorDto editorDto) throws Exception {
        boolean jobListingExists = jobListingService.existsById(id);

        if(jobListingExists) {
            JobListingResponseDto responseDto = jobListingService.update(id, editorDto);
            return ResponseEntity.ok(responseDto);
        } else {
            JobListingResponseDto responseDto = jobListingService.create(id, editorDto);
            return ResponseEntity.created(URI.create("/job-listings/" + id)).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        jobListingService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

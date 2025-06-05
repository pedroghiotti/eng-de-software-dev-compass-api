package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.request.JobListingEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.JobListingResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.JobListingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/job-listings")
public class JobListingController {
    private final JobListingService jobListingService;

    public JobListingController(JobListingService jobListingService) {
        this.jobListingService = jobListingService;
    }

    @PostMapping
    public ResponseEntity<JobListingResponseDto> create(@RequestBody JobListingEditorDto editorDto) throws Exception {
        JobListingResponseDto responseDto = jobListingService.create(editorDto);
        return ResponseEntity.created(URI.create("/job-listings/" + responseDto.id())).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobListingResponseDto> getById(@PathVariable UUID id) throws Exception {
        JobListingResponseDto responseDto = jobListingService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<JobListingResponseDto>> getAll(@RequestParam(required = false) String region) {
        List<JobListingResponseDto> responseDtos = jobListingService.getAll(region);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobListingResponseDto> update(@PathVariable UUID id,
            @RequestBody JobListingEditorDto editorDto) throws Exception {
        boolean jobListingExists = jobListingService.existsById(id);

        if (jobListingExists) {
            JobListingResponseDto responseDto = jobListingService.update(id, editorDto);
            return ResponseEntity.ok(responseDto);
        } else {
            JobListingResponseDto responseDto = jobListingService.create(id, editorDto);
            return ResponseEntity.created(URI.create("/job-listings/" + id)).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {
        jobListingService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Void> unpublish(@PathVariable UUID id) throws Exception {
        jobListingService.unpublish(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable UUID id) throws Exception {
        jobListingService.publish(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/start-selection")
    public ResponseEntity<Void> startSelection(@PathVariable UUID id) throws Exception {
        jobListingService.startSelection(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable UUID id) throws Exception {
        jobListingService.close(id);
        return ResponseEntity.ok().build();
    }
}

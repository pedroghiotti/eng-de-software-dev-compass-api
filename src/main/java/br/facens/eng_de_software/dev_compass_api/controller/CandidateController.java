package br.facens.eng_de_software.dev_compass_api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.naming.OperationNotSupportedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.facens.eng_de_software.dev_compass_api.dto.request.CandidateCreateDto;
import br.facens.eng_de_software.dev_compass_api.dto.request.CandidateEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.CandidateResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.CandidateService;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService baseUserService) {
        this.candidateService = baseUserService;
    }

    @PostMapping
    public ResponseEntity<CandidateResponseDto> create(@RequestBody CandidateCreateDto editorDto) throws Exception {
        CandidateResponseDto responseDto = candidateService.create(editorDto);
        return ResponseEntity.created(URI.create("/users/" + responseDto.getId())).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> getById(@PathVariable UUID id) throws Exception {
        CandidateResponseDto responseDto = candidateService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponseDto>> getAll() {
        List<CandidateResponseDto> responseDtos = candidateService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDto> update(@PathVariable UUID id, @RequestBody CandidateEditorDto editorDto)
            throws Exception {
        if (!candidateService.existsById(id))
            throw new OperationNotSupportedException("Users can not be created through the PUT verb.");

        CandidateResponseDto responseDto = candidateService.update(id, editorDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {
        candidateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

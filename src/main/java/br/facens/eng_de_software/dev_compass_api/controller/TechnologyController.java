package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.TechnologyEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.TechnologyResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("technologies")
public class TechnologyController {
    @Autowired
    private TechnologyService technologyService;

    @PostMapping
    public ResponseEntity<TechnologyResponseDto> create(@RequestBody TechnologyEditorDto editorDto) {
        TechnologyResponseDto responseDto = technologyService.create(editorDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponseDto> getById(@PathVariable UUID id) {
        TechnologyResponseDto responseDto = technologyService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<TechnologyResponseDto>> getAll() {
        List<TechnologyResponseDto> responseDtos = technologyService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyResponseDto> update(@PathVariable UUID id, @RequestBody TechnologyEditorDto editorDto) throws Exception {
        boolean regionExists = technologyService.existsById(id);

        if (regionExists) {
            TechnologyResponseDto responseDto = technologyService.update(id, editorDto);
            return ResponseEntity.ok(responseDto);
        } else {
            TechnologyResponseDto responseDto = technologyService.create(id, editorDto);
            return ResponseEntity.created(URI.create("/regions/" + id)).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        technologyService.delete(id);
        return ResponseEntity.ok().build();
    }
}

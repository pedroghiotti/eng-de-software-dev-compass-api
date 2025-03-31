package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.RegionEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.RegionResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/regions")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping
    public ResponseEntity<RegionResponseDto> create(@RequestBody RegionEditorDto editorDto) {
        RegionResponseDto responseDto = regionService.create(editorDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponseDto> getById(@PathVariable UUID id) {
        RegionResponseDto responseDto = regionService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<RegionResponseDto>> getAll() {
        List<RegionResponseDto> responseDtos = regionService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponseDto> update(@PathVariable UUID id, @RequestBody RegionEditorDto editorDto) throws Exception {
        boolean regionExists = regionService.existsById(id);

        if (regionExists) {
            RegionResponseDto responseDto = regionService.update(id, editorDto);
            return ResponseEntity.ok(responseDto);
        } else {
            RegionResponseDto responseDto = regionService.create(id, editorDto);
            return ResponseEntity.created(URI.create("/regions/" + id)).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        regionService.delete(id);
        return ResponseEntity.ok().build();
    }
}

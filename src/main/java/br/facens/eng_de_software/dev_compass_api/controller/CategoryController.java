package br.facens.eng_de_software.dev_compass_api.controller;

import br.facens.eng_de_software.dev_compass_api.dto.request.CategoryEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.CategoryResponseDto;
import br.facens.eng_de_software.dev_compass_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryEditorDto editorDto) {
        CategoryResponseDto responseDto = categoryService.create(editorDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable UUID id) {
        CategoryResponseDto responseDto = categoryService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        List<CategoryResponseDto> responseDtos = categoryService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable UUID id, @RequestBody CategoryEditorDto editorDto) throws Exception {
        boolean categoryExists = categoryService.existsById(id);

        if (categoryExists) {
            CategoryResponseDto responseDto = categoryService.update(id, editorDto);
            return ResponseEntity.ok(responseDto);
        } else {
            CategoryResponseDto responseDto = categoryService.create(id, editorDto);
            return ResponseEntity.created(URI.create("/categories/" + id)).body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}

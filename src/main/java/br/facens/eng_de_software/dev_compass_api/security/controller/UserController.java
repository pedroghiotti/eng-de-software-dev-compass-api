package br.facens.eng_de_software.dev_compass_api.security.controller;

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

import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserCreateDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserEditorDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserResponseDto;
import br.facens.eng_de_software.dev_compass_api.security.service.BaseUserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final BaseUserService baseUserService;

    public UserController(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    @PostMapping
    public ResponseEntity<BaseUserResponseDto> create(@RequestBody BaseUserCreateDto editorDto) throws Exception {
        BaseUserResponseDto responseDto = baseUserService.create(editorDto);
        return ResponseEntity.created(URI.create("/users/" + responseDto.id())).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> getById(@PathVariable UUID id) throws Exception {
        BaseUserResponseDto responseDto = baseUserService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<BaseUserResponseDto>> getAll() {
        List<BaseUserResponseDto> responseDtos = baseUserService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseUserResponseDto> update(@PathVariable UUID id, @RequestBody BaseUserEditorDto editorDto)
            throws Exception {
        if (!baseUserService.existsById(id))
            throw new OperationNotSupportedException("Users can not be created through the PUT verb.");

        BaseUserResponseDto responseDto = baseUserService.update(id, editorDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {
        baseUserService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

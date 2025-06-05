package br.facens.eng_de_software.dev_compass_api.service;

import br.facens.eng_de_software.dev_compass_api.dto.request.CategoryEditorDto;
import br.facens.eng_de_software.dev_compass_api.dto.response.CategoryResponseDto;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository technologyRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto create(UUID id, CategoryEditorDto editorDto) {
        Category newCategory = new Category(id, editorDto.name());
        technologyRepository.save(newCategory);
        return CategoryResponseDto.fromCategory(newCategory);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto create(CategoryEditorDto editorDto) {
        UUID newCategoryId = UUID.randomUUID();
        return this.create(newCategoryId, editorDto);
    }

    public CategoryResponseDto getById(UUID id) {
        return CategoryResponseDto.fromCategory(
                technologyRepository.findById(id)
                        .orElseThrow(RuntimeException::new)
        );
    }

    public List<CategoryResponseDto> getAll() {
        return technologyRepository.findAll().stream()
                .map(CategoryResponseDto::fromCategory).toList();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto update(UUID id, CategoryEditorDto editorDto) throws Exception {
        Category category = technologyRepository.findById(id)
                .orElseThrow(Exception::new);

        BeanUtils.copyProperties(editorDto, category);

        technologyRepository.save(category);

        return CategoryResponseDto.fromCategory(category);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(UUID id) {
        technologyRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return technologyRepository.existsById(id);
    }
}

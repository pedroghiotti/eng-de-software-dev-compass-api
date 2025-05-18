package br.facens.eng_de_software.dev_compass_api.security.service;

import java.util.List;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserCreateDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserEditorDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserResponseDto;
import br.facens.eng_de_software.dev_compass_api.security.model.Admin;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BaseUserService {
    private final AuthenticationService authenticationService;
    private final BaseUserRepository baseUserRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseUserService(
            BaseUserRepository baseUserRepository,
            AuthenticationService authenticationService,
            PasswordEncoder passwordEncoder) {
        this.baseUserRepository = baseUserRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public BaseUserResponseDto create(BaseUserCreateDto createDto) throws Exception {
        String encodedPassword = passwordEncoder.encode(createDto.password());
        BaseUser newUser;

        switch (createDto.role()) {
            case BUSINESS -> newUser = new Business(createDto.username(), encodedPassword);
            case CANDIDATE -> newUser = new Candidate(createDto.username(), encodedPassword);
            case ADMIN -> {
                BaseUser currentUser = authenticationService.getCurrentUser()
                        .orElseThrow(
                                () -> new AuthenticationException("User not authenticated. Operation disallowed."));
                if (!currentUser.getRole().equals(Role.ADMIN))
                    throw new AuthorizationDeniedException("User not authorized. Operation disallowed.");
                newUser = new Admin(createDto.username(), encodedPassword);
            }
            default -> throw new IllegalArgumentException("Invalid User type.");
        }

        baseUserRepository.save(newUser);
        return BaseUserResponseDto.fromBaseUser(newUser);
    }

    public BaseUserResponseDto getById(UUID id) throws Exception {
        return BaseUserResponseDto.fromBaseUser(_getById(id));
    }

    public List<BaseUserResponseDto> getAll() {
        return baseUserRepository.findAll().stream()
                .map(BaseUserResponseDto::fromBaseUser).toList();
    }

    public BaseUserResponseDto update(UUID id, BaseUserEditorDto editorDto) throws Exception {
        BaseUser currentUser = _getById(id);
        verifyOwnership(currentUser);

        BaseUser user = _getById(id);
        user.setUsername(editorDto.username());
        user.setPassword(passwordEncoder.encode(editorDto.password()));

        baseUserRepository.save(user);
        return BaseUserResponseDto.fromBaseUser(user);
    }

    public void deleteById(UUID id) throws Exception {
        BaseUser currentUser = _getById(id);
        verifyOwnership(currentUser);

        baseUserRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return baseUserRepository.existsById(id);
    }

    private BaseUser _getById(UUID id) {
        return baseUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User listing not found with id %s", id)));
    }

    private void verifyOwnership(BaseUser user) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new AuthenticationException("User not authenticated. Operation disallowed."));
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(user.getId()))
            throw new AuthorizationDeniedException("User not authorized. Operation disallowed.");
    }
}

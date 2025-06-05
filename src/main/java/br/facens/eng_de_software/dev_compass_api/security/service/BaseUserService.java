package br.facens.eng_de_software.dev_compass_api.security.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserCreateDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserEditorDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserResponseDto;
import br.facens.eng_de_software.dev_compass_api.security.model.Admin;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import br.facens.eng_de_software.dev_compass_api.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BaseUserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseUserService(
            UserRepository baseUserRepository,
            AuthenticationService authenticationService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = baseUserRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public BaseUserResponseDto create(BaseUserCreateDto createDto) throws Exception {
        String encodedPassword = passwordEncoder.encode(createDto.getPassword());
        BaseUser newUser;

        switch (createDto.getRole()) {
            case BUSINESS -> newUser = new Business(createDto.getUsername(), encodedPassword);
            case ADMIN -> {
                BaseUser currentUser = authenticationService.getCurrentUser();
                if (!currentUser.getRole().equals(Role.ADMIN))
                    throw new AuthorizationDeniedException("User not authorized. Operation disallowed.");
                newUser = new Admin(createDto.getUsername(), encodedPassword);
            }
            default -> throw new IllegalArgumentException("Invalid User type.");
        }

        userRepository.save(newUser);
        return BaseUserResponseDto.fromUser(newUser);
    }

    public BaseUserResponseDto getById(UUID id) throws Exception {
        return BaseUserResponseDto.fromUser(_getById(id));
    }

    public List<BaseUserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(BaseUserResponseDto::fromUser).toList();
    }

    public BaseUserResponseDto update(UUID id, BaseUserEditorDto editorDto) throws Exception {
        BaseUser user = _getById(id);
        verifyOwnership(user);

        user.setUsername(editorDto.getUsername());
        user.setPassword(passwordEncoder.encode(editorDto.getPassword()));

        userRepository.save(user);
        return BaseUserResponseDto.fromUser(user);
    }

    public void deleteById(UUID id) throws Exception {
        BaseUser user = _getById(id);
        verifyOwnership(user);

        userRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    private BaseUser _getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User listing not found with id %s", id)));
    }

    public void verifyOwnership(BaseUser user) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(user.getId()))
            throw new AuthorizationDeniedException("User not authorized. Operation disallowed.");
    }
}

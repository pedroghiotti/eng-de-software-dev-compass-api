package br.facens.eng_de_software.dev_compass_api.security.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserCreateDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserEditDto;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserResponseDto;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;

@Service
public class BaseUserService {
    private final AuthenticationService authenticationService;
    private final BaseUserRepository baseUserRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseUserService(
        BaseUserRepository baseUserRepository,
        AuthenticationService authenticationService,
        PasswordEncoder passwordEncoder
    ) {
        this.baseUserRepository = baseUserRepository;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public BaseUserResponseDto create(BaseUserCreateDto createDto) throws Exception {
        BaseUser newUser = switch(createDto.role()) {
            case BUSINESS -> new Business(
                createDto.username(),
                passwordEncoder.encode(createDto.password())
            );
            case CANDIDATE -> new Candidate(
                createDto.username(),
                passwordEncoder.encode(createDto.password())
            );
            default -> throw new IllegalArgumentException("Tipo de usuário inválido.");
        };
        
        baseUserRepository.save(newUser);

        return BaseUserResponseDto.fromBaseUser(newUser);
    }

    public BaseUserResponseDto getById(UUID id) throws Exception {
        return BaseUserResponseDto.fromBaseUser(
            baseUserRepository.findById(id)
                .orElseThrow(RuntimeException::new)
        );
    }

    public List<BaseUserResponseDto> getAll() {
        return baseUserRepository.findAll().stream()
            .map(BaseUserResponseDto::fromBaseUser).toList();
    }

    public BaseUserResponseDto update(UUID id, BaseUserEditDto editorDto) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser().orElseThrow(Exception::new);
        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new Exception();
        }

        BaseUser user = baseUserRepository.findById(id).orElseThrow(Exception::new);

        user.setUsername(editorDto.username());
        user.setPassword(passwordEncoder.encode(editorDto.password()));

        baseUserRepository.save(user);

        return BaseUserResponseDto.fromBaseUser(user);
    }

    public void deleteById(UUID id) throws Exception {
        BaseUser currentUser = authenticationService.getCurrentUser().orElseThrow(Exception::new);
        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new Exception();
        }

        baseUserRepository.deleteById(id);
    }
    
    public boolean existsById(UUID id) {
        return baseUserRepository.existsById(id);
    }
}

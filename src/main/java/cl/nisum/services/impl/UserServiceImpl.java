package cl.nisum.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.nisum.exception.ApiException;
import cl.nisum.models.dtos.RegisterUserDto;
import cl.nisum.models.dtos.security.LoginRequest;
import cl.nisum.models.projection.UserProjection;
import cl.nisum.models.vo.UserVo;
import cl.nisum.persistence.entities.Role;
import cl.nisum.persistence.entities.User;
import cl.nisum.persistence.entities.Role.RoleEnum;
import cl.nisum.persistence.repositories.RoleRepository;
import cl.nisum.persistence.repositories.UserRepository;
import cl.nisum.services.AuthenticationService;
import cl.nisum.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    @Override
    public UserProjection saveUser(RegisterUserDto registerUserDto) throws ApiException {
        if (this.userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new ApiException("El correo ya está registrado", HttpStatus.CONFLICT);
        }
        Role role = this.roleRepository.findOneByName(RoleEnum.USER)
        .orElseThrow(() -> new ApiException("No se encontró el rol USER", HttpStatus.NOT_FOUND));

        User userEntity = null;
        UserProjection userProjection = null;
        try {
            userEntity = this.buildUserFromDto(registerUserDto, role);
            this.modelMapper.map(this.userRepository.save(userEntity), userEntity);
            log.info("Usuario creado: {}", userEntity.toString());

            userEntity.setToken(this.authenticateUser(registerUserDto));
            userProjection = this.modelMapper.map(userEntity, UserVo.class);
        } catch (Exception e) {
            log.error("error al guardar el usuario: {}",e.getMessage(), e);
            throw new ApiException("Error al insertar datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userProjection;
    }

    @Override
    public UserProjection updateUser(UUID id, RegisterUserDto registerUserDto) throws ApiException {
        UserProjection userProjection = null;
        User userEntity = this.userRepository.findById(id)
                .orElseThrow(() -> new ApiException("No se encontró el usuario con el id " + id, HttpStatus.NOT_FOUND));
        try {
            this.modelMapper.map(registerUserDto, userEntity);
            userEntity.setModified(LocalDateTime.now());
            userProjection = this.modelMapper.map(this.userRepository.save(userEntity), UserVo.class);
        } catch (Exception e) {
            log.error("Error al actualizar usuario: {}", e.getMessage(), e);
            throw new ApiException("Error al actualizar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userProjection;
    }

    private User buildUserFromDto(RegisterUserDto dto, Role role) {
        User user = modelMapper.map(dto, User.class);
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        return user;
    }

    private String authenticateUser(RegisterUserDto dto) throws ApiException {
        LoginRequest loginRequest = new LoginRequest(dto.getEmail(), dto.getPassword());
        return authenticationService.authenticate(loginRequest).jwt();
    }

}

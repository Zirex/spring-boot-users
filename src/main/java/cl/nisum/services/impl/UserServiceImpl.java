package cl.nisum.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.nisum.exception.ApiException;
import cl.nisum.models.dtos.RegisterUserDto;
import cl.nisum.models.entities.User;
import cl.nisum.models.projection.UserProjection;
import cl.nisum.models.vo.UserVo;
import cl.nisum.repositories.UserRepository;
import cl.nisum.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserProjection saveUser(RegisterUserDto registerUserDto) throws ApiException {
        User userEntity = null;
        UserProjection userProjection = null;
        try {
            userEntity = this.modelMapper.map(registerUserDto, User.class);
            userEntity.setCreated(LocalDateTime.now());
            userEntity.setLastLogin(LocalDateTime.now());
            userEntity.setActive(true);
            userEntity.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
            userProjection = this.modelMapper.map(this.userRepository.save(userEntity), UserVo.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Error al insertar datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userProjection;
    }

    @Override
    public UserProjection upadateUser(UUID id, RegisterUserDto registerUserDto) throws ApiException {
        UserProjection userProjection = null;
        User userEntity = this.userRepository.findById(id).orElseThrow(() -> new ApiException("No se encontró el usuario con el id "+id, HttpStatus.NOT_FOUND));
        try {
            this.modelMapper.map(userEntity, registerUserDto);
            userEntity.setModified(LocalDateTime.now());
            userProjection = this.modelMapper.map(this.userRepository.save(userEntity), UserVo.class);
        } catch (Exception e) {
            throw new ApiException("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userProjection;
    }
    
}

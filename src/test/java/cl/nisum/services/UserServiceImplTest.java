package cl.nisum.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.nisum.exception.ApiException;
import cl.nisum.models.dtos.RegisterUserDto;
import cl.nisum.models.dtos.security.LoginRequest;
import cl.nisum.models.dtos.security.LoginResponse;
import cl.nisum.models.projection.UserProjection;
import cl.nisum.models.vo.UserVo;
import cl.nisum.persistence.entities.Role;
import cl.nisum.persistence.entities.User;
import cl.nisum.persistence.entities.Role.RoleEnum;
import cl.nisum.persistence.repositories.RoleRepository;
import cl.nisum.persistence.repositories.UserRepository;
import cl.nisum.services.impl.UserServiceImpl;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserSuccessfully() throws ApiException {
        // Arrange
        LoginResponse loginResponse = mock(LoginResponse.class);

        UUID userId = UUID.randomUUID();

        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setPassword("password123");

        Role role = new Role();
        role.setName(RoleEnum.USER);

        User userEntity = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("encodedPassword")
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .role(role)
                .build();

        UserVo userVo = UserVo.builder()
                .id(userId)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("jwtToken")
                .active(true)
                .build();

        LoginRequest loginRequest = new LoginRequest(registerUserDto.getEmail(), registerUserDto.getPassword());

        // Mockear respuestas de las dependencias
        when(userRepository.existsByEmail(registerUserDto.getEmail())).thenReturn(false);
        when(roleRepository.findOneByName(RoleEnum.USER)).thenReturn(Optional.of(role));
        when(modelMapper.map(any(RegisterUserDto.class), eq(User.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(loginResponse.jwt()).thenReturn("jwtToken");
        when(authenticationService.authenticate(loginRequest)).thenReturn(loginResponse);
        when(modelMapper.map(any(User.class), eq(UserVo.class))).thenReturn(userVo);

        // Act
        UserProjection result = userService.saveUser(registerUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(userVo.getToken(), result.getToken());
        verify(userRepository).save(userEntity);
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyExists() {
        // Arrange
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("existing@example.com");

        when(userRepository.existsByEmail(registerUserDto.getEmail())).thenReturn(true);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> userService.saveUser(registerUserDto));
        assertEquals("El correo ya está registrado", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionIfRoleNotFound() {
        // Arrange
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@example.com");

        when(userRepository.existsByEmail(registerUserDto.getEmail())).thenReturn(false);
        when(roleRepository.findOneByName(RoleEnum.USER)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> userService.saveUser(registerUserDto));
        assertEquals("No se encontró el rol USER", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws ApiException {
        // Arrange
        UUID userId = UUID.randomUUID();
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("updated@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("existing@example.com");

        UserVo userVo = new UserVo();

        // Mockear respuestas de las dependencias
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(modelMapper.map(any(User.class), eq(UserVo.class))).thenReturn(userVo);

        // Act
        UserProjection result = userService.updateUser(userId, registerUserDto);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        RegisterUserDto registerUserDto = new RegisterUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class,
                () -> userService.updateUser(userId, registerUserDto));
        assertEquals("No se encontró el usuario con el id " + userId, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}

package cl.nisum.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.nisum.exception.ApiException;
import cl.nisum.models.dtos.RegisterUserDto;
import cl.nisum.models.projection.UserProjection;
import cl.nisum.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class UserRest {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserProjection> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) throws ApiException {
        log.info("saving new user");
        var userProjection = userService.saveUser(registerUserDto);
        return new ResponseEntity<>(userProjection, HttpStatus.CREATED);
    }
    
    
}

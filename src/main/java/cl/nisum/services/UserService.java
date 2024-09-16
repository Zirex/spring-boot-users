package cl.nisum.services;

import java.util.UUID;

import cl.nisum.exception.ApiException;
import cl.nisum.models.dtos.RegisterUserDto;
import cl.nisum.models.projection.UserProjection;

public interface UserService {
    UserProjection saveUser(RegisterUserDto user) throws ApiException;
    UserProjection upadateUser(UUID id, RegisterUserDto user) throws ApiException;
}

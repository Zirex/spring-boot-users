package cl.nisum.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nisum.exception.ApiException;
import cl.nisum.models.entities.Role;
import cl.nisum.models.entities.Role.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findOneByName(RoleEnum name) throws ApiException;
}

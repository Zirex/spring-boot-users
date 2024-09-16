package cl.nisum.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nisum.persistence.entities.Role;
import cl.nisum.persistence.entities.Role.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findOneByName(RoleEnum name);
}

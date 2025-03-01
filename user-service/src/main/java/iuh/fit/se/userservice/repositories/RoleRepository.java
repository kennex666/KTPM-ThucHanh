package iuh.fit.se.userservice.repositories;

import iuh.fit.se.userservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByCode(String code);
}

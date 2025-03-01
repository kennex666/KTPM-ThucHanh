package iuh.fit.se.userservice.repositories;

import iuh.fit.se.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}

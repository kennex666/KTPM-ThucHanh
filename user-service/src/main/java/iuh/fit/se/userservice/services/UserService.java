package iuh.fit.se.userservice.services;

import iuh.fit.se.userservice.entities.User;

public interface UserService {
    User findByUserName(String userName);
    void saveUser(User user);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}

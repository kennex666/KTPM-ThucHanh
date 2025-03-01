package iuh.fit.se.userservice.services.impl;

import iuh.fit.se.userservice.entities.User;
import iuh.fit.se.userservice.repositories.UserRepository;
import iuh.fit.se.userservice.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userName: " + userName));
    }

    @Transactional
    @Modifying
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }
}

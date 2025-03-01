package iuh.fit.se.userservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.userservice.auths.UserPrincipal;
import iuh.fit.se.userservice.entities.User;
import iuh.fit.se.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + username));
        Collection<GrantedAuthority> authorities = new ArrayList();
        if(null != user.getRoles()) {
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getCode()));
                role.getPermissions().forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                });
            });
        }
        return new UserPrincipal(user.getId(), user.getUserName(),
                user.getEmail(), user.getPassword(), authorities, user.isEnabled()
        );
    }
}

package iuh.fit.se.userservice.services;

import iuh.fit.se.userservice.entities.Token;
import org.springframework.security.core.Authentication;

public interface TokenService {
    void saveToken(Token token);
    Token findByToken(String token);
}

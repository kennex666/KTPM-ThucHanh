package iuh.fit.se.userservice.services.impl;

import iuh.fit.se.userservice.entities.Token;
import iuh.fit.se.userservice.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandlerServiceImpl implements LogoutHandler {

    private final TokenService tokenService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwtToken = authHeader.substring(7);
        Token token = tokenService.findByToken(jwtToken);
        if (null != token) {
            token.setRevoked(true);
            tokenService.saveToken(token);
        }
    }
}

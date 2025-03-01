package iuh.fit.se.userservice.utils;

import iuh.fit.se.userservice.auths.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    @SuppressWarnings("ReassignedVariable")
    public String generateToken(Authentication authentication, JwtEncoder jwtEncoder) {
        String token = "";
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        try {
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("iuh.fit.se")
                    .issuedAt(now)
                    .expiresAt(generateExpirationDate())
                    .subject(userPrincipal.getUsername())
                    .claim("scope", userPrincipal.getAuthorities()
                            .stream().map(r -> r.getAuthority()).collect(Collectors.toList()))
                    .build();

            token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }
    public String getUsernameFromToken(Jwt jwtToken) {
        return jwtToken.getSubject();
    }
    private boolean isTokenExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }
    public boolean isTokenValid(Jwt jwtToken, UserPrincipal userPrincipal) {
        return !isTokenExpired(jwtToken) &&
                userPrincipal.isEnabled() &&
                userPrincipal.getUsername().equals(getUsernameFromToken(jwtToken));
    }
    public Instant generateExpirationDate() {
        return Instant.now().plus(10, ChronoUnit.MINUTES);
    }
}

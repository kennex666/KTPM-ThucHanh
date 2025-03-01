package iuh.fit.se.userservice.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class SignInResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
}

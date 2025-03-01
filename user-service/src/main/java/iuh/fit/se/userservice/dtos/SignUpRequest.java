package iuh.fit.se.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Username is required!")
    @Size(min= 5, message = "Username must have at least 5 characters!")
    @Size(max= 20, message = "Username can have have at most 20 characters!")
    private String userName;

    @Email(message = "Email is not in valid format!")
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must have at least 8 characters!")
    @Size(max = 20, message = "Password can have have at most 20 characters!")
    private String password;

    private Set<String> roles;
}

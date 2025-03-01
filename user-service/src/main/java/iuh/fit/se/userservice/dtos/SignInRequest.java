package iuh.fit.se.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Username is required!")
    private String userName;

    @NotBlank(message = "Password is required!")
    private String password;
}

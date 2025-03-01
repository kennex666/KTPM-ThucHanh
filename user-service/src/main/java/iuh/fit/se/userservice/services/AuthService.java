package iuh.fit.se.userservice.services;

import iuh.fit.se.userservice.dtos.ApiResponse;
import iuh.fit.se.userservice.dtos.SignInRequest;
import iuh.fit.se.userservice.dtos.SignUpRequest;
import iuh.fit.se.userservice.exceptions.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> signUp(SignUpRequest signUpRequest)
            throws UserAlreadyExistsException;
    ResponseEntity<ApiResponse<?>> signIn(SignInRequest signInRequest);
}

package iuh.fit.se.userservice.exceptions;

import iuh.fit.se.userservice.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });
        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponse.builder()
                                .status("FAILED")
                                .message("Registration Failed: Please provide valid data.")
                                .response(errorMessage)
                                .build()
                );
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.builder()
                                .status("FAILED")
                                .message(exception.getMessage())
                                .build()
                );
    }
}

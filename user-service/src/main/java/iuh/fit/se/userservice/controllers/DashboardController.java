package iuh.fit.se.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class DashboardController {
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication) {
        return ResponseEntity.ok(
                "Welcome to user service: " + authentication.getName() +
                        " with scope: " + authentication.getAuthorities());
    }

    @PreAuthorize("hasAnyRole('SCOPE_ROLE_ADMIN','SCOPE_ROLE_SUPER_ADMIN') " +
            " or hasAuthority('SCOPE_PERMISSION_UPDATE')")
    @GetMapping("/admin-message")
    public ResponseEntity<String> getAdminData(Authentication authentication, Principal principal) {
        return ResponseEntity.ok("Welcome to Admin Role: " + authentication.getAuthorities());
    }
}

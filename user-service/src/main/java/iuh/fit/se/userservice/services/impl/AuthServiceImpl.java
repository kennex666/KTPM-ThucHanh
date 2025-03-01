package iuh.fit.se.userservice.services.impl;

import iuh.fit.se.userservice.auths.UserPrincipal;
import iuh.fit.se.userservice.dtos.ApiResponse;
import iuh.fit.se.userservice.dtos.SignInRequest;
import iuh.fit.se.userservice.dtos.SignInResponse;
import iuh.fit.se.userservice.dtos.SignUpRequest;
import iuh.fit.se.userservice.entities.Role;
import iuh.fit.se.userservice.entities.Token;
import iuh.fit.se.userservice.entities.User;
import iuh.fit.se.userservice.exceptions.UserAlreadyExistsException;
import iuh.fit.se.userservice.services.AuthService;
import iuh.fit.se.userservice.services.RoleService;
import iuh.fit.se.userservice.services.TokenService;
import iuh.fit.se.userservice.services.UserService;
import iuh.fit.se.userservice.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;
    private RoleService roleService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;

    @Autowired
    public AuthServiceImpl(UserService userService,
                           RoleService roleService,
                           TokenService tokenService,
                           JwtTokenUtil jwtTokenUtil,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> signUp(SignUpRequest signUpRequest)
            throws UserAlreadyExistsException {
        if (userService.existsByUserName(signUpRequest.getUserName())) {
            throw new UserAlreadyExistsException("Username already exist");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exist");
        }

        User user = createUser(signUpRequest);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .status(String.valueOf("SUCCESS"))
                        .message("User account has been successfully created!")
                        .build()
        );
    }

    private User createUser(SignUpRequest signUpRequest)  {

        return User.builder()
                .email(signUpRequest.getEmail())
                .userName(signUpRequest.getUserName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .enabled(true)
                .roles(determineRoles(signUpRequest.getRoles()))
                .build();
    }

    private Set<Role> determineRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(roleService.getRoleByCode("ROLE_USER"));
        } else {
            for (String role : strRoles) {
                roles.add(roleService.getRoleByCode(role));
            }
        }
        return roles;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUserName(),
                        signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication, jwtEncoder);
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        User user = new User();
        user.setId(userDetails.getId());
        user.setUserName(userDetails.getUsername());

        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .expiryDate(jwtTokenUtil.generateExpirationDate())
                .revoked(false)
                .build();
        tokenService.saveToken(token);

        SignInResponse signInResponse = SignInResponse.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .token(jwt)
                .type("Bearer")
                .roles(userDetails.getAuthorities())
                .build();

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("SUCCESS")
                        .message("Sign in successfull!")
                        .response(signInResponse)
                        .build()
        );
    }
}

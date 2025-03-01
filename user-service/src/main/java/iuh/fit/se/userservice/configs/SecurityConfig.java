package iuh.fit.se.userservice.configs;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import iuh.fit.se.userservice.filters.JwtAccessTokenFilter;
import iuh.fit.se.userservice.services.TokenService;
import iuh.fit.se.userservice.services.impl.LogoutHandlerServiceImpl;
import iuh.fit.se.userservice.services.impl.UserDetailsServiceImpl;
import iuh.fit.se.userservice.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtil jwtTokenUil;
    private final LogoutHandlerServiceImpl logoutHandlerService;
    private final TokenService tokenService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(RSAKeyRecord rsaKeyRecord, UserDetailsServiceImpl userDetailsService, JwtTokenUtil jwtTokenUil, LogoutHandlerServiceImpl logoutHandlerService, TokenService tokenService, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.rsaKeyRecord = rsaKeyRecord;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUil = jwtTokenUil;
        this.logoutHandlerService = logoutHandlerService;
        this.tokenService = tokenService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).
                privateKey(rsaKeyRecord.rsaPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Order(1)
    @Bean
    public SecurityFilterChain signUpSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher((new AntPathRequestMatcher("/sign-up")))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher((new AntPathRequestMatcher("/sign-in")))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                })
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Order(3)
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher((new AntPathRequestMatcher("/api/**")))
                // 1: Disable Cross-Site Request Forgery (CSRF)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        // 2: The user should be authenticated for any request in the application.
                        authorize -> authorize.anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(
                        // 3: Spring Security will never create an HttpSession and it will never use it to obtain the Security Context.
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAccessTokenFilter(this.jwtDecoder(), this.jwtTokenUil,this.userDetailsService,this.tokenService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        (ex) -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(this.customAccessDeniedHandler))
                // 4: Spring Security’s HTTP Basic Authentication support is enabled by default. However, as soon as any servlet-based configuration is provided, HTTP Basic must be explicitly provided.
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Order(4)
    @Bean
    SecurityFilterChain logoutFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher((new AntPathRequestMatcher("/logout")))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // (3)
                .addFilterBefore(new JwtAccessTokenFilter(this.jwtDecoder(), this.jwtTokenUil,this.userDetailsService,this.tokenService), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();
                        })
                )
                .exceptionHandling(
                        (ex) -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }
}

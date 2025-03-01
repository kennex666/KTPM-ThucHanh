package iuh.fit.se.userservice.filters;

import iuh.fit.se.userservice.auths.UserPrincipal;
import iuh.fit.se.userservice.entities.Token;
import iuh.fit.se.userservice.services.TokenService;
import iuh.fit.se.userservice.services.impl.UserDetailsServiceImpl;
import iuh.fit.se.userservice.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private JwtDecoder jwtDecoder;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsServiceImpl userDetailsService;
    private TokenService tokenService;

    public JwtAccessTokenFilter(JwtDecoder jwtDecoder, JwtTokenUtil jwtTokenUtil,
                                UserDetailsServiceImpl userDetailsService, TokenService tokenService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (request.getRequestURI().equals("/sign-in") ||
                request.getRequestURI().equals("/sign-up")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (null == authHeader || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Please provide a token.");
            return;
        }

        String token = authHeader.substring(7);
        Token tokenEntity = tokenService.findByToken(token);

        try {
            if (null != tokenEntity && tokenEntity.revoked) {
                SecurityContextHolder.clearContext();
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The access token you provided is revoked malformed or invalid.");
                return;
            }

            Jwt jwtToken = this.jwtDecoder.decode(token);
            String userName = jwtToken.getSubject();

            if(!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(userName);

                if(jwtTokenUtil.isTokenValid(jwtToken, userPrincipal)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(userPrincipal.getUsername(), userPrincipal.getPassword(), userPrincipal.getAuthorities());
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (JwtException ex) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }
}

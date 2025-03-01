package iuh.fit.se.userservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.userservice.dtos.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, exc.getMessage());

        ApiResponse apiErrorDto = new ApiResponse(
                "FAILED",
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                exc.getMessage()

        );

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiErrorDto));

    }
}

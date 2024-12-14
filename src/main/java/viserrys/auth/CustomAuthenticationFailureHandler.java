package viserrys.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) 
            throws IOException {
        var redirectUrl = switch (exception) {
            case BadCredentialsException e -> "/login?error=bad_credentials";
            case LockedException e -> "/login?error=locked";
            default -> "/login?error=unknown";
        };
        response.sendRedirect(redirectUrl);
    }
}

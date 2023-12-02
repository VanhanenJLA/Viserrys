package viserrys.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    public String getAuthenticationPrincipalName() {

        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
                
        if (authentication instanceof AnonymousAuthenticationToken)
            return null;

        return authentication.getName();
    }
}

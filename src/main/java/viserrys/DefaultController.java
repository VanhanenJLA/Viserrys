package viserrys;

import org.springframework.stereotype.Controller;
import viserrys.auth.AuthService;

@Controller
public class DefaultController {

    final AuthService authService;
    public DefaultController(AuthService authService) {
        this.authService = authService;
    }

}




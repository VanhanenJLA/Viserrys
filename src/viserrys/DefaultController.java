package viserrys;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import viserrys.auth.AuthService;

@Controller
public class DefaultController {

    final AuthService authService;

    public DefaultController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("*")
    public String home(Model model) {
        if (authService.getAuthenticatedAccount() == null)
            return "redirect:/login";
        return "redirect:/me";
    }

}

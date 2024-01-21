package viserrys;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import viserrys.auth.AuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class DefaultController {

    final AuthService authService;
    public DefaultController(AuthService authService) {
        this.authService = authService;
    }

//    @GetMapping("*")
//    public String home() {
//        var currentAccount = authService.getAuthenticatedAccount();
//        if (currentAccount == null)
//            return "redirect:/login";
//        return "redirect:/me";
//    }

}




package viserrys.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import viserrys.account.Account;
import viserrys.account.AccountService;

import javax.validation.Valid;

@Controller
public class AuthController {

    final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/register")
    public String register(Model model, @ModelAttribute Account account) {
        return "pages/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute Account account, @Valid BindingResult bindingResult) {

        try {
            accountService.createAccount(account.getUsername(), account.getPassword());
        } catch (Exception e) {
            
            model.addAttribute("error", e.getMessage());
            return "redirect:/register";
        }

        return "redirect:/login";
    }

}
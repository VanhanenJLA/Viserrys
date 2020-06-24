package viserrys.Auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import viserrys.Account.Account;
import viserrys.Account.AccountService;

@Controller
public class AuthController {

  @Autowired
  private AccountService accountService;

  @GetMapping("/login")
  public String login(@ModelAttribute Account account) {
    return "login";
  }

  @GetMapping("/register")
  public String register(@ModelAttribute Account account) {
    return "register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute Account account, @Valid BindingResult bindingResult,
      @RequestParam String username, @RequestParam String password, @RequestParam String profileHandle)
      throws Exception {

    if (bindingResult.hasErrors())
      return "redirect:/register";

    return "redirect:/login";
  }

}
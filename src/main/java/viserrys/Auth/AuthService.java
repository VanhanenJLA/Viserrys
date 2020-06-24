package viserrys.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;
import viserrys.Account.AccountService;

@Service
public class AuthService {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AccountService accountService;

  public Account getAuthenticatedAccount() {
    return accountService.getAccount(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  public String login(String username, String password) throws Exception {

    Account account = accountService.getAccount(username);

    if (account == null)
      throw new Exception("No account matches the given username!");

    if (!passwordEncoder.matches(password, account.getPassword()))
      throw new Exception("Username and password do not match!");

    return "redirect:/me";
  }

}

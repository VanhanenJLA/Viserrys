package viserrys.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import viserrys.Account.Account;
import viserrys.Account.AccountService;

@Service
public class AuthService {

  @Autowired
  AccountService accountService;

  public Account getAuthenticatedAccount() {
    return accountService.getAccount(SecurityContextHolder.getContext().getAuthentication().getName());
  }

}

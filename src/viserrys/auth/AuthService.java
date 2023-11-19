package viserrys.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import viserrys.account.Account;
import viserrys.account.AccountService;

@Service
public class AuthService {

  final AccountService accountService;

  public AuthService(AccountService accountService) {
    this.accountService = accountService;
  }

  public Account getAuthenticatedAccount() {
    return accountService.getAccount(SecurityContextHolder.getContext().getAuthentication().getName());
  }

}

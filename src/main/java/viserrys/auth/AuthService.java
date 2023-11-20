package viserrys.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import viserrys.account.Account;
import viserrys.account.AccountService;

import java.util.Optional;

@Service
public class AuthService {

    final AccountService accountService;

    public AuthService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account getAuthenticatedAccount() {
        var principalName = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return accountService.getAccount(principalName);
    }

}

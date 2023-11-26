package viserrys.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken)
            return null;

        return accountService.getAccount(authentication.getName());
    }

}

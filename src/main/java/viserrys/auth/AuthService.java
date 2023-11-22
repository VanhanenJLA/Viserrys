package viserrys.auth;

import ch.qos.logback.core.joran.conditional.IfAction;
import org.apache.commons.logging.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import viserrys.account.Account;
import viserrys.account.AccountService;

import java.util.Optional;
import java.util.logging.Logger;

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
        
        if (authentication.isAuthenticated())
            return accountService.getAccount(authentication.getName());
        return null;
    }

}

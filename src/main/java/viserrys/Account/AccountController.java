package viserrys.Account;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import viserrys.Auth.AuthService;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    private Account current() {
        return authService.getAuthenticatedAccount();
    }

    @GetMapping("/me")
    private String me(Model model) {
        return account(model, current().getProfileHandle());
    }

    @GetMapping("/my-photos")
    private String myPhotos(Model model) {
        return photos(model, current().getProfileHandle());
    }

    @GetMapping("/accounts/{profileHandle}")
    private String account(Model model, @PathVariable String profileHandle) {

        Account a = accountService.accountRepository.findByProfileHandle(profileHandle);
        model.addAttribute("account", a);
        model.addAttribute("currentAccount", current());
        return "account";
    }

    @GetMapping("/accounts")
    private String accounts(Model model) {
        List<Account> accounts = accountService.getAccounts();
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", current());
        return "accounts";
    }

    @PostMapping("/accounts/{profileHandle}/follow")
    private String follow(Model model, @PathVariable String profileHandle) throws Exception {
        Account sender = current();
        Account recipient = accountService.accountRepository.findByProfileHandle(profileHandle);

        accountService.follow(sender, recipient);

        model.addAttribute("currentAccount", sender);
        model.addAttribute("account", recipient);
        return "redirect:/accounts/{profileHandle}";
    }

    @GetMapping("/accounts/{profileHandle}/followers")
    private String followers(Model model, @PathVariable String profileHandle) {
        Account a = accountService.accountRepository.findByProfileHandle(profileHandle);
        List<Account> accounts = a.getFollowers();
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", current());
        return "follows";
    }

    @GetMapping("/accounts/{profileHandle}/following")
    private String following(Model model, @PathVariable String profileHandle) {
        Account a = accountService.accountRepository.findByProfileHandle(profileHandle);
        List<Account> accounts = a.getFollowing();
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", current());
        return "follows";
    }

    @GetMapping("/accounts/{profileHandle}/photos")
    private String photos(Model model, @PathVariable String profileHandle) {
        Account a = accountService.accountRepository.findByProfileHandle(profileHandle);
        model.addAttribute("photos", a.getPhotos());
        model.addAttribute("currentAccount", current());
        return "photos";
    }

}

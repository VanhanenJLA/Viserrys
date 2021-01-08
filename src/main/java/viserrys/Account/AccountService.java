package viserrys.Account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import viserrys.Photo.PhotoService;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PhotoService photoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account createAccount(String username, String password) throws Exception {

        if (accountRepository.findByUsername(username) != null)
            throw new Exception("Username taken.");

        Account a = new Account();
        a.setUsername(username);
        a.setPassword(passwordEncoder.encode(password));

        return accountRepository.save(a);
    }

    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account getAccount(String username) {
        return accountRepository.findByUsername(username);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public void follow(Account sender, Account recipient) throws Exception {

        if (sender.equals(recipient))
            throw new Exception("Cannot follow oneself!");

        if (sender.getFollowing().contains(recipient))
            throw new Exception("Already following " + recipient.getUsername());

        sender.getFollowing().add(recipient);
        recipient.getFollowers().add(sender);
        accountRepository.save(sender);
        accountRepository.save(recipient);
    }

    public void unfollow(Account sender, Account recipient) {
        sender.getFollowing().remove(recipient);
        recipient.getFollowers().remove(sender);
        accountRepository.save(sender);
        accountRepository.save(recipient);
    }

    public byte[] getProfilePicture(Account account) {
        return account.getProfilePicture().getContent();
    }

    public Account setProfilePicture(Account account, long photoId) {
        var photo = photoService.getOne(photoId);
        account.profilePicture = photo;
        return accountRepository.save(account);
    }

}

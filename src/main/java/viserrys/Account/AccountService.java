package viserrys.Account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import viserrys.Follow.Follow;
import viserrys.Follow.FollowService;
import viserrys.Photo.PhotoService;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PhotoService photoService;

    @Autowired
    FollowService followService;

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

    public Follow follow(Account sender, Account recipient) throws Exception {
        return followService.follow(sender, recipient);
    }

    public void unfollow(Account sender, Account recipient) throws Exception {
        followService.unfollow(sender, recipient);
    }

    public byte[] getProfilePicture(Account account) {
        return account.getProfilePicture().getContent();
    }

    public Account setProfilePicture(Account account, long photoId) {
        var photo = photoService.getOne(photoId);
        account.profilePicture = photo;
        return accountRepository.save(account);
    }

    public void deletePicture(Account current, Long photoId) {
        photoService.delete(photoId);
    }

}

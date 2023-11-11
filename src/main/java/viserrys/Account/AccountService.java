package viserrys.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import viserrys.Follow.Follow;
import viserrys.Follow.FollowService;
import viserrys.Photo.PhotoService;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    PhotoService photoService;

    @Autowired
    FollowService followService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account createAccount(String username, String password) throws Exception {

        if (accountRepository.findByUsername(username) != null)
            throw new Exception("Username taken.");

        var account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        return accountRepository.save(account);
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

    public Account setProfilePicture(Account account, long photoId) {
        var photo = photoService.photoRepository.getOne(photoId);
        account.profilePicture = photo;
        return accountRepository.save(account);
    }

    public void deletePicture(Account current, long photoId) {
        photoService.delete(photoId);
    }

}

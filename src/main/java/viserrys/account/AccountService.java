package viserrys.account;

import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import viserrys.follow.Follow;
import viserrys.follow.FollowService;
import viserrys.photo.PhotoService;

import java.util.List;

@Service
public class AccountService {

    public final AccountRepository accountRepository;
    final PhotoService photoService;
    final FollowService followService;
    final PasswordEncoder brcypt;

    public AccountService(AccountRepository accountRepository, PhotoService photoService, FollowService followService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.photoService = photoService;
        this.followService = followService;
        this.brcypt = passwordEncoder;
    }

    @SneakyThrows
    public Account createAccount(String username, String password) {

        if (accountRepository.findByUsername(username).isPresent())
            throw new Exception("Username taken.");

        var account = new Account();
        account.setUsername(username);
        account.setPassword(brcypt.encode(password));

        return accountRepository.save(account);
    }

    @SneakyThrows
    public Account getAccount(String username) {
        return accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new Exception("User not found: " + username));
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Follow follow(Account sender, Account recipient) {
        return followService.follow(sender, recipient);
    }

    public void unfollow(Account sender, Account recipient) {
        followService.unfollow(sender, recipient);
    }

    public Account setProfilePicture(Account account, long photoId) {
        var photo = photoService.getPhoto(photoId);
        account.profilePicture = photo;
        return accountRepository.save(account);
    }

    public void deletePicture(Account current, long photoId) {
        photoService.deleteBy(photoId);
    }

}

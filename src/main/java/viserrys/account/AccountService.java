package viserrys.account;

import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import viserrys.auth.AuthService;
import viserrys.follow.Follow;
import viserrys.follow.FollowService;
import viserrys.photo.Photo;
import viserrys.photo.PhotoService;

import java.util.List;

@Service
public class AccountService {

    final AccountRepository accountRepository;
    final PhotoService photoService;
    final FollowService followService;
    final PasswordEncoder brcypt;
    final AuthService authService;

    public AccountService(AccountRepository accountRepository,
                          PhotoService photoService,
                          FollowService followService,
                          PasswordEncoder passwordEncoder,
                          AuthService authService) {
        this.accountRepository = accountRepository;
        this.photoService = photoService;
        this.followService = followService;
        this.brcypt = passwordEncoder;
        this.authService = authService;
    }

    public Account getAuthenticatedAccount() {
        var name = authService.getAuthenticationPrincipalName();
        return getAccount(name);
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
        var photo = photoService.getPhotoById(photoId);
        account.profilePicture = photo;
        return accountRepository.save(account);
    }

    public void deletePhoto(Account current, long photoId) {
        if (current == null)
            current = getAuthenticatedAccount();
        var photo = photoService.getPhotoById(photoId);
        ensureIsAllowedToDelete(current, photo);
        photoService.deleteById(photoId);
    }

    @SneakyThrows
    private void ensureIsAllowedToDelete(Account deleter, Photo photo) {
        if (photo.getUploader() != deleter)
            throw new Exception("Deleter is not the uploader of photo being deleted.");
    }

}

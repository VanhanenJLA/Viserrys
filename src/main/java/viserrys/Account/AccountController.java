package viserrys.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import viserrys.Auth.AuthService;
import viserrys.Comment.CommentService;
import viserrys.Photo.PhotoService;
import viserrys.Tweet.Tweet;
import viserrys.Tweet.TweetService;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    AuthService authService;

    @Autowired
    PhotoService photoService;

    @Autowired
    TweetService tweetService;

    @Autowired
    CommentService commentService;

    private Account current() {
        return authService.getAuthenticatedAccount();
    }

    @GetMapping("/me")
    private String me(Model model) {
        model.addAttribute("activeNavLink", "me");
        return account(model, current().getUsername());
    }

    @GetMapping("/my-photos")
    private String myPhotos(Model model) {
        model.addAttribute("activeNavLink", "my-photos");
        return photos(model, current().getUsername());
    }

    @PostMapping("/my-photos/set-profile-picture")
    public String setProfilePicture(Model model, @RequestParam Long photoId) {
        accountService.setProfilePicture(current(), photoId);
        return myPhotos(model);
    }

    @PostMapping("/my-photos/delete-picture")
    public String deletePicture(Model model, @RequestParam Long photoId) {
        accountService.deletePicture(current(), photoId);
        return myPhotos(model);
    }

    @GetMapping("/accounts/{username}")
    private String account(Model model, @PathVariable String username) {
        var account = accountService.accountRepository.findByUsername(username);
        var tweets = tweetService.findAllByRecipient(account);
        model.addAttribute("tweets", tweets);
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", current());
        return "account";
    }

    @GetMapping("/others")
    private String others(Model model) {
        model.addAttribute("activeNavLink", "others");
        var accounts = accountService.getAccounts();
        var me = current();
        accounts.remove(me);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", me);
        return "others";
    }

    @PostMapping("/accounts/{username}/follow")
    private String follow(Model model, @PathVariable String username) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        accountService.follow(sender, recipient);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/unfollow")
    private String unfollow(Model model, @PathVariable String username) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        accountService.unfollow(sender, recipient);
        return account(model, username);
    }

    @PostMapping("/accounts/{username}/tweet")
    private String tweet(Model model, @PathVariable String username, @RequestParam String content) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        tweetService.tweet(sender, recipient, LocalDateTime.now(), content);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/photos/{id}/comment")
    private String comment(Model model, @PathVariable String username, @PathVariable Long id,
            @RequestParam String content) throws Exception {
        var sender = current();
        var comment = commentService.comment(sender, LocalDateTime.now(), content);
        photoService.comment(id, comment);
        return "redirect:/accounts/{username}/photos";
    }

    List<Tweet> FakeTweets() {
        var asd = "Improve your goldfish's physical fitness by getting him a bicycle.";
        var tweet = new Tweet(current(), current(), LocalDateTime.now(), asd);
        var tweets = new ArrayList<Tweet>();

        for (int i = 0; i < 10; i++) {
            tweets.add(tweet);
        }
        return tweets;
    }

    @GetMapping("/accounts/{username}/followers")
    private String followers(Model model, @PathVariable String username) {
        var account = accountService.accountRepository.findByUsername(username);
        var accounts = account.getFollowers();
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", current());
        return "follows";
    }

    @GetMapping("/accounts/{username}/following")
    private String following(Model model, @PathVariable String username) {
        var account = accountService.accountRepository.findByUsername(username);
        var accounts = account.getFollowing();
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", current());
        return "follows";
    }

    @GetMapping("/accounts/{username}/photos")
    private String photos(Model model, @PathVariable String username) {
        var account = accountService.accountRepository.findByUsername(username);
        var currentAccount = current();
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        return "photos";
    }

    @PostMapping("/accounts/{username}/photos")
    private String uploadPhoto(@PathVariable String username, @RequestParam MultipartFile file,
            @RequestParam String description) throws Exception {

        var uploader = authService.getAuthenticatedAccount();

        photoService.uploadPhoto(file, description, uploader);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping(path = "/accounts/{username}/profile-picture", produces = "image/jpg")
    @ResponseBody
    public byte[] profilePicture(@PathVariable String username) {
        var account = accountService.getAccount(username);
        return account.getProfilePicture().getContent();
    }

    @PostMapping("accounts/{username}/photos/{id}/like")
    public String like(Model model, @PathVariable String username, @PathVariable Long id) throws Exception {
        var photo = photoService.like(id, current());
        return "redirect:/accounts/{username}/photos";
    }

}

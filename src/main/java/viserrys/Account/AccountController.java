package viserrys.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import viserrys.Reaction.ReactionService;
import viserrys.Reaction.ReactionType;
import viserrys.Tweet.Tweet;
import viserrys.Tweet.TweetService;

interface UrlService {
    String getApplicationUrl();
}

class Tweets {
    public List<Tweet> sent;
    public List<Tweet> received;

    public Tweets(List<Tweet> sent, List<Tweet> received) {
        this.sent = sent;
        this.received = received;
    }
}

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

    @Autowired
    ReactionService reactionService;

    private Account current() {
        return authService.getAuthenticatedAccount();
    }

    @GetMapping("/me")
    private String me(Model model) {
        model.addAttribute("activeNavLink", "me");
        var me = current().getUsername();
        return "redirect:/accounts/" + me;
    }

    @GetMapping("/my-photos")
    private String myPhotos(Model model) {
        model.addAttribute("activeNavLink", "my-photos");
        return redirectMyPhotos();
    }

    @PostMapping("/my-photos/set-profile-picture")
    public String setProfilePicture(Model model, @RequestParam long photoId) {
        accountService.setProfilePicture(current(), photoId);
        return redirectMyPhotos();
    }

    @PostMapping("/my-photos/delete-picture")
    public String deletePicture(Model model, @RequestParam long photoId) {
        accountService.deletePicture(current(), photoId);
        return redirectMyPhotos();
    }

    private String redirectMyPhotos() {
        var me = current().getUsername();
        return "redirect:/accounts/" + me + "/photos";
    }

    @GetMapping("/accounts/{username}")
    private String account(Model model, @PathVariable String username, @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {

        var account = accountService.accountRepository.findByUsername(username);
        var tweetsSent = tweetService.findAllBySender(account);
        var tweetsReceived = tweetService.findAllByRecipient(account);
        var tweets = new Tweets(tweetsSent, tweetsReceived);
        List<Account> following = account.following.stream().map(f -> f.getRecipient()).collect(Collectors.toList());
        List<Account> followers = account.followers.stream().map(f -> f.getSender()).collect(Collectors.toList());

        var page = tweetService.findAllByRecipient(account, pageNumber, pageSize);
        var pageSizes = List.of(5, 10, 25, 50, 100);
        var maxPages = Math.min(page.getTotalPages() - 1, 10);
        model.addAttribute("page", page);
        model.addAttribute("pageSizes", pageSizes);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("maxPages", maxPages);

        model.addAttribute("currentAccount", current());
        model.addAttribute("account", account);
        model.addAttribute("tweets", tweets);
        model.addAttribute("following", following);
        model.addAttribute("followers", followers);
        model.addAttribute("view", "profile");

        return "pages/account";
    }

    @GetMapping("/others")
    private String others(Model model) {
        model.addAttribute("activeNavLink", "others");
        var accounts = accountService.getAccounts();
        var me = current();
        accounts.remove(me);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", me);
        return "pages/others";
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
        return "redirect:/accounts/{username}";
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
        var photo = photoService.photoRepository.getOne(id);
        var comment = commentService.comment(sender, photo, LocalDateTime.now(), content);
        // photoService.comment(id, comment);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping("/accounts/{username}/photos")
    private String photos(Model model, @PathVariable String username) {
        var account = accountService.accountRepository.findByUsername(username);
        var currentAccount = current();
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("reactionService", reactionService);
        model.addAttribute("view", "photos");

        return "pages/photos";
    }

    @GetMapping("/accounts/{username}/photos/{photoId}")
    private String photos(Model model, @PathVariable String username, @PathVariable long photoId,
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        var account = accountService.accountRepository.findByUsername(username);
        var currentAccount = current();
        var photo = photoService.photoRepository.findById(photoId).get();
        model.addAttribute("photo", photo);
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("reactionService", reactionService);
        model.addAttribute("view", "photos");

        var page = commentService.findAllByTargetId(photoId, pageNumber, pageSize);
        var pageSizes = List.of(5, 10, 25, 50, 100);
        var maxPages = Math.min(page.getTotalPages() - 1, 10);
        model.addAttribute("page", page);
        model.addAttribute("pageSizes", pageSizes);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("maxPages", maxPages);

        return "pages/photo";
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

    @PostMapping("accounts/{username}/photos/{id}/react")
    public String like(Model model, @PathVariable String username, @PathVariable Long id,
            @RequestParam ReactionType reactionType) throws Exception {
        var photo = photoService.photoRepository.getOne(id);

        reactionService.react(current(), photo, LocalDateTime.now(), reactionType);
        return "redirect:/accounts/{username}/photos";
    }

}

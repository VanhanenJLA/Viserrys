package viserrys.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import viserrys.auth.AuthService;
import viserrys.comment.CommentService;
import viserrys.photo.PhotoService;
import viserrys.reaction.ReactionService;
import viserrys.reaction.ReactionType;
import viserrys.tweet.Tweet;
import viserrys.tweet.TweetService;
import viserrys.follow.Follow;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final String ACTIVE_NAV_LINK = "activeNavLink";
    final AccountService accountService;

    final AuthService authService;

    final PhotoService photoService;

    final TweetService tweetService;

    final CommentService commentService;

    final ReactionService reactionService;

    public AccountController(AccountService accountService,
                             AuthService authService,
                             PhotoService photoService,
                             TweetService tweetService,
                             CommentService commentService,
                             ReactionService reactionService) {
        this.accountService = accountService;
        this.authService = authService;
        this.photoService = photoService;
        this.tweetService = tweetService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    Account current() {
        return authService.getAuthenticatedAccount();
    }

    @GetMapping("/me")
    String me(Model model) {
        model.addAttribute(ACTIVE_NAV_LINK, "me");
        var me = current().getUsername();
        return "redirect:/accounts/" + me;
    }

    @GetMapping("/my-photos")
    String myPhotos(Model model) {
        model.addAttribute(ACTIVE_NAV_LINK, "my-photos");
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

    String redirectMyPhotos() {
        var me = current().getUsername();
        return "redirect:/accounts/" + me + "/photos";
    }

    @GetMapping("/accounts/{username}")
    String account(Model model, @PathVariable String username, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {

        var account = accountService.accountRepository.findByUsername(username);
        var tweetsSent = tweetService.findAllBySender(account);
        var tweetsReceived = tweetService.findAllByRecipient(account);
        var tweets = new Tweets(tweetsSent, tweetsReceived);
        List<Account> following = account.following.stream().map(f -> f.getRecipient()).collect(Collectors.toList());
        List<Account> followers = account.followers.stream().map(Follow::getSender).collect(Collectors.toList());

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
    String others(Model model) {
        model.addAttribute(ACTIVE_NAV_LINK, "others");
        var accounts = accountService.getAccounts();
        var me = current();
        accounts.remove(me);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentAccount", me);
        return "pages/others";
    }

    @PostMapping("/accounts/{username}/follow")
    String follow(Model model, @PathVariable String username) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        accountService.follow(sender, recipient);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/unfollow")
    String unfollow(Model model, @PathVariable String username) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        accountService.unfollow(sender, recipient);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/tweet")
    String tweet(Model model, @PathVariable String username, @RequestParam String content) throws Exception {
        var sender = current();
        var recipient = accountService.accountRepository.findByUsername(username);
        tweetService.tweet(sender, recipient, LocalDateTime.now(), content);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/photos/{id}/comment")
    String comment(Model model, @PathVariable String username, @PathVariable Long id, @RequestParam String content) throws Exception {
        var sender = current();
        var photo = photoService.photoRepository.getOne(id);
        var comment = commentService.comment(sender, photo, LocalDateTime.now(), content);
        // photoService.comment(id, comment);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping("/accounts/{username}/photos")
    String photos(Model model, @PathVariable String username) {
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
    String photos(Model model, @PathVariable String username, @PathVariable long photoId, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
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
    String uploadPhoto(@PathVariable String username, @RequestParam MultipartFile file, @RequestParam String description) throws Exception {

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
    public String like(Model model, @PathVariable String username, @PathVariable Long id, @RequestParam ReactionType reactionType) throws Exception {
        var photo = photoService
                .photoRepository
                .findById(id)
                .orElseThrow(() -> new Error("Can't react to a non-existent photo."));

        reactionService.react(current(), photo, LocalDateTime.now(), reactionType);
        return "redirect:/accounts/{username}/photos";
    }

}

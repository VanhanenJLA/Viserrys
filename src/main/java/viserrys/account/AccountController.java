package viserrys.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import viserrys.auth.AuthService;
import viserrys.comment.CommentService;
import viserrys.follow.FollowService;
import viserrys.follow.Follows;
import viserrys.photo.PhotoService;
import viserrys.reaction.ReactionService;
import viserrys.reaction.ReactionType;
import viserrys.tweet.TweetService;
import viserrys.tweet.Tweets;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AccountController {

    private static final String ACTIVE_NAV_LINK = "activeNavLink";
    final AccountService accountService;
    final AuthService authService;
    final PhotoService photoService;
    final TweetService tweetService;
    final CommentService commentService;
    final ReactionService reactionService;
    private final FollowService followService;

    public AccountController(AccountService accountService,
                             AuthService authService,
                             PhotoService photoService,
                             TweetService tweetService,
                             CommentService commentService,
                             ReactionService reactionService,
                             FollowService followService) {
        this.accountService = accountService;
        this.authService = authService;
        this.photoService = photoService;
        this.tweetService = tweetService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.followService = followService;
    }

    private static <T> void doVoodoo(Model model, int pageSize, Page<T> page) {
        var pageSizes = List.of(5, 10, 25, 50, 100);
        var maxPages = Math.min(page.getTotalPages() - 1, 10);

        model.addAttribute("page", page);
        model.addAttribute("pageSizes", pageSizes);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("maxPages", maxPages);
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
    String account(Model model,
                   @PathVariable String username,
                   @RequestParam(defaultValue = "0") int pageNumber,
                   @RequestParam(defaultValue = "5") int pageSize) throws Exception {

        var account = accountService.accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new Exception("User not found: " + username));
        var current = current();
        var isFollowing = followService.isFollowing(current, account);

        var follows = new Follows(
                followService.findAllBySender(account),
                followService.findAllByRecipient(account)
        );

        var tweets = new Tweets(
                tweetService.findAllBySender(account),
                tweetService.findAllByRecipient(account)
        );

        var page = tweetService.findAllByRecipient(account, pageNumber, pageSize);
        doVoodoo(model, pageSize, page);

        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("currentAccount", current);
        model.addAttribute("account", account);
        model.addAttribute("tweets", tweets);
        model.addAttribute("follows", follows);
        model.addAttribute("photos", List.of());
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
        model.addAttribute("followService", followService);
        model.addAttribute("photoService", photoService);
        return "pages/others";
    }

    @PostMapping("/accounts/{username}/follow")
    String follow(Model model, @PathVariable String username) {
        var sender = current();
        var recipient = accountService.getAccount(username);
        accountService.follow(sender, recipient);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/unfollow")
    String unfollow(Model model, @PathVariable String username) {
        var sender = current();
        var recipient = accountService.getAccount(username);
        accountService.unfollow(sender, recipient);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/tweet")
    String tweet(Model model, @PathVariable String username, @RequestParam String content) {
        var sender = current();
        var recipient = accountService.getAccount(username);
        tweetService.tweet(sender, recipient, Instant.now(), content);
        return "redirect:/accounts/{username}";
    }

    @PostMapping("/accounts/{username}/photos/{id}/comment")
    String comment(Model model, @PathVariable String username, @PathVariable Long id, @RequestParam String content) {
        var sender = current();
        var photo = photoService.getPhoto(id);
        var comment = commentService.comment(sender, photo, Instant.now(), content);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping("/accounts/{username}/photos")
    String photos(Model model, @PathVariable String username) {
        var account = accountService.getAccount(username);
        var currentAccount = current();
        var photos = photoService.getPhotosByUploader(account);

        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("photos", photos);

        model.addAttribute("view", "photos");
        model.addAttribute("reactionService", reactionService);

        return "pages/photos";
    }

    @GetMapping("/accounts/{username}/photos/{photoId}")
    String photos(Model model,
                  @PathVariable String username,
                  @PathVariable long photoId,
                  @RequestParam(defaultValue = "0") int pageNumber,
                  @RequestParam(defaultValue = "5") int pageSize) {
        var account = accountService.getAccount(username);
        var currentAccount = current();
        var photo = photoService.getPhoto(photoId);
        model.addAttribute("photo", photo);
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("reactionService", reactionService);
        model.addAttribute("view", "photos");

        var page = commentService.findAllByTargetId(photoId, pageNumber, pageSize);
        doVoodoo(model, pageSize, page);

        return "pages/photo";
    }

    @PostMapping("/accounts/{username}/photos")
    String uploadPhoto(@PathVariable String username,
                       @RequestParam MultipartFile file,
                       @RequestParam String description) throws Exception {
        var uploader = authService.getAuthenticatedAccount();
        photoService.uploadPhoto(file, description, uploader);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping(path = "/accounts/{username}/profile-picture", produces = "image/jpg")
    @ResponseBody
    public byte[] profilePicture(@PathVariable String username) {
        var account = accountService.getAccount(username);
        return account
                .getProfilePicture()
                .getContent();
    }

    @PostMapping("accounts/{username}/photos/{id}/react")
    public String like(Model model,
                       @PathVariable String username,
                       @PathVariable Long id,
                       @RequestParam ReactionType reactionType) {
        var photo = photoService.getPhoto(id);
        reactionService.react(current(), photo, LocalDateTime.now(), reactionType);
        return "redirect:/accounts/{username}/photos";
    }

}

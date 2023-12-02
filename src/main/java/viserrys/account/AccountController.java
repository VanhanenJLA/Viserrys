package viserrys.account;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import static viserrys.common.Constants.Paging.*;

@Controller
public class AccountController {

    private static final String ACTIVE_NAV_LINK = "activeNavLink";
    final AccountService accountService;
    final AuthService authService;
    final PhotoService photoService;
    final TweetService tweetService;
    final CommentService commentService;
    final ReactionService reactionService;
    final FollowService followService;

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

    final Account current() {
        return accountService.getAuthenticatedAccount();
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
        var me = current().getUsername();
        return "redirect:/accounts/" + me + "/photos";
    }

    @PostMapping("/my-photos/set-profile-picture")
    public String setProfilePicture(Model model, @RequestParam long photoId) {
        accountService.setProfilePicture(current(), photoId);
        var me = current().getUsername();
        return "redirect:/accounts/" + me + "/photos";
    }

    @PostMapping("/my-photos/delete-picture")
    public String deletePicture(Model model, @RequestParam long photoId) {
        accountService.deletePhoto(current(), photoId);
        var me = current().getUsername();
        return "redirect:/accounts/" + me + "/photos";
    }
    
    @GetMapping("/accounts/{username}")
    String account(Model model,
                   @PathVariable String username,
                   @PageableDefault(size = PAGEABLE_DEFAULT_SIZE, sort = PAGEABLE_DEFAULT_SORT, direction = Sort.Direction.DESC) Pageable tweetPageable) {

        var account = accountService.getAccount(username);
        
        var current = current();
        var isFollowing = followService.isFollowing(current, account);

        var stats = populateStats(account);

        var tweetPage = tweetService.findAllByRecipient(account, tweetPageable);

        model.addAttribute("page", tweetPage);
        model.addAttribute("pageSizes", PAGE_SIZES);

        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("currentAccount", current);
        model.addAttribute("account", account);
        model.addAttribute("stats", stats);

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
        var photo = photoService.getPhotoById(id);
        var comment = commentService.comment(sender, photo, Instant.now(), content);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping("/accounts/{username}/photos")
    String photos(Model model, @PathVariable String username) {
        var account = accountService.getAccount(username);
        var currentAccount = current();
        var photos = photoService.findAllByUploader(account, null);

        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("photos", photos);
        
        var canAddPhoto = account == currentAccount && photos.getTotalElements() < 5;
        model.addAttribute("view", "photos");
        model.addAttribute("canAddPhoto", canAddPhoto);
        model.addAttribute("reactionService", reactionService);

        return "pages/photos";
    }

    @GetMapping("/accounts/{username}/photos/{photoId}")
    String photos(Model model,
                  @PathVariable String username,
                  @PathVariable long photoId,
                  @PageableDefault(size = PAGEABLE_DEFAULT_SIZE, sort = PAGEABLE_DEFAULT_SORT) Pageable commentPageable) {
        var account = accountService.getAccount(username);
        var currentAccount = current();
        var photo = photoService.getPhotoById(photoId);
        model.addAttribute("photo", photo);
        model.addAttribute("account", account);
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("reactionTypes", ReactionType.values());
        model.addAttribute("reactionService", reactionService);
        model.addAttribute("view", "photos");

        var commentPage = commentService.findAllByTargetId(photoId, commentPageable.getPageNumber(), commentPageable.getPageSize());
        model.addAttribute("page", commentPage);
        model.addAttribute("pageSizes", PAGE_SIZES);

        return "pages/photo";
    }

    @PostMapping("/accounts/{username}/photos")
    String uploadPhoto(@PathVariable String username,
                       @RequestParam MultipartFile file,
                       @RequestParam String description) throws Exception {
        var uploader = accountService.getAuthenticatedAccount();
        photoService.uploadPhoto(file, description, uploader);
        return "redirect:/accounts/{username}/photos";
    }

    @GetMapping(path = "/accounts/{username}/profile-picture", produces = "image/jpg")
    @ResponseBody
    public byte[] profilePicture(@PathVariable String username) {
        return accountService
                .getAccount(username)
                .getProfilePicture()
                .getContent();
    }

    @PostMapping("accounts/{username}/photos/{id}/react")
    public String react(Model model,
                       @PathVariable String username,
                       @PathVariable Long id,
                       @RequestParam ReactionType reactionType) {
        var photo = photoService.getPhotoById(id);
        reactionService.react(current(), photo, Instant.now(), reactionType);
        return "redirect:/accounts/{username}/photos";
    }

    private Stats populateStats(Account account) {
        var pageableDefault = PageRequest.of(0,
                PAGEABLE_DEFAULT_SIZE,
                Sort
                        .by("timestamp")
                        .descending());
        return Stats
                .create()
                .follows(
                        followService
                                .findAllBySender(account, pageableDefault)
                                .getTotalElements(),
                        followService
                                .findAllByRecipient(account, pageableDefault)
                                .getTotalElements()
                )
                .tweets(
                        tweetService
                                .findAllBySender(account, pageableDefault)
                                .getTotalElements(),
                        tweetService
                                .findAllByRecipient(account, pageableDefault)
                                .getTotalElements()
                )
                .photos(
                        photoService
                                .findAllByUploader(account, pageableDefault)
                                .getTotalElements() 
                );
    }

}

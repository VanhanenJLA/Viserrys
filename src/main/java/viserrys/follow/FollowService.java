package viserrys.follow;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;
import java.util.List;

@Service
public class FollowService {

    public final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    private static void ensureNotSelfFollow(Account sender, Account recipient) throws Exception {
        if (sender.equals(recipient))
            throw new Exception("Self follows are not allowed.");
    }

    @SneakyThrows
    public Follow follow(Account sender, Account recipient) {
        ensureNotSelfFollow(sender, recipient);

        if (followRepository
                .findBySenderAndRecipient(sender, recipient)
                .isPresent())
            throw new Exception(sender.getUsername() + " is already following " + recipient.getUsername());

        return followRepository.save(new Follow(sender, recipient, Instant.now()));
    }

    @SneakyThrows
    public void unfollow(Account sender, Account recipient) {
        ensureNotSelfFollow(sender, recipient);
        followRepository
                .findBySenderAndRecipient(sender, recipient)
                .ifPresent(follow -> followRepository.delete(follow));
    }

    @SneakyThrows
    public List<Follow> getFollowsBySender(Account sender) {
        return followRepository
                .findAllBySender(sender);
    }    
    @SneakyThrows
    public List<Follow> getFollowsByRecipient(Account recipient) {
        return followRepository
                .findAllBySender(recipient);
    }

    public boolean isFollowing(Account account, Account target) {
        return followRepository
                .findBySenderAndRecipient(account, target)
                .isPresent();
    }

}

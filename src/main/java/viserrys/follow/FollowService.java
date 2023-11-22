package viserrys.follow;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
    
    public Follows getFollowCountsBy(Account account) {
        Map<String, Long> countsMap = followRepository.countSentAndReceivedFollowsBy(account);
        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public boolean isFollowing(Account account, Account target) {
        return followRepository
                .findBySenderAndRecipient(account, target)
                .isPresent();
    }

    public List<Follow> findAllByRecipient(Account recipient) {
        return followRepository
                .findAllByRecipient(recipient);
    }

    public List<Follow> findAllBySender(Account sender) {
        return followRepository
                .findAllBySender(sender);
    }
}

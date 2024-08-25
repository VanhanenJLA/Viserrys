package viserrys.follow;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;

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
                .ifPresent(followRepository::delete);
    }
    
    public boolean isFollowing(Account account, Account target) {
        return followRepository
                .findBySenderAndRecipient(account, target)
                .isPresent();
    }

    public Page<Follow> findAllByRecipient(Account recipient, Pageable pageable) {
        return followRepository
                .findAllByRecipient(recipient, pageable);
    }

    public Page<Follow> findAllBySender(Account sender, Pageable pageable) {
        return followRepository
                .findAllBySender(sender, pageable);
    }
}


package viserrys.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;

@Service
public class FollowService {

    @Autowired
    public FollowRepository followRepository;

    public Follow follow(Account sender, Account recipient) throws Exception {
        if (sender.equals(recipient))
            throw new Exception("An account cannot follow itself.");

        
        if (followRepository.findBySenderAndRecipient(sender, recipient).isPresent())
            throw new Exception(sender.getUsername() + " is already following " + recipient.getUsername());

        return followRepository.save(new Follow(sender, recipient, Instant.now()));
    }

    public void unfollow(Account sender, Account recipient) throws Exception {
        if (sender.equals(recipient))
            throw new Exception("An account cannot follow or unfollow itself.");

        followRepository
                .findBySenderAndRecipient(sender, recipient)
                .ifPresent(follow -> followRepository.delete(follow));
    }

}


package viserrys.Follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viserrys.Account.Account;

import java.time.LocalDateTime;

@Service
public class FollowService {

    @Autowired
    public FollowRepository followRepository;

    public Follow follow(Account sender, Account recipient) throws Exception {
        if (sender.equals(recipient))
            throw new Exception("An account cannot follow itself.");

        if (recipient.getFollowers().stream().anyMatch(f -> f.getSender().equals(sender)))
            throw new Exception(sender.getUsername() + " is already following " + recipient.getUsername());

        return followRepository.save(new Follow(sender, recipient, LocalDateTime.now()));
    }

    public void unfollow(Account sender, Account recipient) throws Exception {
        if (sender.equals(recipient))
            throw new Exception("An account cannot follow or unfollow itself.");

        var follow = followRepository.findBySenderAndRecipient(sender, recipient);
        followRepository.delete(follow);
    }

}

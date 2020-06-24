
package viserrys.Follow;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;


@Service
public class FollowService {

    @Autowired
    public FollowRepository followRepository;

    public void createFollow(Account sender, Account recipient) {
        followRepository.save(
                new Follow(sender, recipient, LocalDateTime.now()));
    }

}

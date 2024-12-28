package viserrys.tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;

@Service
public class TweetService {

    public final TweetRepository tweetRepository;

    public TweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public Tweet tweet(Account sender, Account recipient, Instant timestamp, String content) {
        return tweetRepository.save(new Tweet(sender, recipient, timestamp, content));
    }

    public Page<Tweet> findAllByRecipient(Account recipient, Pageable pageable) {
        return tweetRepository.findAllByRecipient(recipient, pageable);
    }

    public long countSentTweets(Account sender) {
        return tweetRepository.countBySender(sender);
    }
    
    public long countReceivedTweets(Account recipient) {
        return tweetRepository.countByRecipient(recipient);
    }
}

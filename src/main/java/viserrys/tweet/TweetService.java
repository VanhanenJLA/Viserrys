package viserrys.tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;
import java.util.List;

@Service
public class TweetService {

    public final TweetRepository tweetRepository;

    public TweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public Tweet tweet(Account sender, Account recipient, Instant timestamp, String content) {
        return tweetRepository.save(new Tweet(sender, recipient, timestamp, content));
    }

    public List<Tweet> findAllByRecipient(Account recipient) {
        return tweetRepository.findAllByRecipient(recipient);
    }

    public List<Tweet> findAllBySender(Account sender) {
        return tweetRepository.findAllBySender(sender);
    }

    //  public TweetsRecord getTweetsAssociatedWithAccount(Account sender) {
//    return tweetRepository.findAllBySender(sender);
//  }

    public Page<Tweet> findAllByRecipient(Account recipient, int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber,
                pageSize,
                Sort
                        .by("timestamp")
                        .descending());
        return tweetRepository.findAllByRecipient(recipient, pageRequest);
    }

    public Page<Tweet> findAllByRecipient(Account recipient, Pageable pageable) {
        return tweetRepository.findAllByRecipient(recipient, pageable);
    }
}

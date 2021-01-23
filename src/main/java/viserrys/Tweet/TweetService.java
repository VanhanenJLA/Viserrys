package viserrys.Tweet;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;

@Service
public class TweetService {

  @Autowired
  TweetRepository tweetRepository;

  public Tweet tweet(Account sender, Account recipient, LocalDateTime timestamp, String content) {
    return tweetRepository.save(new Tweet(sender, recipient, timestamp, content));
  }

  public List<Tweet> findAllByRecipient(Account recipient) {
    return tweetRepository.findAllByRecipient(recipient);
  }

}

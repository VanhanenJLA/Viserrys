package viserrys.Tweet;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;

@Service
public class TweetService {

  @Autowired
  public TweetRepository tweetRepository;

  public Tweet tweet(Account sender, Account recipient, LocalDateTime timestamp, String content) {
    return tweetRepository.save(new Tweet(sender, recipient, timestamp, content));
  }

  public List<Tweet> findAllByRecipient(Account recipient) {
    return tweetRepository.findAllByRecipient(recipient);
  }

  public List<Tweet> findAllBySender(Account sender) {
    return tweetRepository.findAllBySender(sender);
  }

  public Page<Tweet> findAllByRecipient(Account recipient, int pageNumber, int pageSize) {
    Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending());
    return tweetRepository.findAllByRecipient(recipient, paging);
  }

}

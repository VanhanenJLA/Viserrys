package viserrys.tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
  Page<Tweet> findAllByRecipient(Account recipient, Pageable p);
  Page<Tweet> findAllBySender(Account sender, Pageable p);
  
}

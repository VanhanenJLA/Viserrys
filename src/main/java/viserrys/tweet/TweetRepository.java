package viserrys.tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import viserrys.account.Account;

import java.util.List;
import java.util.Map;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
  Page<Tweet> findAllByRecipient(Account recipient, Pageable p);
  Page<Tweet> findAllBySender(Account sender, Pageable p);
  
}

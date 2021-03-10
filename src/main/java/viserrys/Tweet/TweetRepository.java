package viserrys.Tweet;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import viserrys.Account.Account;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
  List<Tweet> findAllByRecipient(Account recipient);
  Page<Tweet> findAllByRecipient(Account sender, Pageable paging);
  List<Tweet> findAllBySender(Account sender);
}

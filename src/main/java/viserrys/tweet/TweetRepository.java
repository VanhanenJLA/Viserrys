package viserrys.tweet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
  List<Tweet> findAllByRecipient(Account recipient);
  Page<Tweet> findAllByRecipient(Account sender, Pageable paging);
  List<Tweet> findAllBySender(Account sender);
}

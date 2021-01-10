package viserrys.Tweet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import viserrys.Account.Account;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
  List<Tweet> findAllByRecipient(Account recipient);
}

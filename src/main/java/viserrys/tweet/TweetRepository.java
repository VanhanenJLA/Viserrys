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
  List<Tweet> findAllByRecipient(Account recipient);
  Page<Tweet> findAllByRecipient(Account recipient, Pageable p);
  List<Tweet> findAllBySender(Account sender);
  List<Tweet> findAllBySender(Account sender, Pageable p);

  @Query("SELECT COUNT(f) AS sentCount, (SELECT COUNT(fr) FROM Follow fr WHERE fr.recipient = :account) AS receivedCount FROM Follow f WHERE f.sender = :account")
  Map<String, Long> countSentAndReceivedFollowsBy(@Param("account") Account account);
  
}

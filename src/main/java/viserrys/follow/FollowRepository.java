package viserrys.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import viserrys.account.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findBySenderAndRecipient(Account sender, Account recipient);
    List<Follow> findBySender(Account sender);
    List<Follow> findAllBySender(Account sender);
    List<Follow> findAllByRecipient(Account recipient);

    @Query("SELECT COUNT(f) AS sentCount, (SELECT COUNT(fr) FROM Follow fr WHERE fr.recipient = :account) AS receivedCount FROM Follow f WHERE f.sender = :account")
    Map<String, Long> countSentAndReceivedFollowsBy(@Param("account") Account account);
    
}

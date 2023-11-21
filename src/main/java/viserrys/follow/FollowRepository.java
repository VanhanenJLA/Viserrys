package viserrys.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findBySenderAndRecipient(Account sender, Account recipient);
    List<Follow> findBySender(Account sender);
    List<Follow> findAllBySender(Account sender);
    List<Follow> findAllByRecipient(Account recipient);
}

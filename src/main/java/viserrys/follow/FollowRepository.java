package viserrys.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findBySenderAndRecipient(Account sender, Account recipient);
    long countBySender(Account sender);
    long countByRecipient(Account recipient);
}

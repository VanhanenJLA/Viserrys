package viserrys.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findBySenderAndRecipient(Account sender, Account recipient);

}

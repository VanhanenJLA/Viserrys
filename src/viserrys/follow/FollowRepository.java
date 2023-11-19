package viserrys.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findBySenderAndRecipient(Account sender, Account recipient);

}

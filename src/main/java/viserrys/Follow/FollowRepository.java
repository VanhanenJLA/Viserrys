package viserrys.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.Account.Account;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findBySenderAndRecipient(Account sender, Account recipient);

}

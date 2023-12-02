package viserrys.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import viserrys.account.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findBySenderAndRecipient(Account sender, Account recipient);
    Page<Follow> findAllBySender(Account sender, Pageable pageable);
    Page<Follow> findAllByRecipient(Account recipient, Pageable pageable);
    
}

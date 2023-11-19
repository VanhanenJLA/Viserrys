package viserrys.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    @Query(value = "SELECT * FROM Account", nativeQuery = true)
    List<Account> findAllAccounts();

    @Transactional
    @Modifying
    @Query(value="DROP TABLE Account", nativeQuery=true)
    void dropAccount();


}

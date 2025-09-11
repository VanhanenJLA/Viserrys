package viserrys.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import viserrys.account.Account;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
  List<Reaction> findTop5ByReactibleId(long id);
  Reaction findBySenderAndReactibleId(Account sender, Reaction r);
  Reaction findBySenderAndReactibleIdAndReactionType(Account sender, long reactibleId, ReactionType rt);
  int countReactionsByReactibleIdAndReactionType(long reactibleId, ReactionType rt);

  List<Reaction> findAllBySenderAndReactibleId(Account sender, long reactibleId);

  @Query("SELECT r.reactibleId" +
          ", r.reactionType" +
          ", COUNT(r) " +
          "FROM Reaction r " +
          "WHERE r.reactibleId " +
          "IN :reactibleIds " +
          "GROUP BY r.reactibleId, r.reactionType")
  List<Object[]> countReactionsByPhotoIds(@Param("reactibleIds") List<Long> reactibleIds);

  @Query("SELECT r.reactibleId, r.reactionType " +
          "FROM Reaction r " +
          "WHERE r.reactibleId IN :reactibleIds " +
          "AND r.sender = :sender")
  List<Object[]> findReactionsBySenderAndPhotoIds(@Param("sender") Account sender,
                                                  @Param("reactibleIds") List<Long> reactibleIds);

}

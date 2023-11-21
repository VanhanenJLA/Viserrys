package viserrys.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;
import viserrys.photo.Photo;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
  Reaction findBySenderAndTarget(Account sender, Photo target);
  Reaction findBySenderAndTargetAndReactionType(Account sender, Photo target, ReactionType reactionType);
  int countReactionsByTargetAndReactionType(Photo target, ReactionType reactionType);
}

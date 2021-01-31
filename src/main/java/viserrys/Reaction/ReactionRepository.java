package viserrys.Reaction;

import org.springframework.data.jpa.repository.JpaRepository;

import viserrys.Account.Account;
import viserrys.Photo.Photo;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
  Reaction findBySenderAndTarget(Account sender, Photo target);
  Reaction findBySenderAndTargetAndReactionType(Account sender, Photo target, ReactionType reactionType);
}

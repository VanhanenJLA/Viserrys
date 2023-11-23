package viserrys.reaction;

import org.springframework.stereotype.Service;
import viserrys.account.Account;
import viserrys.photo.Photo;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReactionService {
  final ReactionRepository reactionRepository;
  public ReactionService(ReactionRepository reactionRepository) {
    this.reactionRepository = reactionRepository;
  }

  public Reaction react(Account sender, Photo target, Instant timestamp, ReactionType reactionType) {

    var reaction = reactionRepository.findBySenderAndTargetAndReactionType(sender, target, reactionType);
    if (reaction != null) {
      reactionRepository.delete(reaction);
      return null;
    }
    return reactionRepository.save(new Reaction(sender, target, timestamp, reactionType));
  }

  public List<Reaction> getLatestReactions(int n, Photo target) {
    // TODO: Limit on DB level
    return reactionRepository.findAll().stream().filter(r -> r.target == target).limit(n).collect(Collectors.toList());
  }

  public boolean hasReacted(Account sender, Photo target, ReactionType reactionType) {
    return reactionRepository.findBySenderAndTargetAndReactionType(sender, target, reactionType) != null;
  }

  public int countReactions(Photo target, ReactionType reactionType) {
    return reactionRepository
            .countReactionsByTargetAndReactionType(target, reactionType);
  }

}

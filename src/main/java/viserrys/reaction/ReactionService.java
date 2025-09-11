package viserrys.reaction;

import org.springframework.stereotype.Service;
import viserrys.account.Account;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReactionService {
  final ReactionRepository reactionRepository;
  public ReactionService(ReactionRepository reactionRepository) {
    this.reactionRepository = reactionRepository;
  }

  public Reaction react(Account sender, Reactible r, Instant timestamp, ReactionType reactionType) {

    var reaction = reactionRepository.findBySenderAndReactibleIdAndReactionType(sender, r.getId(), reactionType);
    if (reaction != null) {
      reactionRepository.delete(reaction);
      return null;
    }
    
    var uus = Reaction.builder().sender(sender).reactibleId(r.getId()).reactionType(reactionType).build();
    return reactionRepository.save(uus);
  }

  public List<Reaction> getLatestReactions(int n, Reactible r) {
    return reactionRepository.findTop5ByReactibleId(r.getId());
  }
  
  public List<Reaction> getReactionsBySenderAndReactible(Account sender, Reactible r) { 
    return reactionRepository.findAllBySenderAndReactibleId(sender, r.getId());
  }
  
  public Map<Long, String> getReactions(Account sender, List<Long> ids) {
    return reactionRepository
            .findReactionsBySenderAndPhotoIds(sender, ids)
            .stream()
            .collect(Collectors.toMap(
                    obj -> (Long) obj[0],          // Key: reactibleId
                    obj -> (String) obj[1]         // Value: reactionType
            ));
  }

  public Map<Long, Map<ReactionType, Long>> getReactionCountsBy(List<Long> ids) {
    List<Object[]> results = reactionRepository.countReactionsByPhotoIds(ids);
    Map<Long, Map<ReactionType, Long>> reactionCounts = new HashMap<>();

    for (Object[] result : results) {
      Long photoId = (Long) result[0];
      ReactionType reactionType = (ReactionType) result[1];
      Long count = (Long) result[2];

      reactionCounts
              .computeIfAbsent(photoId, id -> new HashMap<>())
              .put(reactionType, count);
    }
    return reactionCounts;
  }

}

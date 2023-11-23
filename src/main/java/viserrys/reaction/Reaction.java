package viserrys.reaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.photo.Photo;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Reaction extends AbstractPersistable<Long> {

  @NotNull
  @ManyToOne
  Account sender;

  @NotNull
  @ManyToOne
  Photo target;

  Instant timestamp;

  @Enumerated(EnumType.STRING)
  ReactionType reactionType;

}

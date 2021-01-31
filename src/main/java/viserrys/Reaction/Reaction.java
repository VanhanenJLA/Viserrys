package viserrys.Reaction;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;
import viserrys.Photo.Photo;

import org.springframework.data.jpa.domain.AbstractPersistable;

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

  LocalDateTime timestamp;

  @Enumerated(EnumType.STRING)
  ReactionType reactionType;

}

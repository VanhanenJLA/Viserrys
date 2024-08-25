package viserrys.reaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.photo.Photo;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Reaction extends AbstractPersistable<Long> {

  @NotNull
  @OneToOne
  Account sender;

  @NotNull
  @OneToOne
  Photo target;

  Instant timestamp;

  @Enumerated(EnumType.STRING)
  ReactionType reactionType;

}

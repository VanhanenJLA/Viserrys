package viserrys.reaction;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.photo.Photo;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Reaction extends AbstractPersistable<Long> {

  @NotNull
  @OneToOne
  Account sender;

  @NotNull
  private Long reactibleId;

  @NotNull
  private String reactibleType;

  @NotNull
  @Builder.Default
  Instant timestamp = Instant.now();

  @Enumerated(EnumType.STRING)
  ReactionType reactionType;

}

package viserrys.Reaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.Account.Account;
import viserrys.Photo.Photo;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

package viserrys.follow;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Follow extends AbstractPersistable<Long> {

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account recipient;

    private Instant timestamp;

}

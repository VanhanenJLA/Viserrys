package viserrys.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.photo.Photo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Comment extends AbstractPersistable<Long> implements Comparable<Comment> {

    @NotNull
    @ManyToOne
    private Account sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Photo target;

    @NotNull
    private Instant timestamp;

    @Lob
    @NotEmpty
    @Size(min = 1, max = 150)
    private String content;
    
    @Override
    public int compareTo(Comment o) {
        return o.timestamp.compareTo(this.timestamp);
    }
}

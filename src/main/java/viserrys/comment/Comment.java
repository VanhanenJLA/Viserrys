package viserrys.comment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.photo.Photo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends AbstractPersistable<Long> implements Comparable<Comment> {

    @NotNull
    @ManyToOne
    private Account sender;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Photo target;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "commentable_id")
    BaseCommentable commentable;

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

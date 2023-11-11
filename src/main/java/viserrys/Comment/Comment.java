package viserrys.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.Account.Account;
import viserrys.Photo.Photo;

import jakarta.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Comment extends AbstractPersistable<Long> implements Comparable<Comment> {

    @NotNull
    @ManyToOne
    private Account sender;

    @NotNull
    @ManyToOne
    private Photo target;

    @NotNull
    private LocalDateTime timestamp;

    @Lob // Doesn't work for Heroku.
    @Column
    @NotEmpty
    @Size(min = 1, max = 150)
    private String content;

    @Override
    public int compareTo(Comment o) {
        if (timestamp.isAfter(o.timestamp))
            return -1;

        if (timestamp.isBefore(o.timestamp))
            return 1;

        return 0;
    }
}

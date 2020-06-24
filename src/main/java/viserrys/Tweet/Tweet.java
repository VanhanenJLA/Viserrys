package viserrys.Tweet;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet extends AbstractPersistable<Long> implements Comparable<Tweet> {

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account recipient;

    private LocalDateTime timestamp;

    @Lob // Doesn't work for Heroku.
    @Column
    @NotEmpty
    @Size(min = 1, max = 150)
    private String content;

    @Override
    public int compareTo(Tweet o) {
        if (this.getTimestamp().isAfter(o.getTimestamp()))
            return -1;

        if (this.getTimestamp().isBefore(o.getTimestamp()))
            return 1;

        return 0;

    }
}

package viserrys.tweet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Tweet extends AbstractPersistable<Long> implements Comparable<Tweet> {

    @NotNull
    @ManyToOne
    private Account sender;
    
    @NotNull
    @ManyToOne
    private Account recipient;
    
    private Instant timestamp;
    
    @Lob
    @Column
    @NotEmpty
    @Size(min = 1, max = 150)
    private String content;

    @Override
    public int compareTo(Tweet o) {
        if (timestamp.isAfter(o.timestamp))
            return -1;

        if (timestamp.isBefore(o.timestamp))
            return 1;

        return 0;
    }
}

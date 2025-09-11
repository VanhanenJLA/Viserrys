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
public class Comment extends AbstractPersistable<Long> {

    @NotNull
    @ManyToOne
    private Account sender;

    @NotNull
    private Long commentableId;

    @NotNull
    private String commentableType;

    @NotNull
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Lob
    @NotEmpty
    @Size(min = 1, max = 150)
    private String content;
    
}

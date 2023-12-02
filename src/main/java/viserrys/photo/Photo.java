package viserrys.photo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.comment.Comment;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

import static viserrys.common.Constants.MB;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends AbstractPersistable<Long> {
    
    @OneToOne
    Account uploader;

    @Length(max = 500)
    String description;

    @NotNull
    private Instant timestamp;

    @Lob
    @Size(max = 5 * MB)
    byte[] content;

//    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
//    @OrderBy("timestamp DESC")
//    List<Comment> comments = null;
    
}



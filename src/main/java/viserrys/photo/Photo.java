package viserrys.photo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.comment.Comment;
import viserrys.comment.Commentable;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import static viserrys.common.Constants.MB;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends AbstractPersistable<Long> implements Commentable {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "uploader_id")
    private Account uploader;
    
    @NotNull
    @Length(max = 500)
    String description;

    @NotNull
    private Instant timestamp;

    @Lob
    @NotNull
    @Size(max = MB)
    byte[] content;
    
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    @OneToMany(mappedBy = "target")
    @OrderBy("timestamp DESC")
    List<Comment> comments = null;
    
}



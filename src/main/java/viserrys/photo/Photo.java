package viserrys.photo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Lazy;
import viserrys.account.Account;
import viserrys.comment.BaseCommentable;
import viserrys.comment.Comment;

import java.time.Instant;
import java.util.List;

import static viserrys.common.Constants.MB;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends BaseCommentable {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "uploader_id")
    private Account uploader;
    
    @NotNull
    @Size(min = 1)
    @Length(max = 500)
    String description;

    @NotNull
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Lob
    @NotNull
    @Size(max = MB)
    byte[] content;
    
    @NotNull
    @Lazy
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    @OneToMany(mappedBy = "commentable")
    @OrderBy("timestamp DESC")
    @Builder.Default
    List<Comment> comments = List.of();
    
}
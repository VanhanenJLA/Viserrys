package viserrys.photo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;
import viserrys.comment.Comment;
import viserrys.reaction.Reaction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    Account uploader;

    @Length(max = 500)
    String description;

    @Lob
    byte[] content;

    @Cascade(CascadeType.ALL)
    @OrderBy("timestamp DESC")
    @OneToMany(mappedBy = "target")
    List<Comment> comments = new ArrayList<>();

    // @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "target")
    List<Reaction> reactions = new ArrayList<>();

}

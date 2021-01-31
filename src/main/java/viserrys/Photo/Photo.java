
package viserrys.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;
import viserrys.Comment.Comment;
import viserrys.Reaction.Reaction;
import viserrys.Reaction.ReactionType;

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

    @OneToMany(mappedBy = "target")
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "target")
    List<Reaction> reactions = new ArrayList<>();

}

package viserrys.photo;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;

import java.time.Instant;

import static viserrys.common.Constants.MB;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private Account uploader;
    
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



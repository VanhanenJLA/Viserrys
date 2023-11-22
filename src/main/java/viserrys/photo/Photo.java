package viserrys.photo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.account.Account;

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

    @Lob
    @Size(max = 5 * MB)
    byte[] content;

}



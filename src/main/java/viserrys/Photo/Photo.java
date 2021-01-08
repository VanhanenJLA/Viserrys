
package viserrys.Photo;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Photo extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account uploader;

    private String description;

    @Lob
    private byte[] content;

}

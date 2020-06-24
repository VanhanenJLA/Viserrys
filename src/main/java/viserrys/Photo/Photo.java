
package viserrys.Photo;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends AbstractPersistable<Long> {

    private String description;

    @Lob
    private byte[] content;

    public String toDecodedString(byte[] content) {
        return Base64.getMimeDecoder().decode(content).toString();
    }

}

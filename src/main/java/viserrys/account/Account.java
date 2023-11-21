package viserrys.account;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.photo.Photo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractPersistable<Long> {

    @NotEmpty
    @Column(unique = true)
    @Size(min = 3, max = 15)
    String username;

    @NotEmpty
    String password;

    @OneToOne
    Photo profilePicture;

}

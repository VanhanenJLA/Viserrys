package viserrys.Account;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Photo.Photo;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractPersistable<Long> {

    @NotEmpty
    @Column(unique = true)
    @Size(min = 3, max = 12)
    private String username;

    @NotEmpty
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Account> following = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Account> followers = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Photo> photos = new ArrayList<>();

    // @OneToMany
    // private List<Tweet> tweets = new ArrayList<>();

    public Photo getProfilePicture() {
        if (photos.size() < 1)
            return null;
        return photos.get(0);
    }

}

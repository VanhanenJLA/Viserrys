package viserrys.Account;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Follow.Follow;
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
    @Size(min = 3, max = 15)
    private String username;

    @NotEmpty
    private String password;

    @ManyToMany(mappedBy = "sender")
    private List<Follow> following = new ArrayList<>();

    @ManyToMany(mappedBy = "recipient")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "uploader")
    private List<Photo> photos = new ArrayList<>();

    @OneToOne
    public Photo profilePicture;

    public boolean isFollowing(Account recipient) {
        return following.stream().anyMatch(a -> a.getRecipient() == recipient);
    }

    public boolean hasProfilePicture() {
        return profilePicture != null;
    }

}

package viserrys.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.follow.Follow;
import viserrys.photo.Photo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractPersistable<Long> {

    @NotEmpty
    @Column(unique = true)
    @Size(min = 3, max = 15)
    String username;

    @NotEmpty
    String password;

    @ManyToMany(mappedBy = "sender")
    List<Follow> following = new ArrayList<>();

    @ManyToMany(mappedBy = "recipient")
    List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "uploader")
    List<Photo> photos = new ArrayList<>();

    @OneToOne
    Photo profilePicture;

    public boolean isFollowing(Account recipient) {
        return following.stream().anyMatch(a -> a.getRecipient() == recipient);
    }

    public boolean hasProfilePicture() {
        return profilePicture != null;
    }

}

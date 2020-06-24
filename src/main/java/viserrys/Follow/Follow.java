
package viserrys.Follow;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow extends AbstractPersistable<Long> {

    @OneToOne(cascade = {CascadeType.ALL})
    private Account sender;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private Account recipient;

    private LocalDateTime timestamp; 
    
}

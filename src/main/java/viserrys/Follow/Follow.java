
package viserrys.Follow;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import viserrys.Account.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Follow extends AbstractPersistable<Long> {

    @ManyToOne
    private Account sender;
    
    @ManyToOne
    private Account recipient;

    private LocalDateTime timestamp; 
    
}

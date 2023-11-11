
package viserrys.Follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import viserrys.Account.Account;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

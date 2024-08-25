package viserrys;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import viserrys.account.Account;
import viserrys.account.AccountRepository;
import viserrys.photo.Photo;
import viserrys.photo.PhotoRepository;

import java.nio.file.Files;
import java.time.Instant;
import java.util.List;

@Profile("SEED")
@Configuration
public class DataSeeder {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoRepository photoRepository;

    public DataSeeder(AccountRepository accountRepository, PasswordEncoder passwordEncoder, PhotoRepository photoRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.photoRepository = photoRepository;
    }

    @Bean
    CommandLineRunner populateAccounts() {
        return args -> {
            var password = passwordEncoder.encode("salasana");
            var Jouni = new Account("Jouni", password, null, null);
            var Lauri = new Account("Lauri", password, null, null);
            var Aleksi = new Account("Aleksi", password, null, null);
            accountRepository.saveAll(List.of(Jouni, Lauri, Aleksi));
        };
    }

    @Bean
    CommandLineRunner populatePhotos() {
        return args -> {
            var Jouni = accountRepository.findByUsername("Jouni").get();
            var res = new ClassPathResource("static/img/avatars/5.png");
            var bytes = Files.readAllBytes(res.getFile().toPath());
            var photo = new Photo(Jouni, "A clever description.", Instant.now(), bytes);
            photoRepository.save(photo);
        };
    }

}

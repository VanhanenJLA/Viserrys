package viserrys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import viserrys.account.Account;
import viserrys.account.AccountRepository;
import viserrys.photo.Photo;
import viserrys.photo.PhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Profile("SEED")
@Configuration
public class DataSeeder {

    private final ApplicationContext applicationContext;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoRepository photoRepository;
    
    public DataSeeder(ApplicationContext applicationContext,
                      AccountRepository accountRepository,
                      PasswordEncoder passwordEncoder,
                      PhotoRepository photoRepository) {
        this.applicationContext = applicationContext;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.photoRepository = photoRepository;
    }
    
    @Bean
    @Profile("WIPE")
    CommandLineRunner wipeDatabase() {
        return args -> {
            log.info("Wiping database...");
            var beanNames = applicationContext.getBeanNamesForType(JpaRepository.class);
            for (var name : beanNames) {
                var repository = (JpaRepository<?, ?>) applicationContext.getBean(name);
                log.info("Wiping repository '{}'", name);
                repository.deleteAll();
            }
            log.info("Database wiped.");
        };
    }

    @Bean
    @Order(1)
    CommandLineRunner populateAccounts() {
        return args -> {
            log.info("Populating accounts...");
            var password = passwordEncoder.encode("salasana");
            var Jouni = new Account("Jouni", password, null, null);
            var Lauri = new Account("Lauri", password, null, null);
            var Aleksi = new Account("Aleksi", password, null, null);
            accountRepository.saveAll(List.of(Jouni, Lauri, Aleksi));
            log.info("Accounts populated");
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner populatePhotos() {
        return args -> {
            log.info("Populating photos...");
            var Jouni = accountRepository.findByUsername("Jouni").orElseThrow();
            var dir = new ClassPathResource("static/img/seed/jouni").getFile();

            var photos = dir.listFiles((f, name) -> name.toLowerCase().endsWith(".webp"));

            var kuvat = Arrays
                    .stream(photos)
                    .map(file -> {
                        try {
                            return Files.readAllBytes(file.toPath());
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            return new byte[0];
                        }
                    })
                    .filter(bytes -> bytes.length > 0)
                    .map(bytes -> new Photo(Jouni, "🖼", Instant.now(), bytes))
                    .toList();

            photoRepository.saveAll(kuvat);
            log.info("Photos populated");
        };
    }
}



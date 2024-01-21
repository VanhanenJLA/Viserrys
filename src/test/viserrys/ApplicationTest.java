package viserrys;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import viserrys.account.Account;
import viserrys.account.AccountRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;
    
    private Account jouni = new Account("Jouni", "password", null, null);

    @LocalServerPort
    private int port;

    @Autowired
    private WebClient.Builder builder;
    private String baseUrl;

    @BeforeEach
    public void setup() {
       baseUrl = "http://localhost:" + port;
    }

    @Test
    public void should_render_login() {
        var url = baseUrl + "/login";
        var response = builder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Assertions.assertTrue(response.contains("Please sign in"));
    }
    
    @Test
    void account_registration_works() throws Exception {
        var url = baseUrl + "/register";

        builder.build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jouni)
                .retrieve()
                .toBodilessEntity()
                .block();
        
        var account = accountRepository.findByUsername(jouni.getUsername()).orElse(null);
        Assertions.assertNotNull(account);
        assertThat(account.getUsername()).isEqualTo(jouni.getUsername());
    }

}
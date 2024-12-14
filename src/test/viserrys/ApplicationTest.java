package viserrys;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import viserrys.account.Account;
import viserrys.account.AccountRepository;

import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Autowired
    private AccountRepository accountRepository;

    private final Account jouni = new Account("Jouni", "password", null, null);

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
    void should_render_login() {
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
    void should_register_account() {
        var url = baseUrl + "/register";

        var formData = "username=" + jouni.getUsername() + "&password=" + jouni.getPassword();
        var response = builder.build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .toBodilessEntity()
                .block();

        var account = accountRepository
                .findByUsername(jouni.getUsername())
                .orElse(null);

        assertThat(account).isNotNull();
        assertThat(account.getUsername()).isEqualTo(jouni.getUsername());
    }
    
    @Test
    void should_login() {
        var url = baseUrl + "/login";

        var formData = "username=" + jouni.getUsername() + "&password=" + jouni.getPassword();
        var response = builder.build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .toBodilessEntity()
                .block();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
    }

    @Test
    void should_not_login() {
        var url = baseUrl + "/login";

        var formData = "username=" + jouni.getUsername() + "&password=" + "wrong";
        var response = builder.build()
                              .post()
                              .uri(url)
                              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                              .bodyValue(formData)
                              .retrieve()
                              .toEntity(String.class)
                              .block();
        assertThat(response).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaders().getLocation())
                .hasPath("/login")  // Redirects back to the login page
                .hasParameter("error", "true");  // Error query parameter added to indicate failure
    }

    @Test
    void should_follow() {
        
    }

}
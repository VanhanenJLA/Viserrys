package viserrys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import viserrys.account.Account;
import viserrys.account.AccountRepository;
import viserrys.account.AccountService;
import viserrys.common.Constants;
import viserrys.common.Exceptions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    private final Account jouni = new Account("Jouni", "password", null, null);

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;
    private String baseUrl;
    @Autowired
    private AccountService accountService;

    private static void NoOp(Account a) {}
    
    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
        accountRepository
                .findByUsername(jouni.getUsername())
                .ifPresentOrElse(AccountIntegrationTest::NoOp, 
                        () -> accountService.createAccount(jouni.getUsername(), jouni.getPassword()));
    }

    @Test
    void should_render_login() {
        var url = baseUrl + "/login";
        var response = client
                .get()
                .uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("Please sign in");
    }

    @Test
    void should_register_account() {
        var url = baseUrl + "/register";

        var formData = "username=" + jouni.getUsername() + "&password=" + jouni.getPassword();
        var response = client
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

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
        var loginResponse = client
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectBody(String.class)
                .returnResult();
        
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getResponseHeaders().getLocation().getPath()).isEqualTo("/me");

        var cookie = loginResponse.getResponseHeaders().get(HttpHeaders.SET_COOKIE).getFirst();
        var sessionId = cookie.split(";", 2)[0];

        var meResponse = client
                .get()
                .uri(baseUrl + "/me")
                .header(HttpHeaders.COOKIE, sessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody(String.class)
                .returnResult();

        assertThat(meResponse).isNotNull();
        assertThat(meResponse.getResponseHeaders().getLocation().getPath()).isEqualTo("/accounts/Jouni");

        var accountsResponse = client
                .get()
                .uri(baseUrl + "/accounts/Jouni")
                .header(HttpHeaders.COOKIE, sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(accountsResponse.getResponseBody()).contains("Jouni&#39;s profile");
    }

    @Test
    void should_not_login() {
        var url = baseUrl + "/login";
        var formData = "username=" + jouni.getUsername() + "&password=" + "wrong";

        var response = client
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login")
                .expectBody(String.class)
                .returnResult();

//        assertThat(queryParams).hasFieldOrProperty("error"); // Ensure "error" exists
//        assertThat(queryParams.get("error")).isEqualTo(Exceptions.BAD_CREDENTIALS); // Validate its value
    }

}
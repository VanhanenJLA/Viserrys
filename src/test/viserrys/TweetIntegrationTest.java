package viserrys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import viserrys.account.Account;
import viserrys.account.AccountRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TweetIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private WebClient.Builder builder;
    private String baseUrl;
    
    private final Account jouni = new Account("jouni", "password", null, null);

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;
        // Make sure the account exists for the test
//        accountRepository.deleteAll();
        accountRepository
                .findByUsername("jouni")
                .ifPresentOrElse(a -> {}, () -> accountRepository.save(jouni));
    }

    @Test
    void should_tweet_authenticated() {
        // 1. Login to get authenticated session cookie
        String sessionCookie = loginAndGetSessionCookie(jouni.getUsername(), jouni.getPassword());
        assertThat(sessionCookie).isNotBlank();

        // 2. Perform the tweet request using the authenticated session
        var username = jouni.getUsername();
        var tweetUrl = baseUrl + "/accounts/" + username + "/tweet";
        var formData = "content=Hello+World";

        var response = builder
                .build()
                .post()
                .uri(tweetUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.COOKIE, sessionCookie)
                .bodyValue(formData)
                .retrieve()
                .toBodilessEntity()
                .block();

        // 3. Verify the response
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        var location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.getPath()).isEqualTo("/accounts/" + username);

        // Optionally, verify that the tweet was actually created if you have a TweetRepository or similar.
    }

    //    @Test
//    void should_tweet() {
//        var username = jouni.getUsername();
//        var url = baseUrl + "/accounts/" + username + "/tweet";
//        var formData = "content=Hello+World";
//
//        var response = builder.build()
//                .post()
//                .uri(url)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .bodyValue(formData)
//                .retrieve()
//                .toEntity(String.class)
//                .block();
//
//        // Check that the request was handled correctly
//        assertThat(response).isNotNull();
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // Expecting a redirect
//
//        // Verify the redirect location
//        var location = response.getHeaders().getLocation();
//        assertThat(location).isNotNull();
//        assertThat(location.getPath()).isEqualTo("/accounts/" + username);
//
//        // If desired, you could also verify that the tweet has been created
//        // For example, if there's a TweetRepository, you can verify the tweet's existence:
//        // var tweet = tweetRepository.findByContent("Hello World");
//        // assertThat(tweet).isNotNull();
//        // assertThat(tweet.getSender().getUsername()).isEqualTo(jouni.getUsername());
//        // assertThat(tweet.getRecipient().getUsername()).isEqualTo(username);
//    }

    /**
     * Performs a login request to /login with given username and password,
     * returns the session cookie from the successful login response.
     */
    private String loginAndGetSessionCookie(String username, String password) {
        var loginUrl = baseUrl + "/login";
        var formData = "username=" + username + "&password=" + password;

        var response = builder.build()
                .post()
                .uri(loginUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is3xxRedirection()) {
                        // Extract cookie from headers
                        var headers = clientResponse.headers().asHttpHeaders();
                        var cookies = headers.get(HttpHeaders.SET_COOKIE);
                        if (cookies != null && !cookies.isEmpty()) {
                            // Typically JSESSIONID; adjust if you're using a different session cookie name
                            return clientResponse.releaseBody().thenReturn(cookies.get(0));
                        }
                    }
                    return clientResponse.releaseBody().thenReturn("");
                })
                .block();

        return response;
    }
}

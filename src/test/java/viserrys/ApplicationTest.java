package viserrys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import viserrys.Account.Account;
import viserrys.Account.AccountService;
import viserrys.Auth.AuthService;
import viserrys.Photo.PhotoService;

@RunWith(SpringRunner.class)
@SpringBootTest
// @ActiveProfiles("test")
public class ApplicationTest {

  @Autowired
  private TestService testService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private AuthService authService;

  @Autowired
  private PhotoService photoService;

  @Before
  public void init() {
    // testService.clearDatabase();
    // testService.createTesters();
  }

  @Test
  public void accountCreation() {

    try {
      Account jouni = testService.jouni;
      Account a = accountService.createAccount(jouni);
      assertEquals(jouni.getUsername(), a.getUsername());
      assertEquals(jouni.getProfileHandle(), a.getProfileHandle());

    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void follow() {

    try {
      Account juoni = accountService.createAccount("Juoni", "salasana", "VanhanenJLU");
      Account jauni = accountService.createAccount("Jauni", "salasana", "VanhanenJLO");
      accountService.follow(juoni, jauni);

      assertTrue("Juoni following contains Jauni", accountService.getAccount("Juoni").getFollowing().stream()
          .anyMatch(account -> account.getUsername() == "Jauni"));

      assertTrue("Jauno followers contains Juoni", accountService.getAccount("Jauni").getFollowers().stream()
          .anyMatch(account -> account.getUsername() == "Juoni"));

    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void uploadPhoto() {
    // photoService.uploadPhoto("Testi avatar.", testService.avatarInBase64(),
    // accountService.getAccount("Jouni"));
  }

  @Test
  public void login() {

    try {
      Account j = testService.jouni;
      authService.login(j.getUsername(), j.getPassword());
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }


}

package viserrys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import viserrys.Account.Account;
import viserrys.Account.AccountService;
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
  private PhotoService photoService;

  @Before
  public void init() {

  }

  @Test
  public void initDatabase() throws Exception {

    accountService.createAccount("Jouni", "salasana");
    accountService.createAccount("Jauni", "salasana");
    accountService.createAccount("Jaoni", "salasana");
    accountService.createAccount("Jooni", "salasana");
    accountService.createAccount("Jyoni", "salasana");

    var accounts = accountService.getAccounts();

    accounts.forEach(a -> {
      try {
        uploadPhotos(a);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

  }

  @Test
  public void setProfilePictures() {
    var accounts = accountService.getAccounts();
    for (int i = 1; i < 6; i++) {
      var a = accounts.get(i);
      var p = a.getPhotos().get(i);
      accountService.setProfilePicture(a, p.getId());
    }
  }

  void uploadPhotos(Account account) throws Exception {

    for (int i = 1; i < 6; i++) {

      // var path =
      // "C:\\Repos\\VanhanenJLA\\Viserrys\\src\\main\\resources\\static\\img\\avatars\\"
      // + i + "png";
      var path = "C:/Repos/VanhanenJLA/Viserrys/src/main/resources/static/img/avatars/" + i + ".png";

      var image = ImageIO.read(new File(path));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      baos.flush();
      var bytes = baos.toByteArray();
      image.flush();
      baos.close();

      var mp = new MockMultipartFile("eka", String.valueOf(i), "image/png", bytes);

      photoService.uploadPhoto(mp, "Test photo.", account);

    }

  }

  @Test
  public void clearDatabase() {
    try {
      var db = new File("C:\\Repos\\VanhanenJLA\\Viserrys\\database.mv.db");
      var success = db.delete();
      assertTrue(success);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void accountCreation() {

    try {
      var a = accountService.createAccount("Jouni", "salasana");
      testService.createTesters();
      assertEquals(a.getUsername(), a.getUsername());
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void follow() {

    try {
      var juoni = accountService.createAccount("Juoni", "salasana");
      var jauni = accountService.createAccount("Jauni", "salasana");
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
  public void uploadPhoto() throws Exception {

    var jouni = accountService.getAccount("Jouni");

    var path = "C:\\Repos\\VanhanenJLA\\Viserrys\\src\\main\\resources\\static\\img\\avatars\\1.png";
    var image = ImageIO.read(new File(path));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, "png", baos);
    baos.flush();
    var bytes = baos.toByteArray();
    baos.close();

    var mp = new MockMultipartFile("eka", "1", "image/png", bytes);

    photoService.uploadPhoto(mp, "Test photo.", jouni);
    photoService.uploadPhoto(mp, "Test photo.", jouni);
    photoService.uploadPhoto(mp, "Test photo.", jouni);
    photoService.uploadPhoto(mp, "Test photo.", jouni);
    photoService.uploadPhoto(mp, "Test photo.", jouni);

  }

}

package viserrys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;

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
import viserrys.Follow.FollowService;
import viserrys.Photo.PhotoService;
import viserrys.Tweet.TweetService;

@RunWith(SpringRunner.class)
@SpringBootTest
// @ActiveProfiles("test")
public class ApplicationTest {

  @Autowired
  TestService testService;

  @Autowired
  AccountService accountService;

  @Autowired
  FollowService followService;

  @Autowired
  PhotoService photoService;

  @Autowired
  TweetService tweetService;

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

    for (Account account : accounts) {
      uploadPhotos(account.getUsername());
    }

    setProfilePictures();

    followService.follow(jouni(), jauni());
    followService.follow(jauni(), jouni());

    var msg = "InitDB populated tweet.";
    tweetService.tweet(jouni(), jouni(), LocalDateTime.now(), msg);
    tweetService.tweet(jauni(), jouni(), LocalDateTime.now(), msg);

  }

  @Test
  public void setProfilePictures() {
    var accounts = accountService.getAccounts();
    for (int i = 0; i < 5; i++) {
      var a = accounts.get(i);
      var photos = photoService.findAllByUploader(a);
      var p = photos.get(i);
      accountService.setProfilePicture(a, p.getId());
    }
  }

  void uploadPhotos(String username) throws Exception {

    for (int i = 1; i < 6; i++) {
      var account = accountService.getAccount(username);
      var path = "C:/Repos/VanhanenJLA/Viserrys/src/main/resources/static/img/avatars/" + i + ".png";

      var image = ImageIO.read(new File(path));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      baos.flush();
      var bytes = baos.toByteArray();
      image.flush();
      baos.close();

      var mp = new MockMultipartFile("eka", String.valueOf(i), "image/png", bytes);
      var description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

      photoService.uploadPhoto(mp, description, account);

    }

  }

  Account jouni() {
    return accountService.getAccount("Jouni");
  }

  Account jauni() {
    return accountService.getAccount("Jauni");
  }

}

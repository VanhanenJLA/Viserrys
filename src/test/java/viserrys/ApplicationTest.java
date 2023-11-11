//package viserrys;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import viserrys.Account.Account;
//import viserrys.Account.AccountService;
//import viserrys.Comment.Comment;
//import viserrys.Comment.CommentRepository;
//import viserrys.Follow.FollowService;
//import viserrys.Photo.Photo;
//import viserrys.Photo.PhotoService;
//import viserrys.Reaction.ReactionService;
//import viserrys.Reaction.ReactionType;
//import viserrys.Tweet.TweetService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//// @ActiveProfiles("test")
//public class ApplicationTest {
//
//  @Autowired
//  AccountService accountService;
//
//  @Autowired
//  FollowService followService;
//
//  @Autowired
//  PhotoService photoService;
//
//  @Autowired
//  TweetService tweetService;
//
//  @Autowired
//  ReactionService reactionService;
//
//  @Autowired
//  CommentRepository commentRepository;
//
//  @Autowired
//  EntityManager em;
//
//  @Test
//  @Transactional
//  public void nukeDb() {
//    em.joinTransaction();
//    em.createNativeQuery("DROP ALL OBJECTS DELETE FILES").executeUpdate();
//    em.close();
//  }
//
//  @Test
//  @Transactional
//  @Rollback(false)
//  public void populateDatabase() throws Exception {
//
//    createAccounts();
//
//    for (Account account : accountService.getAccounts()) {
//      uploadPhotos(account.getUsername());
//    }
//
//    setProfilePictures();
//    createTweets();
//    setFollows();
//    // setComments();
//  }
//
//  @Transactional
//  @Rollback(false)
//  @Test
//  public void react() throws Exception {
//    var photo = photoService.photoRepository.findAll().get(0);
//    var reaction = reactionService.react(jouni(), photo, LocalDateTime.now(), ReactionType.SAD);
//    System.out.println("Bubuntu");
//  }
//
//  public void dropTables() {
//    List.of("Account", "Tweet", "Photo", "Follow").forEach(t -> dropTable(t));
//  }
//
//  @Transactional
//  @Rollback(false)
//  @Test
//  public void setComments() {
//    for (Account account : accountService.getAccounts()) {
//      var j = jouni();
//      var p = j.getPhotos();
//      comment(account, p.get(0));
//    }
//  }
//
//  public void comment(Account sender, Photo photo) {
//    var comment = commentRepository.save(new Comment(sender, photo, LocalDateTime.now(), "Haha very lit! 😂👌"));
//    System.out.println("");
//    // photo.getComments().add(comment);
//    // photoService.photoRepository.save(photo);
//  }
//
//  @Transactional
//  void dropTable(String tableName) {
//    em.joinTransaction();
//    String query = "DROP TABLE IF EXISTS " + tableName;
//    em.createNativeQuery(query).executeUpdate();
//  }
//
//  public void setProfilePictures() {
//    var accounts = accountService.getAccounts();
//    for (int i = 0; i < 5; i++) {
//      var a = accounts.get(i);
//      var photos = photoService.photoRepository.findAllByUploader(a);
//      var p = photos.get(i);
//      accountService.setProfilePicture(a, p.getId());
//    }
//  }
//
//  void uploadPhotos(String username) throws Exception {
//
//    for (int i = 1; i < 6; i++) {
//
//      var photoPath = "C:/Repos/VanhanenJLA/Viserrys/src/main/resources/static/img/avatars/" + i + ".png";
//      var bytes = getImageFileBytes(photoPath);
//
//      var mp = new MockMultipartFile("eka", String.valueOf(i), "image/png", bytes);
//      var description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//      var account = accountService.getAccount(username);
//
//      photoService.uploadPhoto(mp, description, account);
//
//    }
//
//  }
//
//  byte[] getImageFileBytes(String path) throws IOException {
//    var image = ImageIO.read(new File(path));
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    ImageIO.write(image, "png", baos);
//    baos.flush();
//    var bytes = baos.toByteArray();
//    image.flush();
//    baos.close();
//    return bytes;
//  }
//
//  void setFollows() throws Exception {
//    followService.follow(jouni(), jauni());
//    followService.follow(jauni(), jouni());
//    followService.follow(jaoni(), jouni());
//    followService.follow(jaoni(), jauni());
//  }
//
//  void createTweets() {
//    var msg = "Hello world from application tests.";
//    tweetService.tweet(jouni(), jouni(), LocalDateTime.now(), msg);
//    tweetService.tweet(jauni(), jouni(), LocalDateTime.now(), msg);
//    tweetService.tweet(jaoni(), jouni(), LocalDateTime.now(), msg);
//
//  }
//
//  void createAccounts() throws Exception {
//    accountService.createAccount("Jouni", "salasana");
//    accountService.createAccount("Jauni", "salasana");
//    accountService.createAccount("Jaoni", "salasana");
//    accountService.createAccount("Jooni", "salasana");
//    accountService.createAccount("Jyoni", "salasana");
//  }
//
//  Account jouni() {
//    return accountService.getAccount("Jouni");
//  }
//
//  Account jauni() {
//    return accountService.getAccount("Jauni");
//  }
//
//  Account jaoni() {
//    return accountService.getAccount("Jaoni");
//  }
//
//}

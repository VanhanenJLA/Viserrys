package viserrys.Tweet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TweetController {

  @Autowired TweetRepository TweetRepository;

  @PostMapping("/tweet")
  private Tweet tweet(@ModelAttribute Tweet tweet) {
      return TweetRepository.save(tweet);
  }

}

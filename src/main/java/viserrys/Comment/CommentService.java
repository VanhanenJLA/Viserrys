package viserrys.Comment;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;

@Service
public class CommentService {

  @Autowired
  CommentRepository commentRepository;

  public Comment comment(Account sender, LocalDateTime timestamp, String content) {
    return commentRepository.save(new Comment(sender, timestamp, content));
  }

  // public List<Comment> findAllByRecipient(Account recipient) {
  // return commentRepository.findAllByRecipient(recipient);
  // }

}

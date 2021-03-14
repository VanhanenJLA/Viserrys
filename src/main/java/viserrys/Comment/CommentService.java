package viserrys.Comment;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;
import viserrys.Photo.Photo;

@Service
public class CommentService {

  @Autowired
  CommentRepository commentRepository;

  public Comment comment(Account sender, Photo target, LocalDateTime timestamp, String content) {
    return commentRepository.save(new Comment(sender, target, timestamp, content));
  }

  public Page<Comment> findAllByTargetId(long targetId, int pageNumber, int pageSize) {
    Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending());
    return commentRepository.findAllByTargetId(targetId, paging);
  }

  // public List<Comment> findAllByRecipient(Account recipient) {
  // return commentRepository.findAllByRecipient(recipient);
  // }

}

package viserrys.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import viserrys.account.Account;
import viserrys.photo.Photo;

import java.time.Instant;

@Service
public class CommentService {

  final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public Comment comment(Account sender, Photo target, Instant timestamp, String content) {
    return commentRepository.save(new Comment(sender, target, timestamp, content));
  }

  public Page<Comment> findAllByTargetId(long targetId, int pageNumber, int pageSize) {
    Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending());
    return commentRepository.findAllByTargetId(targetId, paging);
  }

}

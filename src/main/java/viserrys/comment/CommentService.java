package viserrys.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import viserrys.account.Account;

@Service
public class CommentService {

  final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public Comment comment(Account sender, Commentable commentable, String content) {
    var c = Comment.builder()
            .sender(sender)
            .content(content)
            .commentableId(commentable.getId())
            .commentableType(commentable.getType())
            .build();
    
    return commentRepository.save(c);
  }

  public Page<Comment> findAllByTargetId(long targetId, int pageNumber, int pageSize) {
    Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending());
    return commentRepository.findAllByCommentableId(targetId, paging);
  }

}

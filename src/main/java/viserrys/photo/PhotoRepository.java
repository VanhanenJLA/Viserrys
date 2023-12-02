package viserrys.photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import viserrys.account.Account;
import viserrys.comment.Comment;


import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  int countPhotosByUploader(Account uploader);
  Page<Photo> findAllByUploader(Account uploader, Pageable pageable);
}


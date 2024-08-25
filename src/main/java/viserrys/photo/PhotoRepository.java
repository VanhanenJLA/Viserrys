package viserrys.photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  int countPhotosByUploader(Account uploader);
  Page<Photo> findAllByUploader(Account uploader, Pageable pageable);
}


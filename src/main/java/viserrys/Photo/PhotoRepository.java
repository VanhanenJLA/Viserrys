package viserrys.Photo;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.Account.Account;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  List<Photo> findAllByUploader(Account uploader);
}
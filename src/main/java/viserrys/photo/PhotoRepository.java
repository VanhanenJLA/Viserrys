package viserrys.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  List<Photo> findAllByUploader(Account uploader);
}
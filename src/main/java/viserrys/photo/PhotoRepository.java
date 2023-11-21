package viserrys.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  List<Photo> findAllByUploader(Account uploader);
  Optional<Photo> findById(Long id);
  
}
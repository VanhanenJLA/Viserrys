package viserrys.Photo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import viserrys.Account.Account;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  List<Photo> findAllByUploader(Account uploader);
}
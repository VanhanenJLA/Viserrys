package viserrys.photo;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import viserrys.account.Account;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
  Page<Photo> findAllByUploader(Account uploader, Pageable pageable);

  long countByUploader(@NotNull Account uploader);
}


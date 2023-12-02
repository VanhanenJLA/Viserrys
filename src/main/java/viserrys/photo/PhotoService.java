package viserrys.photo;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import viserrys.account.Account;

import java.time.Instant;

import static viserrys.photo.FileType.ensureSupportedFileType;

@Service
public class PhotoService {

    final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo uploadPhoto(MultipartFile file, String description, Account uploader) throws Exception {
        var type = file.getContentType();
        ensureSupportedFileType(type);
        var photo = new Photo(uploader, description, Instant.now(), file.getBytes());
        return photoRepository.save(photo);
    }

    @SneakyThrows
    public Photo getPhotoById(Long id) {
        return photoRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Photo not found: " + id));
    }

    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }

    public Page<Photo> findAllByUploader(Account uploader, Pageable pageable) {
        if (pageable == null)
            pageable = PageRequest.of(0, 5, Sort
                    .by("timestamp")
                    .descending());
        return photoRepository.findAllByUploader(uploader, pageable);
    }
}


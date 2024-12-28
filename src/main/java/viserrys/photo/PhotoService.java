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
import java.util.List;

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
        
        var photo = Photo.builder()
                .uploader(uploader)
                .description(description)
                .build();
        
        return photoRepository.save(photo);
    }

    public Photo getPhotoById(Long id) {
        return photoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found: " + id));
    }

    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }

    public Page<Photo> findAllByUploader(Account uploader, Pageable pageable) {
        return photoRepository.findAllByUploader(uploader, pageable);
    }

    public long countUploadedPhotos(Account account) {
        return photoRepository.countByUploader(account);
    }
}


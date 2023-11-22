package viserrys.photo;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import viserrys.account.Account;

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
        var photo = new Photo(uploader, description, file.getBytes());
        return photoRepository.save(photo);
    }

    @SneakyThrows
    public Photo getPhoto(Long id) {
        return photoRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Photo not found: " + id));
    }
    
    public void deleteBy(Long id) {
        photoRepository.deleteById(id);
    }

    public List<Photo> getPhotosByUploader(Account uploader) {
        return photoRepository.findAllByUploader(uploader);
    }
}
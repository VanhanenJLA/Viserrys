package viserrys.photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import viserrys.account.Account;

@Service
public class PhotoService {

    @Autowired
    public PhotoRepository photoRepository;

    public Photo uploadPhoto(MultipartFile file, String description, Account uploader) throws Exception {

        var type = file.getContentType();

        if (!type.equals("image/jpeg"))
            if (!type.equals("image/png"))
                throw new Exception("Unsupported file type: " + type);
        var photo = new Photo(uploader, description, file.getBytes());
        return photoRepository.save(photo);
    }

    public void delete(Long id) {
        var photo = photoRepository.getOne(id);
        photoRepository.delete(photo);
    }

}
package viserrys.Photo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import viserrys.Account.Account;

@Service
public class PhotoService {

  @Autowired
  private PhotoRepository photoRepository;

  public Photo uploadPhoto(MultipartFile file, String description, Account uploader) throws Exception {

    var type = file.getContentType();

    if (!type.equals("image/jpeg"))
      if (!type.equals("image/png"))
        throw new Exception("Unsupported file type: " + type);

    return photoRepository.save(new Photo(uploader, description, file.getBytes()));
  }

  public void delete(Long photoId) {
    var photo = photoRepository.getOne(photoId);
    photoRepository.delete(photo);
  }

  public Photo getOne(Long id) {
    return photoRepository.getOne(id);
  }

  public List<Photo> findAllByUploader(Account uploader) {
    return photoRepository.findAllByUploader(uploader);
  }

}
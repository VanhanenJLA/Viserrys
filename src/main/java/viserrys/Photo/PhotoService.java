package viserrys.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import viserrys.Account.Account;
import viserrys.Account.AccountRepository;

@Service
public class PhotoService {

  @Autowired
  private PhotoRepository photoRepository;

  @Autowired
  private AccountRepository accountRepository;

  public Photo uploadPhoto(MultipartFile file, String description, Account uploader) throws Exception {

    var type = file.getContentType();

    if (!type.equals("image/jpeg"))
      if (!type.equals("image/png"))
        throw new Exception("Unsupported file type: " + type);

    var photo = new Photo(description, file.getBytes());
    photoRepository.save(photo);

    uploader.getPhotos().add(photo);
    accountRepository.save(uploader);

    return photo;
  }

  public Photo getOne(Long id) {
    return photoRepository.getOne(id);
  }

}
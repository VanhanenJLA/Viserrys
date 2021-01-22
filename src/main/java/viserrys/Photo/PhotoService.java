package viserrys.Photo;

import java.util.ArrayList;
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

    return photoRepository.save(new Photo(uploader, description, file.getBytes(), new ArrayList<Account>()));
  }

  public void delete(Long id) {
    var photo = photoRepository.getOne(id);
    photoRepository.delete(photo);
  }

  public Photo like(Long id, Account sender) throws Exception {
    var photo = photoRepository.findById(id).get();

    if (photo.getLikers().contains(sender))
      throw new Exception("Photo " + photo.getId() + " is already liked by " + sender.getUsername());

    photo.likers.add(sender);

    return photoRepository.save(photo);
  }

  public Photo getOne(Long id) {
    return photoRepository.getOne(id);
  }

  public List<Photo> findAllByUploader(Account uploader) {
    return photoRepository.findAllByUploader(uploader);
  }

}
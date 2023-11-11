package viserrys.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import viserrys.Account.Account;
import viserrys.Comment.Comment;
import viserrys.Reaction.Reaction;

import java.util.ArrayList;

@Service
public class PhotoService {

  @Autowired
  public PhotoRepository photoRepository;

  public Photo uploadPhoto(MultipartFile file, String description, Account uploader) throws Exception {

    var type = file.getContentType();

    if (!type.equals("image/jpeg"))
      if (!type.equals("image/png"))
        throw new Exception("Unsupported file type: " + type);
    var photo = new Photo(uploader, description, file.getBytes(), new ArrayList<Comment>(), new ArrayList<Reaction>());
    return photoRepository.save(photo);
  }

  public void delete(Long id) {
    var photo = photoRepository.getOne(id);
    photoRepository.delete(photo);
  }

}
package viserrys.photo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PhotoController {

  final PhotoService photoService;

  public PhotoController(PhotoService photoService) {
    this.photoService = photoService;
  }

  @GetMapping(path = "/photos/{id}/content", produces = "image/jpg")
  @ResponseBody
  public byte[] getContent(@PathVariable Long id) {
    return photoService.getPhotoById(id).getContent();
  }

}

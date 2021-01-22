
package viserrys.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import viserrys.Auth.AuthService;

@Controller
public class PhotoController {

  @Autowired
  PhotoService photoService;

  @Autowired
  AuthService authService;

  @GetMapping(path = "/photos/{id}/content", produces = "image/jpg")
  @ResponseBody
  public byte[] getContent(@PathVariable Long id) {
    return photoService.getOne(id).getContent();
  }

}

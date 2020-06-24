
package viserrys.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import viserrys.Account.Account;
import viserrys.Account.AccountService;
import viserrys.Auth.AuthService;

@Controller
public class PhotoController {

  @Autowired
  private PhotoService photoService;

  @Autowired
  private AccountService accountService;
  
  @Autowired
  private AuthService authService;

  @GetMapping(path = "/photos/{id}/content", produces = "image/jpg")
  @ResponseBody
  public byte[] getContent(@PathVariable Long id) {
    return photoService.getOne(id).getContent();
  }

  @PostMapping("/accounts/{profileHandle}/photos")
  public String uploadPhoto(@PathVariable String profileHandle, @RequestParam MultipartFile file,
      @RequestParam String description) throws Exception {

    Account target = accountService.getAccountBy(profileHandle);
    Account uploader = authService.getAuthenticatedAccount();

    if (!uploader.equals(target))
      throw new Exception("Uploader doesn't match upload target account!");

    photoService.uploadPhoto(file, description, uploader);
    return "redirect:/accounts/" + profileHandle + "/photos";
  }

}

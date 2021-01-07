package viserrys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viserrys.Account.Account;
import viserrys.Account.AccountService;

@Service
public class TestService {

  public Account jouni = makeJouni();

  public Account makeJouni() {
    Account a = new Account();
    a.setUsername("Jouni");
    a.setPassword("salasana");
    return a;
  }

  @Autowired
  AccountService accountService;
  public void createTesters() {
    try {
      accountService.createAccount("Jauni", "salasana");
      accountService.createAccount("Jioni", "salasana");
      accountService.createAccount("Jaoni", "salasana");
      accountService.createAccount("Jooni", "salasana");
      accountService.createAccount("Jöoni", "salasana");
      accountService.createAccount("Jyoni", "salasana");
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}
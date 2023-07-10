import com.socialmediablogapi.Controller.SocialMediaController;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.socialmediablogapi.DAO.AccountDao;
import com.socialmediablogapi.DAO.MessageDao;
import com.socialmediablogapi.DAO.AccountDaoImpl;
import com.socialmediablogapi.DAO.MessageDaoImpl;
import com.socialmediablogapi.Service.AccountService;
import com.socialmediablogapi.Service.MessageService;
import com.socialmediablogapi.Service.AccountServiceImpl;
import com.socialmediablogapi.Service.MessageServiceImpl;

public class Main {

  public static void main(String[] args) {

    AccountDao accountDao = new AccountDaoImpl();
    MessageDao messageDao = new MessageDaoImpl();

    AccountService accountService = new AccountServiceImpl(accountDao);
    MessageService messageService = new MessageServiceImpl(messageDao);

    SocialMediaController controller = new SocialMediaController(accountService, messageService);

    Javalin app = controller.startAPI();
    app.start(8080);
  }
}

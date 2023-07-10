package com.socialmediablogapi.Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.socialmediablogapi.Model.Account;
import com.socialmediablogapi.Model.Message;
import com.socialmediablogapi.Service.AccountService;
import com.socialmediablogapi.Service.MessageService;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SocialMediaController {

  private AccountService accountService;
  private MessageService messageService;
  private ObjectMapper mapper;

  public SocialMediaController () {
  }

  public SocialMediaController(AccountService accountService, MessageService messageService) {
    this.accountService = accountService;
    this.messageService = messageService;
    this.mapper = new ObjectMapper();
  }

  public Javalin startAPI() {
    Javalin app = Javalin.create();
    app.post("/register", this::registerAccountHandler);
    app.post("/login", this::loginAccountHandler);
    app.post("/messages", this::createMessageHandler);
    app.get("/messages", this::getAllMessagesHandler);
    app.get("/messages/{message_id}", this::getMessageByIdHandler);
    app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
    app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
    app.get("/accounts/{account_id}", this::getAllMessagesByAccountIdHandler);
    return app;
  }

  private void registerAccountHandler(Context ctx) throws JsonProcessingException {
    Account account = mapper.readValue(ctx.body(), Account.class);
    Account registerAccount = accountService.registerAccount(account);
    if (registerAccount == null) {
      ctx.status(400);
    } else {
      ctx.json(registerAccount);
    }
  }

  private void loginAccountHandler(Context ctx) throws JsonProcessingException {
    Account account = mapper.readValue(ctx.body(), Account.class);
    Account loginAccount = accountService.loginAccount(account);
    if (loginAccount != null) {
      ctx.json(loginAccount);
    } else {
      ctx.status(401);
    }
  }

  private void createMessageHandler(Context ctx) throws JsonProcessingException {
    Message message = mapper.readValue(ctx.body(), Message.class);
    if (message != null) {
      Message createMessage = messageService.createMessage(message);
      if (createMessage != null) {
        ctx.json(createMessage);
      } else {
        ctx.status(500);
      }
    } else {
      ctx.status(400);
    }
  }

  private void getAllMessagesHandler(Context ctx) {
    ctx.json(messageService.getAllMessages());
  }

  private void getMessageByIdHandler(Context ctx) {
    String messageIdString = ctx.pathParam("message_id");
    try {
      int messageId = Integer.parseInt(messageIdString);
      Message message = messageService.getMessageById(messageId);
      if (message != null) {
        ctx.json(message);
      } else {
        ctx.status(404);
      }
    } catch (NumberFormatException ex) {
      ctx.status(400);
    }
  }

  private void deleteMessageByIdHandler(Context ctx) {
    String messageIdString = ctx.pathParam("message_id");
    try {
      int messageId = Integer.parseInt(messageIdString);
      messageService.deleteMessageById(messageId);
    } catch (NumberFormatException ex) {
      ctx.status(400);
    }
  }

  private void updateMessageByIdHandler(Context ctx) {
    String messageIdString = ctx.pathParam("message_id");
    try {
      int messageId = Integer.parseInt(messageIdString);
      Message updatedMessage = ctx.bodyAsClass(Message.class);
      updatedMessage.setMessage_id(messageId);
      Message updated = messageService.updateMessageById(updatedMessage);
      if (updated != null) {
        ctx.json(updated);
      } else {
        ctx.status(500);
      }
    } catch (NumberFormatException ex) {
      ctx.status(400);
    }
  }

  private void getAllMessagesByAccountIdHandler(Context ctx) {
    String accountIdString = ctx.pathParam("account_id");
    try {
      int accountId = Integer.parseInt(accountIdString);
      List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
      ctx.json(messages);
    } catch (NumberFormatException ex) {
      ctx.status(400);
    }
  }
}
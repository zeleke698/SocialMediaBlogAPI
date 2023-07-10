package com.socialmediablogapi.DAO;

import com.socialmediablogapi.Model.Message;
import java.util.List;

public interface MessageDao {

  Message createMessage(Message message);

  List<Message> getAllMessages();

  Message getMessageById(int id);

  void deleteMessageById(int id);

  Message updateMessageById(Message message);

  List<Message> getAllMessagesByAccountId(int accountId);

}
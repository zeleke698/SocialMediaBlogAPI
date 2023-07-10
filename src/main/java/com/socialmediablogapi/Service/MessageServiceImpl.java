package com.socialmediablogapi.Service;

import com.socialmediablogapi.Model.Message;
import com.socialmediablogapi.DAO.MessageDao;
import java.util.List;

public class MessageServiceImpl implements MessageService {

  private MessageDao messageDao;

  public MessageServiceImpl(MessageDao messageDao) {
    this.messageDao = messageDao;
  }

  @Override
  public Message createMessage(Message message) {
    return messageDao.createMessage(message);
  }

  @Override
  public List<Message> getAllMessages() {
    return messageDao.getAllMessages();
  }

  @Override
  public Message getMessageById(int id) {
    return messageDao.getMessageById(id);
  }

  @Override
  public void deleteMessageById(int id) {
    messageDao.deleteMessageById(id);
  }

  @Override
  public Message updateMessageById(Message message) {
    return messageDao.updateMessageById(message);
  }

  @Override
  public List<Message> getAllMessagesByAccountId(int accountId) {
    return messageDao.getAllMessagesByAccountId(accountId);
  }
}

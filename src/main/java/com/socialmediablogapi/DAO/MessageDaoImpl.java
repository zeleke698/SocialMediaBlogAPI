package com.socialmediablogapi.DAO;

import com.socialmediablogapi.Model.Message;
import com.socialmediablogapi.Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class MessageDaoImpl implements MessageDao {

  @Override
  public Message createMessage(Message message) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);",
             Statement.RETURN_GENERATED_KEYS
         )) {

      // Check if the account exists
      if (!accountExists(connection, message.getPosted_by())) {
        return null;
      }

      // Check the validity of message text
      if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
        return null;
      }

      ps.setInt(1, message.getPosted_by());
      ps.setString(2, message.getMessage_text());
      ps.setLong(3, message.getTime_posted_epoch());
      ps.executeUpdate();

      try (ResultSet primaryKeyResultSet = ps.getGeneratedKeys()) {
        if (primaryKeyResultSet.next()) {
          int id = primaryKeyResultSet.getInt(1);
          message.setMessage_id(id);
          return message;
        }
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private boolean accountExists(Connection connection, int id) throws SQLException {
    String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int count = rs.getInt(1);
        return count > 0;
      }
    }
    return false;
  }

  @Override
  public List<Message> getAllMessages() {
    List<Message> messages = new ArrayList<>();
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT * FROM message");
         ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        int messageId = rs.getInt("message_id");
        int postedBy = rs.getInt("posted_by");
        String messageText = rs.getString("message_text");
        long timePostedEpoch = rs.getLong("time_posted_epoch");
        Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
        messages.add(message);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return messages;
  }

  @Override
  public Message getMessageById(int id) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT * FROM message WHERE message_id = ?")) {

      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int messageId = rs.getInt("message_id");
        int postedBy = rs.getInt("posted_by");
        String messageText = rs.getString("message_text");
        long timePostedEpoch = rs.getLong("time_posted_epoch");
        Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
        return message;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  @Override
  public void deleteMessageById(int id) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement("DELETE FROM message WHERE message_id = ?")) {

      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

  @Override
  public Message updateMessageById(Message message) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?")) {

      ps.setInt(1, message.getPosted_by());
      ps.setString(2, message.getMessage_text());
      ps.setLong(3, message.getTime_posted_epoch());
      ps.setInt(4, message.getMessage_id());

      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0) {
        return message;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Message> getAllMessagesByAccountId(int accountId) {
    List<Message> messages = new ArrayList<>();
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement("SELECT * FROM message WHERE posted_by = ?")) {

      ps.setInt(1, accountId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int messageId = rs.getInt("message_id");
        int postedBy = rs.getInt("posted_by");
        String messageText = rs.getString("message_text");
        long timePostedEpoch = rs.getLong("time_posted_epoch");
        Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
        messages.add(message);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return messages;
  }
}

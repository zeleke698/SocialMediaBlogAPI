package com.socialmediablogapi.DAO;

import com.socialmediablogapi.Model.Account;
import com.socialmediablogapi.Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDaoImpl implements AccountDao {

  @Override
  public Account registerAccount(Account account) {
    // Check if username is blank or password is less than 4 characters
    if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
      return null;
    }

    // Try connection
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "INSERT INTO account (username, password) VALUES (?, ?);",
             PreparedStatement.RETURN_GENERATED_KEYS
         )) {

      // Return null if a username already exists
      if (usernameExists(account.getUsername())) {
        return null;
      }

      ps.setString(1, account.getUsername());
      ps.setString(2, account.getPassword());
      ps.executeUpdate();

      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int id = generatedKeys.getInt(1);
          account.setAccount_id(id);
          return account;
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean usernameExists(String username) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "SELECT COUNT(*) FROM account WHERE username = ?;"
         )) {

      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        // If username count is greater than 0, usernameExists returns true
        if (rs.next()) {
          int count = rs.getInt(1);
          return count > 0;
        }
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  @Override
  public Account loginAccount(Account account) {
    try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "SELECT * FROM account WHERE username = ? AND password = ?"
         )) {

      ps.setString(1, account.getUsername());
      ps.setString(2, account.getPassword());
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          int id = resultSet.getInt("account_id");
          return new Account(id, account.getUsername(), account.getPassword());
        }
      }
      return null;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // @Override
  // public Account updateAccount(Account account) {

  // try (Connection connection = ConnectionUtil.getConnection()) {

  // String sql = "UPDATE account SET username = ?, password = ? WHERE account_id
  // = ?;";

  // PreparedStatement ps = connection.prepareStatement(sql);

  // ps.setString(1, account.getUsername());
  // ps.setString(2, account.getPassword());
  // ps.setInt(3, account.getAccount_id());

  // int rowsAffected = ps.executeUpdate();

  // if (rowsAffected > 0) {
  // return account;
  // }
  // } catch (SQLException ex) {
  // ex.printStackTrace();
  // }

  // return null;
  // }

  // @Override
  // public void deleteAccount(int id) {

  // try (Connection connection = ConnectionUtil.getConnection()) {

  // String sql = "DELETE FROM account WHERE account_id = ?;" ;

  // PreparedStatement ps = connection.prepareStatement(sql);

  // ps.setInt(1, id);
  // ps.executeUpdate();

  // } catch (SQLException ex){
  // System.out.println(ex.getMessage());
  // }
  // }
}

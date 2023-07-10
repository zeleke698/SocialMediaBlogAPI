package com.socialmediablogapi.DAO;

import com.socialmediablogapi.Model.Account;

public interface AccountDao {

  Account registerAccount(Account account);

  Account loginAccount(Account account);

  boolean usernameExists(String username);

  // Account updateAccount(Account account);

  // void deleteAccount(int account_id);

}

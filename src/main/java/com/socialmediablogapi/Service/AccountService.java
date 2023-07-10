package com.socialmediablogapi.Service;

import com.socialmediablogapi.Model.Account;

public interface AccountService {

  Account registerAccount(Account account);

  Account loginAccount(Account account);

  // Account updateAccount(Account account);

  // void deleteAccount(int id);

}

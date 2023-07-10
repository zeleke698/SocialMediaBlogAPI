package com.socialmediablogapi.Service;

import com.socialmediablogapi.Model.Account;
import com.socialmediablogapi.DAO.AccountDao;

public class AccountServiceImpl implements AccountService {

  private AccountDao accountDao;

  public AccountServiceImpl(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  @Override
  public Account registerAccount(Account account) {
    return accountDao.registerAccount(account);
  }

  @Override
  public Account loginAccount(Account account) {
    return accountDao.loginAccount(account);
  }

  // @Override
  // public Account updateAccount(Account account){
  // return accountDao.updateAccount(account);
  // }

  // @Override
  // public void deleteAccount(int id){
  // accountDao.deleteAccount(id);
  // }
}
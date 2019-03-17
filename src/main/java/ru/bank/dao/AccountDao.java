package ru.bank.dao;

import ru.bank.domain.Account;

import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class AccountDao {

    private ConcurrentMap<UUID, Account> accounts = new ConcurrentHashMap<>();

    public Account getAccountById(UUID accountId) {
         return accounts.get(accountId);
    }


    public void createAccount(Account account) {
         accounts.putIfAbsent(account.getId(), account);
    }

    public void updateAccount(Account account) {
         accounts.put(account.getId(), account);
    }

    public void deleteAccount(UUID accountId) {
         accounts.remove(accountId);
    }

}

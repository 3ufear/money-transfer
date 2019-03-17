package ru.bank.service;

import io.micronaut.http.HttpStatus;
import ru.bank.dao.AccountDao;
import ru.bank.domain.Account;
import ru.bank.domain.AccountOperation;
import ru.bank.exception.BusinessException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Singleton
//There won't be any checks for consistence of users and accounts to simplify application
//users existance only checks when account is created
public class AccountService {

    private final UserService userService;
    private final AccountDao accountDao;
    private final AccountLockService accountLockService;

    @Inject
    public AccountService(UserService userService, AccountDao accountDao, AccountLockService accountLockService) {
        this.userService = userService;
        this.accountDao = accountDao;
        this.accountLockService = accountLockService;
    }

    public Account getById(UUID accountId) {
        var account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return account;
    }

    public Account createAccount(UUID userId, Account account) {
        userService.getById(userId);
        account.setId(UUID.randomUUID());
        accountDao.createAccount(account);

        return account;
    }

    public void deleteAccount(UUID accountId) {
        //check if exists
        getById(accountId);
        try {
            accountLockService.acquire(accountId.toString());
            accountDao.deleteAccount(accountId);
        } finally {
            accountLockService.release(accountId.toString());
        }
    }

    public Account updateBalance(UUID accountId, AccountOperation operation) {
        try {
            accountLockService.acquire(accountId.toString());
            Account account = getById(accountId);
            switch (operation.getOperationType()) {
                case DEPOSIT:
                    account.setBalance(account.getBalance().add(operation.getAmount()));
                    break;
                case WITHDRAW:
                    account.setBalance(account.getBalance().subtract(operation.getAmount()));
                    break;
            }
            validateIfOperationPermitted(account);
            accountDao.updateAccount(account);
        } finally {
            accountLockService.release(accountId.toString());
        }

        return getById(accountId);
    }

    private void validateIfOperationPermitted(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0 ) {
            throw new BusinessException(HttpStatus.NOT_MODIFIED, "Final amount must be >= 0");
        }
    }
}

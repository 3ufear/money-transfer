package ru.bank.service;

import io.micronaut.http.HttpStatus;
import ru.bank.dao.AccountDao;
import ru.bank.domain.Account;
import ru.bank.exception.BusinessException;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class TransferService {

    private final AccountService accountService;
    private final AccountLockService accountLockService;
    private final AccountDao accountDao;

    public TransferService(AccountService accountService, AccountLockService accountLockService, AccountDao accountDao) {
        this.accountService = accountService;
        this.accountLockService = accountLockService;
        this.accountDao = accountDao;
    }

    public void performTransfer(UUID accountFrom, UUID accountTo, BigDecimal amount) {
        /*
            We can make here double checks, to avoid blocking when account not exists or havn't enough money,
            but i don't think that we need it in demo app
         */
        try{
            lockOrdered(Arrays.asList(accountFrom, accountTo));
            Account from = accountService.getById(accountFrom);
            Account to = accountService.getById(accountTo);
            transfer(from, to, amount);
            validateAmount(from, to);
            accountDao.updateAccount(from);
            accountDao.updateAccount(to);
        } finally {
            unlockOrdered(Arrays.asList(accountFrom, accountTo));
        }
    }

    private void transfer(Account from, Account to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }

    private void validateAmount(Account from, Account to) {
        if (from.getBalance().compareTo(BigDecimal.ZERO) < 0 || to.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(HttpStatus.NOT_MODIFIED, "Final amount must be >= 0");
        }
    }

    private void lockOrdered(List<UUID> uuids) {
        uuids.stream().sorted().map(UUID::toString).forEach(accountLockService::acquire);
    }

    private void unlockOrdered(List<UUID> uuids) {
        uuids.stream().sorted().map(UUID::toString).forEach(accountLockService::release);
    }

}

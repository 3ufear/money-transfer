package ru.bank.mapper;

import ru.bank.domain.Account;
import ru.bank.domain.AccountOperation;

import javax.inject.Singleton;

@Singleton
public class AccountMapper {

    public Account accountDtotoDomain(ru.bank.dto.Account account) {
        var result = new Account();
        result.setBalance(account.getBalance());
        result.setId(account.getId());

        return result;
    }

    public ru.bank.dto.Account accountDomainToDto(Account account) {
        var result = new ru.bank.dto.Account();
        result.setBalance(account.getBalance());
        result.setId(account.getId());

        return result;
    }

    public AccountOperation mapOperationDtotoDomain(ru.bank.dto.AccountOperation operation) {
        var result = new AccountOperation();
        result.setAmount(operation.getAmount());
        result.setOperationType(operation.getOperationType());
        return result;
    }
}

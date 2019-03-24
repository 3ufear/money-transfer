package ru.bank.web.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import ru.bank.dto.*;
import ru.bank.mapper.AccountMapper;
import ru.bank.service.AccountService;
import ru.bank.service.TransferService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Validated
@Controller("/api/v1/")
public class UserAccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final TransferService transferService;

    @Inject
    public UserAccountController(AccountService accountService,
                                 AccountMapper accountMapper,
                                 TransferService transferService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.transferService = transferService;
    }


    @Post("/user/{userId}/account")
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<Account> createAccount(@NotNull final UUID userId,
                                               @Body @Valid final Account account) {
        Account createdAccount = accountMapper.accountDomainToDto(
                accountService.createAccount(userId, accountMapper.accountDtotoDomain(account))
        );
        return HttpResponse.ok(createdAccount);
    }

    @Get(uri = "/account/{accountId}")
    @Produces("application/json")
    public HttpResponse<Account> getAccountBalance(@NotNull final UUID accountId) {
        Account account = accountMapper.accountDomainToDto(accountService.getById(accountId));
        return HttpResponse.ok(account);
    }

    @Put(uri = "/account/{accountId}")
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<Account> performOperation(@NotNull final UUID accountId,
                                                  @Body @Valid final AccountOperation operation) {
        Account account = accountMapper.accountDomainToDto(
                accountService.updateBalance(accountId, accountMapper.mapOperationDtotoDomain(operation))
        );
        return HttpResponse.ok(account);
    }

    @Put(uri = "/account/{accountId}")
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<Account> performTransfer(@NotNull final UUID accountId,
                                                 @Body @Valid final AccountOperation operation) {
        Account account = accountMapper.accountDomainToDto(
                accountService.updateBalance(accountId, accountMapper.mapOperationDtotoDomain(operation))
        );
        return HttpResponse.ok(account);
    }

    @Post(uri = "/transfer/{from}/{to}")
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<Account> performTransfer(@PathVariable @NotNull final UUID from,@PathVariable @NotNull final UUID to,
                                                 @Body @Valid final TransferOperation operation) {
        transferService.performTransfer(from, to, operation.getAmount());
        return HttpResponse.ok();
    }


    @Delete(uri = "/account/{accountId}")
    public HttpResponse<User> deleteAccount(@NotNull final UUID accountId) {
        accountService.deleteAccount(accountId);
        return HttpResponse.ok();
    }
}

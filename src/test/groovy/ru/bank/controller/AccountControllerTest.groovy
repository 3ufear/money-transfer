package ru.bank.controller

import io.micronaut.http.HttpStatus
import ru.bank.dto.AccountOperation
import ru.bank.dto.OperationType
import ru.bank.dto.TransferOperation
import ru.bank.dto.UpdatedUser
import ru.bank.dto.User
import ru.bank.mapper.AccountMapper
import ru.bank.mapper.UserMapper
import ru.bank.service.AccountService
import ru.bank.service.TransferService
import ru.bank.service.UserService
import ru.bank.web.controller.UserAccountController
import ru.bank.web.controller.UserController
import spock.lang.Specification

import static java.util.UUID.randomUUID

class AccountControllerTest extends Specification {

    AccountService accountService = Mock()
    AccountMapper accountMapper = Mock()
    TransferService transferService = Mock()

    UserAccountController controller = new UserAccountController(accountService, accountMapper, transferService)

    def "Should create account"() {
        given:
        def userId = randomUUID()
        def accountDto = new ru.bank.dto.Account(
            balance : BigDecimal.TEN
        )

        def accountDomain = new ru.bank.domain.Account(
                balance : BigDecimal.TEN
        )

        when:
        def result = controller.createAccount(userId, accountDto )

        then:
        1 * accountMapper.accountDtotoDomain(accountDto) >> accountDomain
        1 * accountService.createAccount(userId, accountDomain)
        result.getStatus() == HttpStatus.OK
    }

    def "Should delete account"() {
        given:
        def id = randomUUID()

        when:
        def result = controller.deleteAccount(id)

        then:
        1 * accountService.deleteAccount(id)
        result.getStatus() == HttpStatus.OK
    }

    def "Should update balance"() {
        given:
        def id = randomUUID()
        def operationDto = new  AccountOperation(
            operationType : OperationType.WITHDRAW,
            amount: BigDecimal.TEN
        )

        def operationDomain = new  ru.bank.domain.AccountOperation(
                operationType : OperationType.WITHDRAW,
                amount: BigDecimal.TEN
        )

        when:
        def result = controller.performTransfer(id, operationDto)

        then:
        1 * accountMapper.mapOperationDtotoDomain(operationDto) >> operationDomain
        1 * accountService.updateBalance(id, operationDomain)
        result.getStatus() == HttpStatus.OK
    }

    def "Should transfer"() {
        given:
        def idFrom = randomUUID()
        def idTo = randomUUID()
        def transfer = new TransferOperation(
            amount: BigDecimal.TEN
        )

        when:
        def result = controller.performTransfer(idFrom, idTo, transfer)

        then:
        1 * transferService.performTransfer(idFrom, idTo, BigDecimal.TEN)
        result.getStatus() == HttpStatus.OK
    }

}

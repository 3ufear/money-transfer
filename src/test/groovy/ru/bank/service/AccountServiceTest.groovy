package ru.bank.service

import io.micronaut.http.HttpStatus
import ru.bank.dao.AccountDao
import ru.bank.domain.Account
import ru.bank.domain.AccountOperation
import ru.bank.dto.OperationType
import ru.bank.exception.BusinessException
import spock.lang.Specification

import static ru.bank.dto.OperationType.DEPOSIT
import static ru.bank.dto.OperationType.WITHDRAW

class AccountServiceTest extends Specification {

    UserService userService = Mock()
    AccountDao accountDao = Mock()
    AccountLockService accountLockService = Mock()

    AccountService accountService = new AccountService(userService, accountDao, accountLockService)

    def "Should get account by id"() {
        given:
        def id = UUID.randomUUID()

        when:
        def result = accountService.getById(id)

        then:
        1 * accountDao.getAccountById(id) >> new Account(
                id : id
        )
        result.id == id
    }

    def "Should throw exception on not existing account"() {
        given:
        def id = UUID.randomUUID()

        when:
        accountService.getById(id)

        then:
        1 * accountDao.getAccountById(id) >> null
        thrown(BusinessException)
    }

    def "Should create account"() {
        given:
        def userId = UUID.randomUUID()
        def accountId = UUID.randomUUID()
        def account = new Account(
                id: accountId,
                balance: BigDecimal.ONE
        )

        when:
        accountService.createAccount(userId, account)

        then:
        1 * userService.getById(userId)
        1 * accountDao.createAccount(account)

    }

    def "Should delete account"() {
        given:
        def accountId = UUID.randomUUID()

        when:
        accountService.deleteAccount(accountId)

        then:
        1 * accountLockService.acquire(accountId.toString())
        1 * accountDao.getAccountById(accountId) >> new Account(id : accountId)
        1 * accountDao.deleteAccount(accountId)
        1 * accountLockService.release(accountId.toString())

    }

    def "Should create account on not existing user"() {
        given:
        def userId = UUID.randomUUID()
        def accountId = UUID.randomUUID()
        def account = new Account(
                id: accountId,
                balance: BigDecimal.ONE
        )

        when:
        accountService.createAccount(userId, account)

        then:
        1 * userService.getById(userId) >> { throw new BusinessException(HttpStatus.NOT_FOUND, "") }
        0 * accountDao.createAccount(account)
        thrown(BusinessException)
    }

    def "Should update balance"(def operationType, def expectedSum) {
        given:
        def accountId = UUID.randomUUID()
        def account = new Account(
                id: accountId,
                balance: BigDecimal.TEN
        )
        def operation = new AccountOperation(
                operationType: operationType,
                amount: BigDecimal.ONE
        )

        when:
        accountService.updateBalance(accountId, operation)

        then:
        1 * accountLockService.acquire(accountId.toString())
        1 * accountDao.getAccountById(accountId) >> account
        1 * accountDao.updateAccount(account)
        1 * accountLockService.release(accountId.toString())
        1 * accountDao.getAccountById(accountId) >> account
        account.balance == expectedSum
        where:
        operationType | expectedSum
        DEPOSIT       |  BigDecimal.valueOf(11)
        WITHDRAW      |  BigDecimal.valueOf(9)
    }

    def "Should update balance - invalid result"() {
        given:
        def accountId = UUID.randomUUID()
        def account = new Account(
                id: accountId,
                balance: BigDecimal.ONE
        )
        def operation = new AccountOperation(
                operationType: WITHDRAW,
                amount: BigDecimal.TEN
        )

        when:
        accountService.updateBalance(accountId, operation)

        then:
        1 * accountLockService.acquire(accountId.toString())
        1 * accountDao.getAccountById(accountId) >> account
        0 * accountDao.updateAccount(account)
        1 * accountLockService.release(accountId.toString())
        thrown(BusinessException)
    }

}

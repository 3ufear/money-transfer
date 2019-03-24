package ru.bank.service

import ru.bank.dao.AccountDao
import ru.bank.domain.Account
import ru.bank.exception.BusinessException
import spock.lang.Specification

class TransferServiceTest extends Specification {

    AccountService accountService = Mock()
    AccountLockService accountLockService = Mock()
    AccountDao accountDao = Mock()

    TransferService transferService = new TransferService(accountService, accountLockService, accountDao)

    def "Should transfer between 2 accounts"() {
        given:
        def from = UUID.fromString("00000000-0000-0000-0000-000000004001")
        def to = UUID.fromString("00000000-0000-0000-0000-000000004002")
        def sourceAccount = new Account(id : from, balance : BigDecimal.TEN)
        def targetAccount = new Account(id : to, balance : BigDecimal.ZERO)

        when:
        transferService.performTransfer(from, to, BigDecimal.TEN)

        then:
        1 * accountLockService.acquire(from.toString())
        1 * accountLockService.acquire(to.toString())
        1 * accountService.getById(from) >> sourceAccount
        1 * accountService.getById(to) >> targetAccount
        1 * accountDao.updateAccount(sourceAccount)
        1 * accountDao.updateAccount(targetAccount)
        1 * accountLockService.release(from.toString())
        1 * accountLockService.release(to.toString())
        sourceAccount.balance == BigDecimal.ZERO
        targetAccount.balance == BigDecimal.TEN
    }

    def "Should throw exception"() {
        given:
        def from = UUID.fromString("00000000-0000-0000-0000-000000004001")
        def to = UUID.fromString("00000000-0000-0000-0000-000000004002")
        def sourceAccount = new Account(id : from, balance : BigDecimal.ZERO)
        def targetAccount = new Account(id : to, balance : BigDecimal.ZERO)

        when:
        transferService.performTransfer(from, to, BigDecimal.TEN)

        then:
        1 * accountLockService.acquire(from.toString())
        1 * accountLockService.acquire(to.toString())
        1 * accountService.getById(from) >> sourceAccount
        1 * accountService.getById(to) >> targetAccount
        1 * accountLockService.release(from.toString())
        1 * accountLockService.release(to.toString())
        thrown(BusinessException)
    }
}

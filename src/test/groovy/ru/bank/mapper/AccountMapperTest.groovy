package ru.bank.mapper

import ru.bank.dto.Account
import ru.bank.dto.AccountOperation
import ru.bank.dto.OperationType
import spock.lang.Specification

import static java.math.BigDecimal.ONE
import static java.util.UUID.randomUUID
import static ru.bank.dto.OperationType.DEPOSIT
import static ru.bank.dto.OperationType.WITHDRAW


class AccountMapperTest extends Specification {


    AccountMapper mapper = new AccountMapper()

    def "Should map AccountDto to domain"() {
        given:
        Account account = new Account(
            id: randomUUID(),
            balance: ONE
        )

        when:
        def result = mapper.accountDtotoDomain(account)

        then:
        result.id == account.id
        result.balance == account.balance
    }

    def "Should map AccountDomain to dto"() {
        given:
        ru.bank.domain.Account account = new ru.bank.domain.Account(
                id: randomUUID(),
                balance: ONE
        )

        when:
        def result = mapper.accountDomainToDto(account)

        then:
        result.id == account.id
        result.balance == account.balance
    }

    def "Should map OperationDto to domain"(OperationType operationType) {
        given:
        AccountOperation operation = new AccountOperation(
                operationType: operationType,
                amount: ONE
        )

        when:
        def result = mapper.mapOperationDtotoDomain(operation)

        then:
        result.operationType == operation.operationType
        result.amount == operation.amount

        where:
        operationType << [WITHDRAW, DEPOSIT]
    }
}

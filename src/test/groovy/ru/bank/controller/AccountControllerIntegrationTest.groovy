package ru.bank.controller

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.experimental.categories.Category
import ru.bank.IntegratonTest
import ru.bank.dao.AccountDao
import ru.bank.dao.UserDao
import ru.bank.dto.Account
import ru.bank.dto.TransferOperation
import ru.bank.dto.User
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static java.util.UUID.randomUUID

@Category(IntegratonTest.class)
class AccountControllerIntegrationTest extends Specification {

    @Shared
    @AutoCleanup
    def server = ApplicationContext.run EmbeddedServer
    @Shared
    @AutoCleanup
    def client = server.applicationContext.createBean HttpClient, server.getURL()


    @Shared
    def userDao  = server.applicationContext.getBean UserDao

    @Shared
    def accountDao  = server.applicationContext.getBean AccountDao

    void "Should create account"() {
        given:
        def user = createUser()
        def account = new Account(
                balance: BigDecimal.TEN
        )
        when:
        def response = client.toBlocking().exchange(HttpRequest.POST("/api/v1/user/$user.id/account", account))

        then:
        response.status == HttpStatus.OK
    }


    void "Should process transfer between 2 users"() {
        given:
        def from = createAccount()
        def to = createAccount()
        def operation = new TransferOperation(
                amount: BigDecimal.TEN
        )
        when:
        def response = client.toBlocking().exchange(HttpRequest.POST("/api/v1/transfer/$from.id/$to.id", operation))

        then:
        response.status == HttpStatus.OK
        accountDao.getAccountById(from.id).balance == BigDecimal.ZERO
        accountDao.getAccountById(to.id).balance == BigDecimal.valueOf(20)
    }


    ru.bank.domain.User createUser() {
        def user = new ru.bank.domain.User(
                id : randomUUID(),
                name: "name"
        )
        userDao.saveUser(user)
        return user
    }

    ru.bank.domain.Account createAccount() {
        def account = new ru.bank.domain.Account (
                id : randomUUID(),
                balance: BigDecimal.TEN
        )
        accountDao.createAccount(account)
        return account
    }
}

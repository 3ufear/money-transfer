package ru.bank.controller

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.experimental.categories.Category
import ru.bank.IntegratonTest
import ru.bank.dao.UserDao
import ru.bank.dto.User
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static java.util.UUID.randomUUID

@Category(IntegratonTest.class)
class UserControllerIntegrationTest extends Specification {

    @Shared
    @AutoCleanup
    def server = ApplicationContext.run EmbeddedServer
    @Shared
    @AutoCleanup
    def client = server.applicationContext.createBean HttpClient, server.getURL()


    @Shared
    def userDao  = server.applicationContext.getBean UserDao

    void "Should create user"() {
        given:
        def user = new User(
                name: "name"
        )
        when:
        def response = client.toBlocking().retrieve(HttpRequest.POST('/api/v1/user', user), User)

        then:
        userDao.getUser(response.getId()) != null
        response.name == "name"

    }

    void "Should throw exception - invalid user"() {
        given:
        def user = new User()
        when:
        client.toBlocking().exchange(HttpRequest.POST('/api/v1/user', user))

        then:
        thrown(HttpClientResponseException)

    }

    void "Should throw exception - user not exists"() {
        given:
        def id = randomUUID()
        when:
        client.toBlocking().exchange(HttpRequest.DELETE("/api/v1/user/$id"))

        then:
        thrown(HttpClientResponseException)
    }

    void "Should delete user"() {
        given:
        def user = createUser()
        when:
        def response = client.toBlocking().exchange(HttpRequest.DELETE("/api/v1/user/$user.id") )

        then:
        response.status == HttpStatus.OK
        userDao.getUser(user.id) == null
    }

    ru.bank.domain.User createUser() {
        def user = new ru.bank.domain.User(
                id : randomUUID(),
                name: "name"
        )
        userDao.saveUser(user)
        return user
    }
}

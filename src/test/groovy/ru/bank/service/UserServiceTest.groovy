package ru.bank.service

import ru.bank.dao.UserDao
import ru.bank.domain.User
import ru.bank.exception.BusinessException
import spock.lang.Specification

import static java.util.UUID.randomUUID

class UserServiceTest extends Specification {

    UserDao userDao = Mock()

    UserService userService = new UserService(
         userDao
    )

    def "should get User"() {
        given:
        def id = randomUUID()

        when:

        def result = userService.getById(id)

        then:
        1 * userDao.getUser(id) >> new User(id : id)
        result.id == id

    }

    def "should throw exception on not existing User"() {
        given:
        def id = randomUUID()

        when:

        userService.getById(id)

        then:
        1 * userDao.getUser(id) >> null
        thrown(BusinessException)
    }

    def "should create new User"() {
        given:

        User user = new User(
                name: "testName"
        )

        when:

        def result = userService.createUser(user)

        then:
        1 * userDao.saveUser(user)
        result.name == user.name
    }

    def "should update User"() {
        given:
        def id = randomUUID()
        User user = new User(
                id: id,
                name: "testName"
        )

        when:

        def result = userService.updateUser(user)

        then:
        1 * userDao.getUser(id) >> new User(id : id)
        1 * userDao.updateUser(user) >> null
        result.name == user.name
    }

    def "should throw exception on updating User"() {
        given:
        def id = randomUUID()
        User user = new User(
                id: id,
                name: "testName"
        )

        when:

        def result = userService.updateUser(user)

        then:
        1 * userDao.getUser(id) >> null
        0 * userDao.updateUser(user) >> null
        thrown(BusinessException)
    }


    def "should delete User"() {
        given:
        def id = randomUUID()

        when:

        userService.deleteUser(id)

        then:
        1 * userDao.getUser(id) >> new User(id : id)
        1 * userDao.deleteUser(id) >> null

    }

    def "should throw exception on deleting User"() {
        given:
        def id = randomUUID()

        when:

        userService.deleteUser(id)

        then:
        1 * userDao.getUser(id) >> null
        0 * userDao.deleteUser(id) >> null
        thrown(BusinessException)
    }
}

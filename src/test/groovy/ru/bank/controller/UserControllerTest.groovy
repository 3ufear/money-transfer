package ru.bank.controller

import io.micronaut.http.HttpStatus
import ru.bank.dto.UpdatedUser
import ru.bank.dto.User
import ru.bank.mapper.UserMapper
import ru.bank.service.UserService
import ru.bank.web.controller.UserController
import spock.lang.Specification

import static java.util.UUID.randomUUID

class UserControllerTest extends Specification {

    UserService userService = Mock()
    UserMapper userMapper = Mock()

    UserController controller = new UserController(userService, userMapper)

    def "Should create user"() {
        given:
        def user = new User(
                name : "testUser"
        )
        def serviceUser = new ru.bank.domain.User(
            name : "testUser"
        )

        when:
        def result = controller.createUser(user)

        then:
        1 * userMapper.userDtotoDomain(user) >> serviceUser
        1 * userService.createUser(serviceUser)
        result.getStatus() == HttpStatus.OK
    }

    def "Should update user"() {
        given:
        def updatedUser = new UpdatedUser(
                id: randomUUID(),
                name : "testUser"
        )

        def user = new ru.bank.domain.User(
            id: randomUUID(),
            name : "testUser"
        )
        when:
        def result = controller.updateUser(updatedUser)

        then:
        1 * userMapper.updatedUserToUserDomain(updatedUser) >> user
        1 * userService.updateUser(user)
        result.getStatus() == HttpStatus.OK
    }

    def "Should delete user"() {
        given:
        def id = randomUUID()

        when:
        def result = controller.deleteUser(id)

        then:
        1 * userService.deleteUser(id)
        result.getStatus() == HttpStatus.OK
    }

}

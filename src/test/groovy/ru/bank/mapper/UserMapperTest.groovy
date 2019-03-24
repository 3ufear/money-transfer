package ru.bank.mapper

import ru.bank.domain.User
import ru.bank.dto.UpdatedUser
import spock.lang.Specification

import static java.util.UUID.randomUUID

class UserMapperTest extends Specification {

    UserMapper userMapper = new UserMapper();

    def "Should map userDomain to dto"() {
        given:
        User user = new User(
                id: randomUUID(),
                name: "testName"
        )
        when:

        def result = userMapper.userDomainToDto(user)

        then:
        result.id == user.id
        result.name == user.name

    }

    def "Should map userDto to domain"() {
        given:
        ru.bank.dto.User user = new  ru.bank.dto.User(
                id: randomUUID(),
                name: "testName"
        )
        when:

        def result = userMapper.userDtotoDomain(user)

        then:
        result.id == user.id
        result.name == user.name

    }

    def "Should map updated user to domain"() {
        given:
        UpdatedUser user = new  UpdatedUser(
                id: randomUUID(),
                name: "testName"
        )
        when:

        def result = userMapper.updatedUserToUserDomain(user)

        then:
        result.id == user.id
        result.name == user.name

    }

}

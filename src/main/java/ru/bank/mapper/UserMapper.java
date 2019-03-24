package ru.bank.mapper;

import ru.bank.dto.UpdatedUser;
import ru.bank.dto.User;
import javax.inject.Singleton;

//ToDo: use mapstruct if have time
@Singleton
public class UserMapper {

    public ru.bank.domain.User updatedUserToUserDomain(UpdatedUser user) {
        var result = new ru.bank.domain.User();
        result.setId(user.getId());
        result.setName(user.getName());

        return result;
    }

    public User userDomainToDto(ru.bank.domain.User user) {
        User result = new User();
        result.setId(user.getId());
        result.setName(user.getName());

        return result;
    }

    public ru.bank.domain.User userDtotoDomain(User user) {
        var result = new ru.bank.domain.User();
        result.setId(user.getId());
        result.setName(user.getName());
        return result;
    }

}

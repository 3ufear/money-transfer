package ru.bank.web.controller;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.validation.Validated;
import ru.bank.dto.UpdatedUser;
import ru.bank.dto.User;
import ru.bank.exception.BusinessException;
import ru.bank.mapper.UserMapper;
import ru.bank.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Validated
@Controller("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Inject
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Post
    @Consumes("application/json")
    @Produces("application/json")
    public HttpResponse<User> createUser(@Body @Valid final User user) {
        final User createdUser = userMapper.userDomainToDto(
                userService.createUser(userMapper.userDtotoDomain(user))
        );
        return HttpResponse.ok(createdUser);
    }

    @Get(uri = "/{userId}")
    @Produces("application/json")
    public HttpResponse<User> getUser(@NotNull final UUID userId) {
        final User user = userMapper.userDomainToDto(userService.getById(userId));
        return HttpResponse.ok(user);
    }

    @Put
    @Produces("application/json")
    @Consumes("application/json")
    public HttpResponse<User> updateUser(@Body @Valid final UpdatedUser updatedUser) {
        final User user = userMapper.userDomainToDto(
                userService.updateUser(
                        userMapper.updatedUserToUserDomain(updatedUser)
                ));
        return HttpResponse.ok(user);
    }

    @Delete(uri = "/{userId}")
    public HttpResponse<User> deleteUser(@NotNull final UUID userId) {
        //Here we also need to delete accounts of user,
        //but because of simplified dao, we don't store information about users in accounts
        userService.deleteUser(userId);
        return HttpResponse.ok();
    }

}
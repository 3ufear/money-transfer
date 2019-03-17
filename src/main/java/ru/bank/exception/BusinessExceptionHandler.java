package ru.bank.exception;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import lombok.extern.java.Log;
import ru.bank.dto.Error;
import javax.inject.Singleton;
import java.util.logging.Level;

@Log
@Produces
@Singleton
@Requires(classes = {BusinessException.class, ExceptionHandler.class})
public class BusinessExceptionHandler implements ExceptionHandler<BusinessException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, BusinessException exception) {
        log.log(Level.INFO, "Error: {}", exception.getMessage());
        return HttpResponse.status(exception.getStatus()).body(new Error(exception.getMessage()));
    }

}

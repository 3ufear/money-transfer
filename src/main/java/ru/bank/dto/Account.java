package ru.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.validation.Validated;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Validated
public class Account {

    @JsonProperty("id")
    private UUID id;

    @NotNull
    @JsonProperty("balance")
    private BigDecimal balance;

}

package ru.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.validation.Validated;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@Validated
public class TransferOperation {
    @NotNull
    @Positive
    @JsonProperty("amount")
    BigDecimal amount;
}

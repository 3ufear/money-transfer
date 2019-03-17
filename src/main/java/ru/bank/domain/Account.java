package ru.bank.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Account {

    private UUID id;
    private BigDecimal balance;

}

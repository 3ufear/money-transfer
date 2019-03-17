package ru.bank.domain;

import lombok.Getter;
import lombok.Setter;
import ru.bank.dto.OperationType;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountOperation {
    OperationType operationType;
    BigDecimal amount;
}

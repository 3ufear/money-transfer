package ru.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Error {

    @JsonProperty("message")
    private String message;
}

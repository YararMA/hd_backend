package com.github.dlism.backend.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private String username;
    private String password;
    private String passwordConfirmation;
}

package com.github.dlism.backend.dto;

import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserDto {
    private String username;
    @Transient
    private String password;
    @Transient
    private String passwordConfirmation;

    public UserDto(String username){
        this.username=username;
    }
}

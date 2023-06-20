package com.github.dlism.backend.dto;

import java.io.Serializable;
import java.util.UUID;

public class RabbitmqDto implements Serializable {
    private String email;

    private UUID code;

    public RabbitmqDto() {
    }

    public RabbitmqDto(String email, UUID code) {
        this.email = email;
        this.code = code;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RabbitmqDto{" +
                "email='" + email + '\'' +
                ", code=" + code +
                '}';
    }
}

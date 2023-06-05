package com.github.dlism.model;

import java.io.Serializable;

public class RabbitmqDto implements Serializable {
    private String email;

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
                '}';
    }
}

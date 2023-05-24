package com.github.dlism.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserPojo {
    private Long id;
    private String username;

    public UserPojo(Long id, String username){
        this.id=id;
        this.username=username;
    }
}

package com.github.dlism.backend.pojo;

import lombok.Builder;
import lombok.Data;

@Data
public class OrganizationPojo {
    private Long id;
    private String name;
    private boolean active;
    private String description;
    private Long subscribers;
    private int participantsMaxCount;

    public OrganizationPojo(){}
    public OrganizationPojo(Long id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public OrganizationPojo(Long id, String name, String description, boolean active, Long subscribers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.subscribers = subscribers;
    }
}

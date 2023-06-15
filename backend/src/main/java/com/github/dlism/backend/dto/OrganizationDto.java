package com.github.dlism.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrganizationDto {

    private Long id;

    @NotEmpty(message = "Название организации не должен быть пустым")
    @Size(min = 5, max = 100, message = "Название организации должно содержать от 5 до 100 символов")
    private String name;

    private String description;
}

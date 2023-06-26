package com.github.dlism.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrganizationDto {
    private Long id;

    @NotEmpty(message = "Название организации не должен быть пустым")
    @Size(min = 5, max = 255, message = "Название организации должно содержать от 5 до 255 символов")
    private String name;

    private String description;

    @Min(value = 0, message = "Количество участников должен быть больше 0")
    private int participantsMaxCount;

    private String country;

    private String region;

    private String city;

    private String address;
}

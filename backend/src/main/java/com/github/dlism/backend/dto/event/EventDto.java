package com.github.dlism.backend.dto.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EventDto {
    private Long id;

    @NotEmpty(message = "Название организации не должен быть пустым")
    @Size(min = 5, max = 255, message = "Название организации должно содержать от 5 до 255 символов")
    private String name;

    @NotEmpty(message = "Название организации не должен быть пустым")
    @Size(min = 5, max = 1000, message = "Название организации должно содержать от 5 до 1000 символов")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}

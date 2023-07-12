package com.github.dlism.backend.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 3, max = 100, message = "Имя пользователя должно содержать от 3 до 100 символов")
    private String name;

    @NotEmpty(message = "Фамилия не должен быть пустым")
    @Size(min = 3, max = 100, message = "Фамилия пользователя должно содержать от 3 до 100 символов")
    private String firstname;
    private String lastname;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phone;

    @Pattern(regexp = "^(Муж|Жен)$", message = "Выберите пол")
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    private String country;
    private String region;
    private String locality;
    private String typeOfActivity;

    @NotEmpty(message = "Email не должен быть пустым")
    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов")
    @Email(message = "Введите валидный e-mail")
    private String username;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 8, max = 50, message = "Пароль должно содержать минимум 8 символов")
    private String password;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 8, max = 50, message = "Пароль должно содержать минимум 8 символов")
    private String passwordConfirmation;

}

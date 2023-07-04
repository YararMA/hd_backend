package com.github.dlism.backend.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.StringJoiner;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfileDto {
    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 3, max = 100, message = "Имя пользователя должно содержать от 3 до 100 символов")
    private String name;
    @NotEmpty(message = "Фамилия не должен быть пустым")
    @Size(min = 3, max = 100, message = "Фамилия пользователя должно содержать от 3 до 100 символов")
    private String firstname;
    private String lastname;
    private String phone;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String country;
    private String region;
    private String locality;
    private String typeOfActivity;

    @NotEmpty(message = "Email не должен быть пустым")
    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов")
    @Email(message = "Введите валидный e-mail")
    private String username;

    public String getFullName(){
        StringJoiner fullName = new StringJoiner(" ");
        fullName.add(this.name);
        fullName.add(this.firstname);
        fullName.add(this.lastname);
        return fullName.toString();
    }

    public String getFullAddress(){
        StringJoiner fullAddress = new StringJoiner(", ");
        fullAddress.add(this.country);
        fullAddress.add(this.region);
        fullAddress.add(this.locality);
        return fullAddress.toString();
    }

}

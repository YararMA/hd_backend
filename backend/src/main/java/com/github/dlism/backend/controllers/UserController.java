package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal User user, Model model) {
        UserDto userDto = new UserDto(user.getUsername(), user.getPassword(), null);

        model.addAttribute("user", userDto);
        return "forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user, @ModelAttribute("user") UserDto userDto, Model model) {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            model.addAttribute("passIsNotConfirm", "Пароли не совпадают!");
            return "forms/editUserProfile";
        }

        if (userService.update(user, userDto)) {
            model.addAttribute("updateSuccess", "Данные успешно обновлены");
        } else {
            model.addAttribute("userExists", "Пользовател уже существует!");
        }

        return "forms/editUserProfile";
    }
}

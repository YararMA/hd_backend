package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.mappers.UserMapper;
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
        model.addAttribute("user", UserMapper.INSTANCE.entityToDto(user));
        return "forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user, @ModelAttribute("user") UserDto userDto, Model model) {

        try {
            model.addAttribute("user", userService.update(user, userDto));
            model.addAttribute("updateSuccess", "Данные успешно обновлены");
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
        } catch (IllegalArgumentException e) {
            model.addAttribute("passIsNotConfirm", e.getMessage());
            return "forms/editUserProfile";
        }

        return "forms/editUserProfile";
    }
}

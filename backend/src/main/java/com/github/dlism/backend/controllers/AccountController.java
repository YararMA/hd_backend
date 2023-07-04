package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.user.UserUpdatePasswordDto;
import com.github.dlism.backend.dto.user.UserUpdateProfileDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.UpdateException;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/", "/main"})
    public String mainPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getById(user.getId()));
        return "account/index";
    }

    @GetMapping("/edit")
    public String editPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getById(user.getId()));
        return "account/forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user,
                       @Valid @ModelAttribute("user") UserUpdateProfileDto userUpdateProfileDto,
                       BindingResult bindingResult, Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "account/forms/editUserProfile";
        }

        try {
            model.addAttribute("user", userService.update(user, userUpdateProfileDto));
            model.addAttribute("updateSuccess", "Данные успешно обновлены");
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
        }

        return "account/forms/editUserProfile";
    }

    @GetMapping("/password")
    public String changePassword(Model model) {
        model.addAttribute("user", new UserUpdatePasswordDto());
        return "account/forms/changePassword";
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @Valid @ModelAttribute("user") UserUpdatePasswordDto userUpdatePasswordDto,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "account/forms/changePassword";
        }

        try {
            userService.changePassword(user, userUpdatePasswordDto);
            model.addAttribute("user", new UserUpdatePasswordDto());
            model.addAttribute("message", "Пароль изменен!");
        } catch (IllegalArgumentException | UpdateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "account/forms/changePassword";
    }
}

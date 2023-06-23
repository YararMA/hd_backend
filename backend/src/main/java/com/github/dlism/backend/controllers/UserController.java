package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.user.UserUpdateDto;
import com.github.dlism.backend.dto.user.UserUpdatePasswordDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.UpdateException;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getById(user.getId()));
        return "user/forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user,
                       @Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
                       BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "user/forms/editUserProfile";
        }

        try {
            userService.update(user, userUpdateDto);
            model.addAttribute("user", userService.getById(user.getId()));
            model.addAttribute("updateSuccess", "Данные успешно обновлены");
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
        }
        return "user/forms/editUserProfile";
    }

    @GetMapping("/password")
    public String changePassword(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", new UserUpdatePasswordDto());
        return "user/forms/changePassword";
    }

    @PostMapping("/password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @Valid @ModelAttribute("user") UserUpdatePasswordDto userUpdatePasswordDto,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/forms/changePassword";
        }

        try {
            userService.changePassword(user, userUpdatePasswordDto);
            model.addAttribute("user", new UserUpdatePasswordDto());
            model.addAttribute("message", "Пароль изменен!");
        } catch (IllegalArgumentException | UpdateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "user/forms/changePassword";
    }

    @GetMapping("/join/{organization}")
    public String join(@PathVariable Organization organization,
                       @AuthenticationPrincipal User user,
                       RedirectAttributes redirectAttributes,
                       HttpServletRequest request) {
        try {
            userService.subscribeToOrganization(organization, user);
        } catch (DuplicateRecordException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:" + request.getHeader("Referer");
    }
}

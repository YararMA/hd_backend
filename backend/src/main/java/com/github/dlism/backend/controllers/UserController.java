package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("user", userService.getById(user.getId()));
        return "forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user,
                       @Valid @ModelAttribute("user") UserDto userDto,
                       BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "forms/editUserProfile";
        }

        try {
            userService.update(user, userDto);
            model.addAttribute("user", userService.getById(user.getId()));
            model.addAttribute("updateSuccess", "Данные успешно обновлены");
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
        }
        return "forms/editUserProfile";
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

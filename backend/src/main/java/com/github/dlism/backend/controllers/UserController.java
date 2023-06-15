package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.mappers.UserMapper;
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
        model.addAttribute("user", UserMapper.INSTANCE.entityToDto(user));
        return "forms/editUserProfile";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal User user, @Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            return "forms/editUserProfile";
        }

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

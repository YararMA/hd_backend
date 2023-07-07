package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.user.UserUpdateProfileDto;
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

package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/organization-list")
    public String organizationList(Model model) {
        model.addAttribute("organizations", organizationService.getAllActiveOrganizations());
        return "organization/list";
    }

    @GetMapping("/organization-list/{id}")
    public String organizationPage(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        return "organization/profile";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("registrationForm", new UserDto());
        return "forms/registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("registrationForm") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/registration";
        }

        try {
            userService.createUser(userDto);
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
            return "forms/registration";
        } catch (IllegalArgumentException e) {
            model.addAttribute("passIsNotConfirm", e.getMessage());
            return "forms/registration";
        }

        return "redirect:login";
    }

    @GetMapping("/access-exception")
    public String accessException() {
        return "exceptions/access-exception";
    }
}

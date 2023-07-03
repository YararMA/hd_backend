package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.user.UserDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.OrganizationNotFoundException;
import com.github.dlism.backend.exceptions.UserNotFoundException;
import com.github.dlism.backend.models.Organization;
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
        try {
            Organization organization = organizationService.getById(id);
            model.addAttribute("organization", organization);
        } catch (OrganizationNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "helpers/not-found";
        }
        return "organization/view";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("registrationForm", new UserDto());
        return "forms/registration";
    }

    @GetMapping("/registration-organization")
    public String organizationRegistrationPage(Model model) {
        model.addAttribute("registrationForm", new UserDto());
        return "forms/registration-organization";
    }

    @PostMapping("/registration-organization")
    public String registrationOrganization(@Valid @ModelAttribute("registrationForm") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/registration-organization";
        }

        try {
            userService.createOrganizer(userDto);
        } catch (DuplicateRecordException e) {
            model.addAttribute("userExists", e.getMessage());
            return "forms/registration-organization";
        } catch (IllegalArgumentException e) {
            model.addAttribute("passIsNotConfirm", e.getMessage());
            return "forms/registration-organization";
        }

        return "redirect:login";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("registrationForm") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/registration";
        }

        try {
            userService.create(userDto);
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
        return "helpers/access-exception";
    }

    @GetMapping("/active/{code}")
    public String activationUser(@PathVariable("code") String code, Model model) {
        try {
            if (userService.active(code).isPresent()) {
                model.addAttribute("message", "Ваш аккаунт успешно активировань");
            } else {
                model.addAttribute("message", "Код активации не найден");
            }
        }catch (UserNotFoundException e){
            model.addAttribute("message", e.getMessage());
        }

        return "helpers/activation-user";
    }
}

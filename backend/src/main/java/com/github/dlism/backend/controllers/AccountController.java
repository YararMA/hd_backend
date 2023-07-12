package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.dto.user.UserUpdatePasswordDto;
import com.github.dlism.backend.dto.user.UserUpdateProfileDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.OrganizationNotFoundException;
import com.github.dlism.backend.exceptions.UpdateException;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.DictionaryService;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final OrganizationService organizationService;
    private final DictionaryService dictionaryService;

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

    @GetMapping(value = {"/organization", "/organization/"})
    public String profile(@AuthenticationPrincipal User user, Model model) {
        Optional<OrganizationDto> organization = organizationService.searchOrganization(user);

        if (organization.isPresent()) {
            model.addAttribute("organization", organization.orElseGet(OrganizationDto::new));
            return "account/organization/index";
        }
        return "redirect:/organization/create";
    }

    @GetMapping("/organization/edit")
    public String editForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("organizationForm", organizationService.searchOrganization(user));
        model.addAttribute("types", dictionaryService.organizationType());

        return "account/organization/forms/editOrganizationProfile";
    }

    @PostMapping("/organization/edit")
    public String edit(
            @AuthenticationPrincipal User user,
            @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            Model model) {
        model.addAttribute("types", dictionaryService.organizationType());
        try {
            model.addAttribute("organizationForm", organizationService.update(user, organizationDto));
            model.addAttribute("organizationCreateSuccess", "Организация успешно обновлена");
        } catch (DuplicateRecordException | OrganizationNotFoundException e) {
            model.addAttribute("organizationExists", e.getMessage());
        }
        return "account/organization/forms/editOrganizationProfile";
    }
}

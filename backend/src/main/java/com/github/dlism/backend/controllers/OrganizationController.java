package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        OrganizationDto organization = organizationService.searchOrganization(user);

        if (organization != null) {
            model.addAttribute("organization", organization);
        } else {
            return "redirect:/organization/create";
        }
        return "organization/profile";
    }

    @GetMapping("/create")
    public String index(@AuthenticationPrincipal User user, Model model) {

        if (userService.hasOrganization(user)) {
            model.addAttribute("organizationExists", "Организация уже создана!");
            return "redirect:/organization";

        }

        model.addAttribute("organizationForm", new OrganizationDto());
        return "forms/organization";
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "forms/organization";
        }

        try {
            organizationService.create(organizationDto, user);
        } catch (DuplicateRecordException e) {
            model.addAttribute("organizationExists", e.getMessage());
            return "forms/organization";
        }
        return "redirect:/organization";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("organizationForm", organizationService.searchOrganization(user));
        return "forms/editOrganizationProfile";
    }

    @PostMapping("/edit")
    public String edit(
            @AuthenticationPrincipal User user,
            @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            Model model) {

        try {
            model.addAttribute("organizationForm", organizationService.update(user, organizationDto));
            model.addAttribute("organizationCreateSuccess", "Организация успешно обновлена");
        } catch (DuplicateRecordException | IllegalArgumentException e) {
            model.addAttribute("organizationExists", e.getMessage());
        }
        return "forms/editOrganizationProfile";
    }

}
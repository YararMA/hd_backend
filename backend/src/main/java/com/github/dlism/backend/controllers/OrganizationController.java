package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        Optional<OrganizationDto> organization = organizationService.searchOrganization(user);

        if (organization.isPresent()) {
            model.addAttribute("organization", organization);
            return "organization/profile";
        }
        return "redirect:/organization/create";
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

    @GetMapping("/join/{organization}")
    public String join(@PathVariable Organization organization, @AuthenticationPrincipal User user){
        try {
            userService.subscribeToOrganization(organization, user);
        }catch (DuplicateRecordException e){
            e.printStackTrace();
        }
        return "redirect:/organization-list";
    }
}
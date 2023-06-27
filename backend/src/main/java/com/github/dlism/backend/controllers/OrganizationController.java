package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.OrganizationNotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            model.addAttribute("organization", organization.orElseGet(OrganizationDto::new));
            return "organization/profile";
        }
        return "redirect:/organization/create";
    }

    @GetMapping("/create")
    public String index(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {

        if (userService.hasOrganization(user)) {
            redirectAttributes.addFlashAttribute("organizationExists", "Организация уже создана!");
            return "redirect:/organization";
        }

        model.addAttribute("organizationForm", new OrganizationDto());
        return "organization/forms/create";
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "organization/forms/create";
        }

        try {
            organizationService.create(organizationDto, user);
        } catch (DuplicateRecordException e) {
            model.addAttribute("organizationExists", e.getMessage());
            return "organization/forms/create";
        }
        return "redirect:/organization";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("organizationForm", organizationService.searchOrganization(user));
        return "organization/forms/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @AuthenticationPrincipal User user,
            @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            Model model) {

        try {
            model.addAttribute("organizationForm", organizationService.update(user, organizationDto));
            model.addAttribute("organizationCreateSuccess", "Организация успешно обновлена");
        } catch (DuplicateRecordException | OrganizationNotFoundException e) {
            model.addAttribute("organizationExists", e.getMessage());
        }
        return "organization/forms/edit";
    }

}
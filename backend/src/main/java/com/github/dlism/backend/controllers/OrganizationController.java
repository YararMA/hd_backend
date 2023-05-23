package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/create")
    public String index(@AuthenticationPrincipal User user, Model model) {

        if (userService.hasOrganization(user)) {
            //TODO Если организация уже создана то перенаправить на страницу редактирования организации
            model.addAttribute("organizationExists", "Организация уже создана!");
        }

        model.addAttribute("organizationForm", new OrganizationDto());
        return "forms/organization";
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal User user,
            @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            Model model) {

        if (!organizationService.create(organizationDto, user)) {
            model.addAttribute("organizationExists", "Организация с такой названием уже существует!");
            return "forms/organization";
        }
        return "forms/organization";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        OrganizationDto organization = organizationService.searchOrganization(user);

        model.addAttribute("organization", organization);

        return "organization/profile";
    }
}

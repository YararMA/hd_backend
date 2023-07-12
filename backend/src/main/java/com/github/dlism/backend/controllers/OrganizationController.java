package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.services.DictionaryService;
import com.github.dlism.backend.services.OrganizationService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final DictionaryService dictionaryService;

    @GetMapping("/create")
    public String index(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {

        if (organizationService.existsOrganizationsByAuth(user)) {
            redirectAttributes.addFlashAttribute("organizationExists", "Организация уже создана!");
            return "redirect:/account/organization";
        }

        model.addAttribute("organizationForm", new OrganizationDto());
        model.addAttribute("types", dictionaryService.organizationType());
        return "organization/forms/create";
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("organizationForm") OrganizationDto organizationDto,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("types", dictionaryService.organizationType());

        if (bindingResult.hasErrors()) {
            return "organization/forms/create";
        }

        try {
            organizationService.create(organizationDto, user);
        } catch (DuplicateRecordException e) {
            model.addAttribute("organizationExists", e.getMessage());
            return "organization/forms/create";
        }
        return "redirect:/account/organization";
    }
}
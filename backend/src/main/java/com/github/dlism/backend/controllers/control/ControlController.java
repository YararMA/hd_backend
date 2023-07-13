package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.services.DictionaryService;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/control")
@RequiredArgsConstructor
public class ControlController {
    private final UserService userService;

    private final OrganizationService organizationService;

    private final DictionaryService dictionaryService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("organizationCount", organizationService.count());
        return "control/index";
    }

    @GetMapping("/users")
    public String userList(@PageableDefault(value = 10, page = 0) Pageable pageable, Model model) {
        model.addAttribute("users", userService.getAllUsers(pageable));
        return "control/user-list";
    }

    @GetMapping("/organizations")
    public String organizationList(@PageableDefault(value = 10, page = 0) Pageable pageable, Model model) {
        model.addAttribute("organizations", organizationService.getAllOrganizations(pageable));
        return "control/organization/list";
    }

    @PostMapping("/organizations/active")
    public String activeOrganization(@RequestParam("id") Long id) {
        organizationService.active(id);
        return "redirect:/control/organizations";
    }

    @GetMapping("/organizations/edit/{id}")
    public String editOrganization(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        model.addAttribute("types", dictionaryService.organizationType());
        return "control/organization/edit";
    }

    @PostMapping("/organizations/edit/{id}")
    public String editOrganization(
            @PathVariable Long id,
            Model model,
            @ModelAttribute("organization") OrganizationDto organizationDto) {
        model.addAttribute("organization", organizationService.update(id, organizationDto));
        model.addAttribute("types", dictionaryService.organizationType());
        return "control/organization/edit";
    }

    @GetMapping("/organizations/{id}")
    public String organizationView(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        return "control/organization/view";
    }

}

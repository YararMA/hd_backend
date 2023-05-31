package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/control")
public class ControlController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("organizationCount", organizationService.count());
        return "control/index";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "control/user-list";
    }

    @GetMapping("/organizations")
    public String organizationList(Model model) {
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        return "control/organization/list";
    }

    @PostMapping("/organizations/active")
    public String activeOrganization(@RequestParam("id") Long id){
        organizationService.active(id);
        return "redirect:/control/organizations";
    }

    @GetMapping("/organizations/edit/{id}")
    public String editOrganization(@PathVariable Long id, Model model){
        model.addAttribute("organization", organizationService.getById(id));
        return "control/organization/edit";
    }

    @PostMapping("/organizations/edit/{id}")
    public String editOrganization(
            @PathVariable Long id,
            Model model,
            @ModelAttribute("organization") OrganizationDto organizationDto){

        model.addAttribute("organization", organizationService.update(id, organizationDto));
        return "control/organization/edit";
    }
}

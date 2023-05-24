package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "control/organization/organization-list";
    }

    @PostMapping("/organizations/active")
    public String activeOrganization(@RequestParam("id") Long id){
        organizationService.active(id);
        return "redirect:/control/organizations";
    }
}

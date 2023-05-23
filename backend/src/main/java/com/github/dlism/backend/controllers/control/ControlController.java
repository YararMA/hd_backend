package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/control")
public class ControlController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;
    @GetMapping("")
    public String index(Model model){

        model.addAttribute("userCount", userService.count());
        model.addAttribute("organizationCount", organizationService.count());
        return "control/index";
    }
}

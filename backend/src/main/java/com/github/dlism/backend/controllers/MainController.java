package com.github.dlism.backend.controllers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("registrationForm", new UserDto());
        return "forms/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registrationForm") UserDto userDto, Model model) {

        if(!userDto.getPassword().equals(userDto.getPasswordConfirmation())){
            model.addAttribute("passIsNotConfirm", "Пароли не совпадают!");
            return "forms/registration";
        }

        if (!userService.createUser(userDto)) {
            model.addAttribute("userExists", "Пользовател уже существует!");
            return "forms/registration";
        }

        return "redirect:login";
    }
}

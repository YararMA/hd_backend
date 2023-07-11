package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.dto.event.EventDto;
import com.github.dlism.backend.exceptions.ResourceNotFoundException;
import com.github.dlism.backend.services.EventService;
import com.github.dlism.backend.services.OrganizationService;
import com.github.dlism.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/control")
@RequiredArgsConstructor
public class ControlController {
    private final UserService userService;

    private final OrganizationService organizationService;

    private final EventService eventService;

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
    public String activeOrganization(@RequestParam("id") Long id) {
        organizationService.active(id);
        return "redirect:/control/organizations";
    }

    @GetMapping("/organizations/edit/{id}")
    public String editOrganization(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        return "control/organization/edit";
    }

    @PostMapping("/organizations/edit/{id}")
    public String editOrganization(
            @PathVariable Long id,
            Model model,
            @ModelAttribute("organization") OrganizationDto organizationDto) {

        model.addAttribute("organization", organizationService.update(id, organizationDto));
        return "control/organization/edit";
    }

    @GetMapping("/organizations/{id}")
    public String organizationView(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));

        return "control/organization/view";
    }

    @GetMapping("/event")
    public String event(Model model){
        model.addAttribute("events", eventService.getAll());

        return "control/event/list";
    }

    @GetMapping("/event/create")
    public String createEvent(Model model){
        model.addAttribute("eventForm", new EventDto());

        return "control/event/forms/create";
    }

    @PostMapping("/event/create")
    public String createEvent(@Valid @ModelAttribute("eventForm")  EventDto eventDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "control/event/forms/create";
        }
        model.addAttribute("eventForm", eventService.create(eventDto));

        return "control/event/forms/create";
    }

    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable("id") Long id, Model model){
        try {
            model.addAttribute("eventForm", eventService.getById(id));
        }catch (ResourceNotFoundException e){
            model.addAttribute("message", e.getMessage());
            return "control/event/list";
        }
        return "control/event/forms/edit";
    }

    @PostMapping("/event/edit/{id}")
    public String editEvent(@Valid @ModelAttribute("eventForm")  EventDto eventDto,
                            @PathVariable("id") Long id,
                            Model model
    ){
        try {
            model.addAttribute("eventForm", eventService.update(eventDto, id));
            model.addAttribute("message", "Событие успешно обновлен!");
        }catch (ResourceNotFoundException e){
            model.addAttribute("message", e.getMessage());
            return "control/event/list";
        }
        return "control/event/forms/edit";
    }
}

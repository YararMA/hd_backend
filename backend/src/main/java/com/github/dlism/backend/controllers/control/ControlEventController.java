package com.github.dlism.backend.controllers.control;

import com.github.dlism.backend.dto.event.EventDto;
import com.github.dlism.backend.exceptions.ResourceNotFoundException;
import com.github.dlism.backend.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/control/event")
public class ControlEventController {
    private final EventService eventService;

    @GetMapping("")
    public String event(Model model) {
        model.addAttribute("events", eventService.getAll());

        return "control/event/list";
    }

    @GetMapping("/create")
    public String createEvent(Model model) {
        model.addAttribute("eventForm", new EventDto());

        return "control/event/forms/create";
    }

    @PostMapping("/create")
    public String createEvent(@Valid @ModelAttribute("eventForm") EventDto eventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "control/event/forms/create";
        }
        model.addAttribute("eventForm", eventService.create(eventDto));

        return "control/event/forms/create";
    }

    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("eventForm", eventService.getById(id));
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "control/event/list";
        }
        return "control/event/forms/edit";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@Valid @ModelAttribute("eventForm") EventDto eventDto,
                            @PathVariable("id") Long id,
                            Model model
    ) {
        try {
            model.addAttribute("eventForm", eventService.update(eventDto, id));
            model.addAttribute("message", "Событие успешно обновлен!");
        } catch (ResourceNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "control/event/list";
        }
        return "control/event/forms/edit";
    }
}

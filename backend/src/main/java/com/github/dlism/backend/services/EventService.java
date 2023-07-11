package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.event.EventDto;
import com.github.dlism.backend.exceptions.ResourceNotFoundException;
import com.github.dlism.backend.mappers.EventMapper;
import com.github.dlism.backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public EventDto getById(Long id){
        return EventMapper.INSTANCE.entityToDto(
                eventRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Событие не найден"))
        );
    }
    public List<EventDto> getAll() {
        return EventMapper.INSTANCE.entityToDto(eventRepository.findAll());
    }

    public EventDto create(EventDto eventDto){
        return EventMapper.INSTANCE.entityToDto(
                eventRepository.save(EventMapper.INSTANCE.dtoToEntity(eventDto))
        );
    }

    public EventDto update(EventDto eventDto, Long eventId){
        eventDto.setId(eventId);
        return EventMapper.INSTANCE.entityToDto(
                eventRepository.save(EventMapper.INSTANCE.dtoToEntity(eventDto))
        );
    }
}

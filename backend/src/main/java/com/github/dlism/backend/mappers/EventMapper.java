package com.github.dlism.backend.mappers;

import com.github.dlism.backend.dto.event.EventDto;
import com.github.dlism.backend.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto entityToDto(Event event);

    List<EventDto> entityToDto(List<Event> event);

    Event dtoToEntity(EventDto eventDto);

}

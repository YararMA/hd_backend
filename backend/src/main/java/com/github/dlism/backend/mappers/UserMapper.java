package com.github.dlism.backend.mappers;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);
}

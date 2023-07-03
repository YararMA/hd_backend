package com.github.dlism.backend.mappers;

import com.github.dlism.backend.dto.user.UserDto;
import com.github.dlism.backend.dto.user.UserProfileDto;
import com.github.dlism.backend.dto.user.UserUpdateDto;
import com.github.dlism.backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);

    User dtoToEntity(UserUpdateDto userUpdateDto);

    List<UserProfileDto> entityToDto(List<User> users);
}

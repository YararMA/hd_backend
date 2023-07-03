package com.github.dlism.backend.mappers;

import com.github.dlism.backend.dto.user.UserDto;
import com.github.dlism.backend.dto.user.UserProfileDto;
import com.github.dlism.backend.dto.user.UserUpdateProfileDto;
import com.github.dlism.backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User user);

    UserUpdateProfileDto entityToUpdateProfileDto(User user);

    User dtoToEntity(UserDto userDto);

    User dtoToEntity(UserUpdateProfileDto userUpdateProfileDto);

    List<UserProfileDto> entityToDto(List<User> users);
}

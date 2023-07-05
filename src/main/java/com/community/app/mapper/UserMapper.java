package com.community.app.mapper;

import com.community.app.dto.UserDto;
import com.community.app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {

    UserDto userToUserDto(User user);

    @Mapping(target = "bookingList", ignore = true)
    User userDtoToUser(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingList", ignore = true)
    void updateUser(UserDto userDto, @MappingTarget User user);
}

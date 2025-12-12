package com.fooddelivery.users.mapper;

import com.fooddelivery.users.dto.*;
import com.fooddelivery.users.entity.User;
import org.mapstruct.*;


import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDto userToUserResponseDto(User user);
    @Mapping(target = "addresses", ignore = true)
    User userResponseDtoToUser(UserResponseDto userResponseDto);
    @Mapping(target = "addresses", ignore = true)
    User userRequestDtoToUser(UserRequestDto userRequestDto);
    List<UserResponseDto> userListToUserResponseDtoList(List<User> list);
    @Mapping(target = "addresses", ignore = true)
    List<User> UserResponseDtoListToUserList(List<UserResponseDto>list);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);
}

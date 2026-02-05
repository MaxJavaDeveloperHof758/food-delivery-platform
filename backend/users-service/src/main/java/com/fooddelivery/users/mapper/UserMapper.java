package com.fooddelivery.users.mapper;

import com.fooddelivery.users.dto.RoleResponseDto;
import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.entity.User;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = {RoleMapper.class,AddressMapper.class})
public interface UserMapper {
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "addresses", source = "addresses")
    UserResponseDto userToUserResponseDto(User user);
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

    /*default Set<RoleResponseDto> mapRolesSafely(User user) {
        if (user == null || user.getRoles() == null || !Hibernate.isInitialized(user.getRoles())) {
            return new HashSet<>();
        }
        RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
        return roleMapper.roleSetToRoleResponseDtoSet(user.getRoles());
    }*/

}

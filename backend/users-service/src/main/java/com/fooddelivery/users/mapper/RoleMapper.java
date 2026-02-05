package com.fooddelivery.users.mapper;

import com.fooddelivery.users.dto.RoleRequestDto;
import com.fooddelivery.users.dto.RoleResponseDto;
import com.fooddelivery.users.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    Role RoleRequestDtoToRole(RoleRequestDto roleRequestDto);
    RoleResponseDto roleToRoleResponseDto(Role role);
    List<RoleResponseDto> roleListToRoleResponseDtoList(List<Role>list);
    Set<RoleResponseDto> roleSetToRoleResponseDtoSet(Set<Role> roles);
}

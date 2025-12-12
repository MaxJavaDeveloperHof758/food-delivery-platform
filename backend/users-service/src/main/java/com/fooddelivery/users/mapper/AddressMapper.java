package com.fooddelivery.users.mapper;

import com.fooddelivery.users.dto.AddressRequestDto;
import com.fooddelivery.users.dto.AddressResponseDto;
import com.fooddelivery.users.dto.AddressSlimDto;
import com.fooddelivery.users.dto.RoleResponseDto;
import com.fooddelivery.users.entity.Address;
import com.fooddelivery.users.entity.Role;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address addressRequestDtoToAddress(AddressRequestDto addressRequestDto);
    AddressSlimDto addressToAddressSlimDto(Address address);
    List<AddressSlimDto> addressListToAddressSlimDtoList(List<Address> addresses);
    Address addressSlimDtoToAddress(AddressSlimDto addressSlimDto);
    Set<AddressSlimDto> addressSetToAddressSlimDtoSet(Set<Address>addresses);
    @Mapping(target="userId",source="user.id")
    @Mapping(target ="userFullName",source = "user.fullName")
    AddressResponseDto addressToAddressResponseDto(Address address);
    Address addressResponseDtoToAddress(AddressResponseDto addressResponseDto);
    List<AddressResponseDto> addressListToAddressResponseList(List<Address>list);
    List<Address> addressResponseDtosToAddresses (List<AddressResponseDto>list);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDto(AddressRequestDto dto, @MappingTarget Address address);
}

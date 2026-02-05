package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.AddressRequestDto;
import com.fooddelivery.users.dto.AddressResponseDto;
import com.fooddelivery.users.entity.Address;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.AddressNotFoundException;
import com.fooddelivery.users.exception.CustomAccessDeniedException;
import com.fooddelivery.users.exception.UserNotFoundException;
import com.fooddelivery.users.mapper.AddressMapper;

import com.fooddelivery.users.repository.AddressRepository;
import com.fooddelivery.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressResponseDto getAddressById(Long id) {
        return addressMapper.addressToAddressResponseDto(addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id)));
    }

    public List<AddressResponseDto> getAllAddresses() {
        return addressMapper.addressListToAddressResponseList(addressRepository.findAll());
    }

    public List<AddressResponseDto> getUserAddresses(Long userId) {
        return addressMapper.addressListToAddressResponseList(addressRepository.findByUserId(userId));
    }

    public List<AddressResponseDto> getAllAddressesByStreetAndUserId(String street,Long userId) {
        List<Address> addresses=addressRepository.findByStreetContainingAndUserId(street,userId);
        if(addresses.isEmpty()){
            throw new AddressNotFoundException("No addresses found on street "+street+" for current user");
        }
        return addressMapper.addressListToAddressResponseList(addresses);
    }

    public List<AddressResponseDto> getAllAddressesByCityAndUserId(String city,Long userId) {
        List<Address> addresses=addressRepository.findByCityContainingAndUserId(city,userId);
        if(addresses.isEmpty()){
            throw new AddressNotFoundException("No addresses found in city "+city+" for current user");
        }
        return addressMapper.addressListToAddressResponseList(addresses);
    }

    public List<AddressResponseDto> getAllAddressesByStateAndUserId(String state,Long userId) {
        List<Address> addresses=addressRepository.findByStateContainingAndUserId(state,userId);
        if(addresses.isEmpty()){
            throw new AddressNotFoundException("No addresses found in state "+state+" for current user");
        }
        return addressMapper.addressListToAddressResponseList(addresses);
    }

    public List<AddressResponseDto> getAllAddressesByCountryAndUserId(String country,Long userId) {
        List<Address> addresses=addressRepository.findByCountryContainingAndUserId(country,userId);
        if(addresses.isEmpty()){
            throw new AddressNotFoundException("No addresses found in country "+country+" for current user");
        }
        return addressMapper.addressListToAddressResponseList(addresses);
    }

    public AddressResponseDto createAddressForUser(Long userId, AddressRequestDto addressRequestDto) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    public AddressResponseDto updateAddressForUser(Long addressId, Long userId, AddressRequestDto addressRequestDto) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));
        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new CustomAccessDeniedException("Address does not belong to user with id: " + userId);
        }
        addressMapper.updateAddressFromDto(addressRequestDto, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.addressToAddressResponseDto(updatedAddress);
    }

    public void deleteAddressFromUser(Long addressId,Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomAccessDeniedException("Address does not belong to user with id: " + userId);
        }
        addressRepository.deleteById(addressId);
    }
}

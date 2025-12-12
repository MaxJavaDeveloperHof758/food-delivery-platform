package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.AddressRequestDto;
import com.fooddelivery.users.dto.AddressResponseDto;
import com.fooddelivery.users.entity.Address;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.AddressNotFoundException;
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

    public List<AddressResponseDto> getAllAddressesByStreet(String street) {
        return addressMapper.addressListToAddressResponseList(addressRepository.findAllByStreet(street));
    }

    public List<AddressResponseDto> getAllAddressesByCity(String city) {
        return addressMapper.addressListToAddressResponseList(addressRepository.findAllByCity(city));
    }

    public List<AddressResponseDto> getAllAddressesByState(String state) {
        return addressMapper.addressListToAddressResponseList(addressRepository.findAllByState(state));
    }

    public List<AddressResponseDto> getAllAddressesByCountry(String country) {
        return addressMapper.addressListToAddressResponseList(addressRepository.findAllByCountry(country));
    }

    public AddressResponseDto createAddressForUser(Long userId, AddressRequestDto addressRequestDto) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        User user = userRepository.findById(addressRequestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + addressRequestDto.getUserId()));
        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    public AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + id));
        addressMapper.updateAddressFromDto(addressRequestDto, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.addressToAddressResponseDto(updatedAddress);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}

package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.AddressRequestDto;
import com.fooddelivery.users.dto.AddressResponseDto;
import com.fooddelivery.users.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Address API", description = "API for managing addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Get all addresses existing",
            description = "Returns all the addresses existing in the database")
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses() {
        List<AddressResponseDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get one address by ID",
            description = "Returns one address by ID",
            parameters = {
                    @Parameter(name = "id", description = "Address ID", example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Address found")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDto> getAddressById(@PathVariable("id") Long id) {
        AddressResponseDto addressResponseDto = addressService.getAddressById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponseDto);
    }

    @Operation(summary = "Get all the addresses by user's ID",
            description = "Returns all the addresses that belong to the user",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByUserId(@PathVariable("userId") Long userId) {
        List<AddressResponseDto> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get all the addresses by street name",
            description = "Returns all the addresses by the name of street",
            parameters = {
                    @Parameter(name = "street", description = "The name of street", example = "Main street")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search/street")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByStreet(@RequestParam String street) {
        List<AddressResponseDto> adresses = addressService.getAllAddressesByStreet(street);
        return ResponseEntity.status(HttpStatus.OK).body(adresses);
    }

    @Operation(summary = "Get all the addresses by city name",
            description = "Returns all the addresses by the name of city",
            parameters = {
                    @Parameter(name = "city", description = "The name of city", example = "Boston")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search/city")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCity(@RequestParam String city) {
        List<AddressResponseDto> addresses = addressService.getAllAddressesByCity(city);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get all the addresses by state name",
            description = "Returns all the addresses by the name of state",
            parameters = {
                    @Parameter(name = "state", description = "The name of state", example = "Massachusetts")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search/state")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByState(@RequestParam String state) {
        List<AddressResponseDto> addresses = addressService.getAllAddressesByState(state);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get all the addresses by country name",
            description = "Returns all the addresses by the name of country",
            parameters = {
                    @Parameter(name = "country", description = "The name of country", example = "United States")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search/country")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCountry(@RequestParam String country) {
        List<AddressResponseDto> adresses = addressService.getAllAddressesByCountry(country);
        return ResponseEntity.status(HttpStatus.OK).body(adresses);
    }

    @Operation(summary = "Create a new address for user",
            description = "Creates a new address for user and saves it in the database",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", example = "1")
            })
    @ApiResponse(responseCode = "201", description = "Address was successfully created")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponseDto> createAddressForUser(@PathVariable("userId") Long userId,
                                                                   @RequestBody @Valid AddressRequestDto addressRequestDto) {
        AddressResponseDto createdAddress = addressService.createAddressForUser(userId, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @Operation(summary = "Update existing address",
            description = "Updates already existing address in the database",
            parameters = {
                    @Parameter(name = "id", description = "Address ID", example = "1")
            })
    @ApiResponse(responseCode = "204", description = "Address was successfully updated")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @PutMapping("/{addressId}/users/{userId}")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable("addressId") Long addressId,
                                                            @PathVariable("userId") Long userId,
                                                            @RequestBody @Valid AddressRequestDto addressRequestDto) {
        AddressResponseDto updatedAddress = addressService.updateAddressForUser(addressId,userId,addressRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updatedAddress);
    }

    @Operation(summary = "Delete existing address",
            description = "Deletes existing address from the database",
            parameters = {
                    @Parameter(name = "id", description = "Address ID", example = "1",required = true)
            })
    @ApiResponse(responseCode = "204", description = "Address was deleted")
    @ApiResponse(responseCode = "404",description = "Address not found")
    @ApiResponse(responseCode = "400",description = "Invalid input format")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{addressId}/users/{userId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Long addressId,
                                              @PathVariable("userId") Long userId) {
        addressService.deleteAddressFromUser(addressId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

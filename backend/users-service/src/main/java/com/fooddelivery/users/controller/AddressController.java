package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.AddressRequestDto;
import com.fooddelivery.users.dto.AddressResponseDto;
import com.fooddelivery.users.security.UserPrincipal;
import com.fooddelivery.users.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Address API", description = "API for managing addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Get all addresses that belong to the user",
            description = "Returns all the addresses that belong to the user")
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-addresses")
    public ResponseEntity<List<AddressResponseDto>> getMyAddresses(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<AddressResponseDto> addresses = addressService.getUserAddresses(userPrincipal.getId());
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Get all addresses existing (ADMIN endpoint)",
            description = "Returns all the addresses existing in the database")
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses() {
        List<AddressResponseDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get one address by ID (ADMIN endpoint)",
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

    @Operation(summary = "Get all the addresses by user's ID (ADMIN endpoint)",
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

    @Operation(summary = "Get all the addresses by street name that belong to the user",
            description = "Returns all the addresses by street name that belong to the user",
            parameters = {
                    @Parameter(name = "street", description = "The name of street", example = "Main street")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/street")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByStreet(Authentication authentication,
                                                                         @RequestParam String street) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        List<AddressResponseDto> addresses = addressService.getAllAddressesByStreetAndUserId(street, userId);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Get all the addresses by city name",
            description = "Returns all the addresses by the name of city",
            parameters = {
                    @Parameter(name = "city", description = "The name of city", example = "Boston")
            })
    @ApiResponse(responseCode = "200", description = "Addresses found")
    @ApiResponse(responseCode = "404", description = "Addresses not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/city")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCity(Authentication authentication,
                                                                       @RequestParam String city) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        List<AddressResponseDto> addresses = addressService.getAllAddressesByCityAndUserId(city, userId);
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/state")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByState(Authentication authentication,
                                                                        @RequestParam String state) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        List<AddressResponseDto> addresses = addressService.getAllAddressesByStateAndUserId(state, userId);
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/country")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCountry(Authentication authentication,
                                                                          @RequestParam String country) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        List<AddressResponseDto> addresses = addressService.getAllAddressesByCountryAndUserId(country, userId);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @Operation(summary = "Create a new address for user",
            description = "Creates a new address for user and saves it in the database")
    @ApiResponse(responseCode = "201", description = "Address was successfully created")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(Authentication authentication,
                                                            @RequestBody @Valid AddressRequestDto addressRequestDto) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        AddressResponseDto createdAddress = addressService.createAddressForUser(userId, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @Operation(summary = "Update existing address",
            description = "Updates already existing address in the database",
            parameters = {
                    @Parameter(name = "addressId", description = "Address ID", example = "1")
            })
    @ApiResponse(responseCode = "204", description = "Address was successfully updated")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{addressId}/users")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable("addressId") Long addressId,
                                                            Authentication authentication,
                                                            @RequestBody @Valid AddressRequestDto addressRequestDto) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        AddressResponseDto updatedAddress = addressService.updateAddressForUser(addressId, userId, addressRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updatedAddress);
    }

    @Operation(summary = "Delete existing address",
            description = "Deletes existing address from the database",
            parameters = {
                    @Parameter(name = "id", description = "Address ID", example = "1", required = true)
            })
    @ApiResponse(responseCode = "204", description = "Address was deleted")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "400", description = "Invalid input format")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{addressId}/users")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Long addressId,
                                              Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        addressService.deleteAddressFromUser(addressId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

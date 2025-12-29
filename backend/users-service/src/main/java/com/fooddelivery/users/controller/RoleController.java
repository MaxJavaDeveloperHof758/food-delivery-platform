package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.RoleRequestDto;
import com.fooddelivery.users.dto.RoleResponseDto;
import com.fooddelivery.users.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role API", description = "API for managing roles")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Get all roles existing",
            description = "Returns all the roles existing in the database")
    @ApiResponse(responseCode = "200", description = "Roles found")
    @ApiResponse(responseCode = "400", description = "Invalid input format")
    @ApiResponse(responseCode = "404", description = "Roles not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @Operation(summary = "Get role by ID",
            description = "Returns the role by ID",
            parameters = {
                    @Parameter(name = "id", description = "Role ID", example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Role found")
    @ApiResponse(responseCode = "400", description = "Invalid input format")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable("id") Long id) {
        RoleResponseDto role = roleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @Operation(summary = "Get role by name",
            description = "Returns the role by name",
            parameters = {
                    @Parameter(name = "name", description = "Role name", example = "USER")
            })
    @ApiResponse(responseCode = "200", description = "Role found")
    @ApiResponse(responseCode = "400", description = "Invalid input format")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/name")
    public ResponseEntity<RoleResponseDto> getRoleByName(@RequestParam String name) {
        RoleResponseDto role = roleService.findByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @Operation(summary = "Create a new role",
            description = "Creates the role and saves it in the database")
    @ApiResponse(responseCode = "201", description = "Role was successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input format")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/create")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRequestDto roleRequestDto) {
        RoleResponseDto createdRole = roleService.createRole(roleRequestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @Operation(summary = "Update existing role", description = "Updates existing role by ID")
    @ApiResponse(responseCode = "200", description = "Role updated")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable("id") Long id,
                                                      @RequestBody @Valid RoleRequestDto roleRequestDto) {
        RoleResponseDto updatedRole = roleService.updateRole(id, roleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @Operation(summary = "Delete existing role",
            description = "Deletes existing role by ID",
    parameters = {
            @Parameter(name = "id",description = "Role ID",example = "1")
    })
    @ApiResponse(responseCode = "204", description = "Role deleted successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

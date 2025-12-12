package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.RoleRequestDto;
import com.fooddelivery.users.dto.RoleResponseDto;
import com.fooddelivery.users.entity.Role;
import com.fooddelivery.users.exception.ResourceAlreadyExistsException;
import com.fooddelivery.users.exception.RoleNotFoundException;
import com.fooddelivery.users.mapper.RoleMapper;
import com.fooddelivery.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleResponseDto> getAllRoles(){
        return roleMapper.roleListToRoleResponseDtoList(roleRepository.findAll());
    }
    public RoleResponseDto findByName(String name){
        return roleMapper.roleToRoleResponseDto(roleRepository.findByName(name)
                .orElseThrow(()->new RoleNotFoundException("Role not found: "+name)));
    }
    public RoleResponseDto findById(Long id){
        return roleMapper.roleToRoleResponseDto(roleRepository.findById(id)
                .orElseThrow(()->new RoleNotFoundException("Role not found with id: "+id)));
    }
    public RoleResponseDto createRole(String name){
        if(roleRepository.existsByName(name)){
            throw new ResourceAlreadyExistsException("Role already exists: "+name);
        }
        Role role=new Role();
        role.setName(name);
        roleRepository.save(role);
        return roleMapper.roleToRoleResponseDto(role);
    }
    public RoleResponseDto updateRole(Long roleId, RoleRequestDto roleRequestDto) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id " + roleId));
        existingRole.setName(roleRequestDto.getName());
        return roleMapper.roleToRoleResponseDto(existingRole);
    }
    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }
    public Role getOrCreateRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(name);
                    return roleRepository.save(newRole);
                });
    }
}

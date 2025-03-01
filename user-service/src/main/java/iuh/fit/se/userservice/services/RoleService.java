package iuh.fit.se.userservice.services;

import iuh.fit.se.userservice.entities.Role;

import java.util.List;

public interface RoleService {
    void saveRole(Role role);
    List<Role> getAllRoles();
    Role getRoleByCode(String roleCode);
}

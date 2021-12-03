package web.service;

import web.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    void saveRole(Role role);

    Role getRole(Long id);

    void deleteRole(Long id);

    Role getByRoleName(String name);
}
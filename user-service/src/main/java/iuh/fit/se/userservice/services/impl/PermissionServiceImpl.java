package iuh.fit.se.userservice.services.impl;

import iuh.fit.se.userservice.entities.Permission;
import iuh.fit.se.userservice.repositories.PermissionRepository;
import iuh.fit.se.userservice.services.PermissionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    @Modifying
    @Override
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }
}

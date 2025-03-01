package iuh.fit.se.userservice;

import iuh.fit.se.userservice.configs.RSAKeyRecord;
import iuh.fit.se.userservice.entities.Permission;
import iuh.fit.se.userservice.entities.Role;
import iuh.fit.se.userservice.entities.User;
import iuh.fit.se.userservice.enums.PermissionType;
import iuh.fit.se.userservice.enums.RoleType;
import iuh.fit.se.userservice.services.PermissionService;
import iuh.fit.se.userservice.services.RoleService;
import iuh.fit.se.userservice.services.UserService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.Set;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    private void initData(PasswordEncoder passwordEncoder,
                          PermissionService permissionService,
                          RoleService roleService,
                          UserService userService) {

        Permission permissionRead = Permission.builder()
                .code(PermissionType.PERMISSION_READ.name())
                .name(PermissionType.PERMISSION_READ.name())
                .build();
        permissionService.savePermission(permissionRead);

        Permission permissionWrite = Permission.builder()
                .code(PermissionType.PERMISSION_WRITE.name())
                .name(PermissionType.PERMISSION_WRITE.name())
                .build();
        permissionService.savePermission(permissionWrite);

        Permission permissionDelete = Permission.builder()
                .code(PermissionType.PERMISSION_DELETE.name())
                .name(PermissionType.PERMISSION_DELETE.name())
                .build();
        permissionService.savePermission(permissionDelete);

        Permission permissionUpdate = Permission.builder()
                .code(PermissionType.PERMISSION_UPDATE.name())
                .name(PermissionType.PERMISSION_UPDATE.name())
                .build();
        permissionService.savePermission(permissionUpdate);

        Role roleUser = Role.builder()
                .code(RoleType.ROLE_USER.name())
                .name(RoleType.ROLE_USER.name())
                .permissions(Set.of(permissionRead))
                .build();
        roleService.saveRole(roleUser);

        Role roleAdmin = Role.builder()
                .code(RoleType.ROLE_ADMIN.name())
                .name(RoleType.ROLE_ADMIN.name())
                .permissions(Set.of(permissionRead, permissionWrite, permissionUpdate))
                .build();
        roleService.saveRole(roleAdmin);

        Role roleSuperAdmin = Role.builder()
                .code(RoleType.ROLE_SUPER_ADMIN.name())
                .name(RoleType.ROLE_SUPER_ADMIN.name())
                .permissions(Set.of(permissionRead, permissionWrite, permissionUpdate, permissionDelete))
                .build();
        roleService.saveRole(roleSuperAdmin);


        User user = User.builder()
                .userName("user")
                .email("user@user.com")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .roles(Set.of(roleUser))
                .build();
        userService.saveUser(user);

        User admin = User.builder()
                .userName("admin")
                .email("admin@admin.com")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .roles(Set.of(roleAdmin))
                .build();
        userService.saveUser(admin);

        User superAdmin = User.builder()
                .userName("superadmin")
                .email("superadmin@superadmin.com")
                .password(passwordEncoder.encode("password"))
                .enabled(true)
                .roles(Set.of(roleSuperAdmin))
                .build();
        userService.saveUser(superAdmin);
    }

    @Bean
    public CommandLineRunner runner(PasswordEncoder passwordEncoder, PermissionService permissionService, RoleService roleService, UserService userService) {
        return (args) -> {
//            initData(passwordEncoder, permissionService, roleService, userService);
        };

        //[Role(id=null, code=ROLE_USER, name=ROLE_USER, permissions=[Permission(id=null, code=PERMISSION_READ, name=PERMISSION_READ)])]
    }
}

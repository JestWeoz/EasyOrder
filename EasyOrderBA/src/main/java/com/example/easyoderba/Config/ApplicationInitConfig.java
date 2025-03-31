package com.example.easyoderba.Config;

import java.util.HashSet;
import java.util.Set;

import com.example.easyoderba.Model.Entity.AuthEntity.Permission;
import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import com.example.easyoderba.Model.Entity.AuthEntity.User;
import com.example.easyoderba.Repository.PermissionRepository;
import com.example.easyoderba.Repository.RoleRepository;
import com.example.easyoderba.Repository.UserRepository;
import com.example.easyoderba.constant.PredefinedPermission;
import com.example.easyoderba.constant.PredefinedRole;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean

    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIEW_PERMISSION)
                        .description("View permission")
                        .build()));
                permissions.add(permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.EDIT_PERMISSION)
                        .description("Edit permission")
                        .build()));
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.STAFF_ROLE)
                        .description("Staff role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.MANAGER_ROLE)
                        .permissions(permissions)
                        .description("Manager role")
                        .build());
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .permissions(permissions)
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);


                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}

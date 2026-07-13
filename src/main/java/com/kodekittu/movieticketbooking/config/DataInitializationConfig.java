package com.kodekittu.movieticketbooking.config;

import com.kodekittu.movieticketbooking.constant.SecurityConstants;
import com.kodekittu.movieticketbooking.entity.Role;
import com.kodekittu.movieticketbooking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final RoleRepository roleRepository;

    @Bean
    public ApplicationRunner roleInitializer() {
        return args -> initializeRoles();
    }

    @Transactional
    public void initializeRoles() {
        List.of(SecurityConstants.ROLE_ADMIN, SecurityConstants.ROLE_CUSTOMER).forEach(roleName ->
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                }));
    }
}

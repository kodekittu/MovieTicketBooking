package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);
}

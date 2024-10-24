package com.ygtech.employeeManagementSystem.modules.role;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("SELECT e.id FROM employee e WHERE e.role.id = :roleId")
    List<Integer> findAllByRoleId(UUID roleId);

}

package com.ygtech.employeeManagementSystem.modules.department;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {

    @Query("SELECT e.id FROM employee e WHERE e.department.id = :departmentId")
    List<Integer> findAllByDepartmentId(UUID departmentId);
    
    List<DepartmentEntity> findByManagerId(Integer id);

}

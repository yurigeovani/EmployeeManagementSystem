package com.ygtech.employeeManagementSystem.modules.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

	long countByDepartmentId(UUID departmentId);
	long countByRoleId(UUID roleId);
	List<EmployeeEntity> findByDepartmentId(UUID departmentId);
	List<EmployeeEntity> findByRoleId(UUID roleId);

}

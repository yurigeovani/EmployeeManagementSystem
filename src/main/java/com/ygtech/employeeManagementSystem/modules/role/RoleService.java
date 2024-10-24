package com.ygtech.employeeManagementSystem.modules.role;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ygtech.employeeManagementSystem.modules.employee.EmployeeRepository;

@Service
public class RoleService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	public Boolean hasEmployee(UUID id) {
		if(employeeRepository.countByRoleId(id) > 0) {
			return true;
		} else {
			return false;
		}
	}
}

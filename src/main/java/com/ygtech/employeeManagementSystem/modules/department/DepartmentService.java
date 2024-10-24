package com.ygtech.employeeManagementSystem.modules.department;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ygtech.employeeManagementSystem.modules.employee.EmployeeRepository;

@Service
public class DepartmentService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	public boolean hasEmployees(UUID id) {
		return employeeRepository.countByDepartmentId(id) > 0;
	}
}

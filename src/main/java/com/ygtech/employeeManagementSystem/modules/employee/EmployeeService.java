package com.ygtech.employeeManagementSystem.modules.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ygtech.employeeManagementSystem.modules.department.DepartmentRepository;

@Service
public class EmployeeService {

	@Autowired
	DepartmentRepository departmentRepository;
	
	public boolean isManager(Integer id) {
		List<UUID> departmentManagedById = 
				departmentRepository.findByManagerId(id)
					.stream()
					.map(d -> d.getId())
					.toList();
		if(departmentManagedById.isEmpty()) {
			return false;			
		}
		return true;
	}
}

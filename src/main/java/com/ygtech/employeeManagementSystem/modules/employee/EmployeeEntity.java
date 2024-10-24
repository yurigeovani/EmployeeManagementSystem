package com.ygtech.employeeManagementSystem.modules.employee;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ygtech.employeeManagementSystem.modules.department.DepartmentEntity;
import com.ygtech.employeeManagementSystem.modules.role.RoleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "employee")
public class EmployeeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 100, nullable = false)
	private String name;

	private int age;

	@Column(length = 100, nullable = false)
	private String email;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id", nullable = false)
	private DepartmentEntity department;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private RoleEntity role;
	
	//FIXME
	// private boolean isActive;
	// private LocalDateTime updatedAt;
	// private Double? salary;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
}

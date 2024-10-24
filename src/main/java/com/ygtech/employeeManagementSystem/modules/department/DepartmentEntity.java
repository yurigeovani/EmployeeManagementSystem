package com.ygtech.employeeManagementSystem.modules.department;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.ygtech.employeeManagementSystem.modules.employee.EmployeeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "department")
public class DepartmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(length = 100, nullable = false)
    private String name;
	
	private String description;

	private Integer managerId;
	
	@Column(length = 100)
    private String location;
	
	@OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
	private List<EmployeeEntity> employees;
        
	@CreationTimestamp
	private LocalDateTime createdAt;

	public DepartmentEntity(DepartmentPayload payload) {
		this.name = payload.name();
		this.description = payload.description();
		this.managerId = payload.managerId();
		this.location = payload.location();
	}
}

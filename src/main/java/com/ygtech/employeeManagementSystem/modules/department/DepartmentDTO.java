package com.ygtech.employeeManagementSystem.modules.department;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
	private UUID id;
	private String name;
	private String description;
	private Integer managerId;
	private String location;
	private LocalDateTime createdAt;
}

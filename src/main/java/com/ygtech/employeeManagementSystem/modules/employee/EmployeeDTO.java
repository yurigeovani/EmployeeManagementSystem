package com.ygtech.employeeManagementSystem.modules.employee;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeDTO(Integer id, String name, int age, String email, UUID departmentId, UUID roleId, LocalDateTime createdAt) {

}
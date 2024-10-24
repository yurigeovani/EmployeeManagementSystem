package com.ygtech.employeeManagementSystem.modules.department;

public record DepartmentPayload (String name, String description, Integer managerId, String location) {
}

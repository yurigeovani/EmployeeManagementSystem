package com.ygtech.employeeManagementSystem.modules.employee;

import java.util.UUID;

public record EmployeePayload(String name, Integer age, String email, UUID departmentId, UUID roleId) {

}

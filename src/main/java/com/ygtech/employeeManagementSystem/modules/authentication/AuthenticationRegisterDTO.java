package com.ygtech.employeeManagementSystem.modules.authentication;

import com.ygtech.employeeManagementSystem.modules.user.UserRole;

public record AuthenticationRegisterDTO (String login, String password, UserRole role) {

}

package com.ygtech.employeeManagementSystem.modules.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	public UserDetails findByLogin(String login);
	public boolean existsByLogin(String login);
	
	@Transactional
	public void deleteByLogin(String login);
}

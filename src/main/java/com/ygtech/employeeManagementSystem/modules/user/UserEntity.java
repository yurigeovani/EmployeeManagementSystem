package com.ygtech.employeeManagementSystem.modules.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(length = 100)
	private String login;

	@Column(length = 100)
	private String password;

	private UserRole role;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.role == UserRole.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));			
		} else{
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));			
		}
	}
	
	@Override
	public String getUsername() {
		return login;
	}

	public UserEntity(String login, String password, UserRole role) {
		this.login = login;
		this.password = password;
		this.role = role;
	}
}

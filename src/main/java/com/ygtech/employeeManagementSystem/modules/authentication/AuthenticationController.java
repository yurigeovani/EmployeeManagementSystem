package com.ygtech.employeeManagementSystem.modules.authentication;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ygtech.employeeManagementSystem.infra.security.TokenService;
import com.ygtech.employeeManagementSystem.modules.user.UserEntity;
import com.ygtech.employeeManagementSystem.modules.user.UserIdLoginDTO;
import com.ygtech.employeeManagementSystem.modules.user.UserRepository;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	TokenService tokenService;
	

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Validated AuthenticationDTO dto) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
		var auth = authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((UserEntity) auth.getPrincipal());
		
		return ResponseEntity.ok(new AuthenticationResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserEntity> register(@RequestBody @Validated AuthenticationRegisterDTO dto) {
		if(repository.findByLogin(dto.login()) != null) {
			System.out.println();
			return ResponseEntity.badRequest().build();
		}
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
		
		UserEntity newUser = new UserEntity(dto.login(), encryptedPassword, dto.role());
		
		this.repository.save(newUser);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/listAllLogins")
	public ResponseEntity<List<UserIdLoginDTO>> listAllLogins(){
		List<UserIdLoginDTO> allLogins = repository.findAll().stream().
				map(l -> new UserIdLoginDTO(l.getId(), l.getLogin()))
				.toList();
		return ResponseEntity.ok(allLogins);
	}
	
	@DeleteMapping("/deleteByLogin/{login}")
	public ResponseEntity<String> deleteByLogin(@PathVariable String login){
		if(repository.existsByLogin(login)) {
			repository.deleteByLogin(login);
			return ResponseEntity.ok().body(login);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> deleteById(@PathVariable UUID id){
		if(repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.ok().body(id.toString());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}

package com.ygtech.employeeManagementSystem.modules.role;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	RoleService roleService;
	
	private RoleDTO convertToDTO(RoleEntity role) {
		RoleDTO roleDTO = new RoleDTO(role.getId(), role.getName(), role.getDescription());
		return roleDTO;
	}

	@GetMapping
	public ResponseEntity<List<RoleDTO>> getAll () {
		return ResponseEntity.ok(roleRepository.findAll().stream().map(r -> convertToDTO(r)).toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleDTO> getById (@PathVariable UUID id) {
		return ResponseEntity.ok(convertToDTO(roleRepository.findById(id).get()));
	}
	
	@GetMapping("/{id}/employees")
	public ResponseEntity<List<Integer>> getAllEmployeesByRoleId (@PathVariable UUID id) {
		return ResponseEntity.ok(roleRepository.findAllByRoleId(id));
	}
	
	@PostMapping
	public ResponseEntity<RoleDTO> addRole(@RequestBody RolePayload payload) {
		RoleEntity newRole = new RoleEntity(payload);
		roleRepository.save(newRole);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(newRole));
		// Ou desta forma
		//return new ResponseEntity<>(convertToDTO(newRole), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<RoleDTO> updateROle(@PathVariable UUID id, @RequestBody RolePayload payload) {
		if(roleRepository.existsById(id)) {
			RoleEntity updatedRole = this.roleRepository.findById(id).get();
			if(payload.name() != null) {
				updatedRole.setName(payload.name());
			}
			if(payload.description() != null) {
				updatedRole.setDescription(payload.description());
			}
			roleRepository.save(updatedRole);
			return ResponseEntity.ok(convertToDTO(updatedRole));
		} else {
			return ResponseEntity.notFound().build(); 			
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteByID(@PathVariable UUID id) {
		if(roleService.hasEmployee(id)) {
			return ResponseEntity.badRequest().body("Role not deleted, it has employees!");
		} else if(roleRepository.existsById(id)) {
			roleRepository.deleteById(id);
			return ResponseEntity.ok("Role deleted successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

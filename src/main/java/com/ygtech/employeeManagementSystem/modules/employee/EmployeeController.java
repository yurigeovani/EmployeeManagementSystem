package com.ygtech.employeeManagementSystem.modules.employee;

import java.util.List;
import java.util.Optional;
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

import com.ygtech.employeeManagementSystem.modules.department.DepartmentEntity;
import com.ygtech.employeeManagementSystem.modules.department.DepartmentRepository;
import com.ygtech.employeeManagementSystem.modules.role.RoleEntity;
import com.ygtech.employeeManagementSystem.modules.role.RoleRepository;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	private EmployeeDTO convertToDTO(EmployeeEntity employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO(
				employee.getId(),
				employee.getName(),
				employee.getAge(),
				employee.getEmail(),
				employee.getDepartment().getId(),
				employee.getRole().getId(),
				employee.getCreatedAt());
		return employeeDTO;
	}
	
	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> getAll() {
		List<EmployeeDTO> employees = employeeRepository.findAll()
				.stream()
				.map(e -> convertToDTO(e))
				.sorted((e1, e2) -> e1.id().compareTo(e2.id()))
				.toList();
		
		return ResponseEntity.ok(employees);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getById(@PathVariable Integer id) {
		if(employeeRepository.existsById(id)) {
			EmployeeEntity employee = this.employeeRepository.findById(id).orElse(null);
			return ResponseEntity.ok(convertToDTO(employee));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping("/department/{id}")
	public ResponseEntity<List<EmployeeDTO>> getByDepartmentId(@PathVariable UUID id) {
		if(!employeeRepository.findByDepartmentId(id).isEmpty()) {
			List<EmployeeDTO> employeeDTO = this.employeeRepository.findByDepartmentId(id)
					.stream()
					.map(e -> convertToDTO(e))
					.toList();
			return ResponseEntity.ok(employeeDTO);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping("/role/{id}")
	public ResponseEntity<List<EmployeeDTO>> getByRoleId(@PathVariable UUID id) {
		if(!employeeRepository.findByRoleId(id).isEmpty()) {
			List<EmployeeDTO> employeeDTO = this.employeeRepository.findByRoleId(id)
					.stream()
					.map(e -> convertToDTO(e))
					.toList();
			return ResponseEntity.ok(employeeDTO);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping
	public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeePayload payload) {
		Optional<DepartmentEntity> departmentOptional = departmentRepository.findById(payload.departmentId());
		Optional<RoleEntity> roleOptional = roleRepository.findById(payload.roleId());
		if(departmentOptional.isPresent() && roleOptional.isPresent()) {
			EmployeeEntity newEmployee = new EmployeeEntity();
			newEmployee.setName(payload.name());
			newEmployee.setAge(payload.age());
			newEmployee.setEmail(payload.email());
			newEmployee.setDepartment(departmentOptional.get());
			newEmployee.setRole(roleOptional.get());

			employeeRepository.save(newEmployee);
			
			return new ResponseEntity<>(convertToDTO(newEmployee), HttpStatus.CREATED);			
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody EmployeePayload payload){
		Optional<EmployeeEntity> employee = this.employeeRepository.findById(id);
		if(employee.isPresent()) {
			EmployeeEntity employeeUpdated = employee.get();
			
			if(payload.age() != null) {
				employeeUpdated.setAge(payload.age());
			}
			if(payload.name() != null) {
				employeeUpdated.setName(payload.name());
			}
			if(payload.email() != null) {
				employeeUpdated.setEmail(payload.email());
			}
			if(payload.departmentId() != null) {
				employeeUpdated.setDepartment(departmentRepository.findById(payload.departmentId()).orElse(null));
			}
			if(payload.roleId() != null) {
				employeeUpdated.setRole(roleRepository.findById(payload.roleId()).orElse(null));
			}
			
			employeeRepository.save(employeeUpdated);
			
			return ResponseEntity.ok(convertToDTO(employeeUpdated));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id) {
		if(employeeService.isManager(id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee not deleted, is a manager!");
		} 
		else if(employeeRepository.existsById(id)) {
			employeeRepository.deleteById(id);
			return ResponseEntity.ok("Employee deleted successfully!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!");
		}	}
}

package com.ygtech.employeeManagementSystem.modules.department;

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
@RequestMapping("/department")
public class DepartmentController {
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	DepartmentService departmentService;
	
	private DepartmentDTO convertToDTO(DepartmentEntity department) {
		
		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setId(department.getId());
		departmentDTO.setName(department.getName());
		departmentDTO.setDescription(department.getDescription());
		departmentDTO.setManagerId(department.getManagerId());
		departmentDTO.setLocation(department.getLocation());
		departmentDTO.setCreatedAt(department.getCreatedAt());

		return departmentDTO;
	}

	@GetMapping
	public ResponseEntity<List<DepartmentDTO>> getAll() {
		List<DepartmentDTO> departments = departmentRepository.findAll()
				.stream()
				.map(d -> convertToDTO(d))
				.sorted((d1, d2) -> d1.getName().compareTo(d2.getName()))
				.toList();
		return ResponseEntity.ok(departments);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DepartmentDTO> getById(@PathVariable UUID id){
		return ResponseEntity.ok(convertToDTO(departmentRepository.findById(id).orElse(null)));
	}
	
	@GetMapping("managerId/{id}")
	public ResponseEntity<List<DepartmentDTO>> getByManagerId(@PathVariable Integer id){
		List<DepartmentDTO> departmentManagedById = 
				departmentRepository.findByManagerId(id)
					.stream()
					.map(d -> convertToDTO(d))
					.toList();
		if(departmentManagedById.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(departmentManagedById);
	}
	
	@GetMapping("/{id}/employees")
	public ResponseEntity<List<Integer>> getAllEmployeesByDepartmentId(@PathVariable UUID id){
			return ResponseEntity.ok(departmentRepository.findAllByDepartmentId(id));
	}
	
	@PostMapping
	public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentPayload payload){
		DepartmentEntity newDepartment = new DepartmentEntity(payload);
		departmentRepository.save(newDepartment);
		return new ResponseEntity<>(convertToDTO(newDepartment), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable UUID id, @RequestBody DepartmentPayload payload) {
		if(departmentRepository.findById(id).isPresent()) {
			DepartmentEntity departmentUpdated = departmentRepository.findById(id).get();

			if(payload.name() != null) {
				departmentUpdated.setName(payload.name());
			}
			if(payload.description() != null) {
				departmentUpdated.setDescription(payload.description());
			}
			if(payload.managerId() != null) {
				departmentUpdated.setManagerId(payload.managerId());
			}
			if(payload.location() != null) {
				departmentUpdated.setLocation(payload.location());
			}
			
			departmentRepository.save(departmentUpdated);
			
			return ResponseEntity.ok(convertToDTO(departmentUpdated));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDepartmentById(@PathVariable UUID id){
		if(departmentService.hasEmployees(id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Department not deleted, it has employees!");
		} else if(departmentRepository.existsById(id)) {
			departmentRepository.deleteById(id);
			return ResponseEntity.ok("Department deleted successfully!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found!");
		}
	}
}

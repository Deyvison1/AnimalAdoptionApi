package com.animaladoption.api.controller;

import java.net.URI;
import java.util.UUID;

import com.animaladoption.api.dto.dog.DogFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.animaladoption.api.dto.constants.DogConstants;
import com.animaladoption.api.dto.dog.DogCreateDTO;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.dto.dog.DogUpdateDTO;
import com.animaladoption.api.service.IDogService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(DogConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = DogConstants.TITLE, description = DogConstants.DESCRIPTION)
public class DogController {

	private final IDogService service;

	@GetMapping("/{id}")
	@PreAuthorize(DogConstants.ADMIN_READ_AUTHORITY)
	@ApiResponse(responseCode = "200", description = DogConstants.FIND_BY_ID)
	public ResponseEntity<DogDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findByIdDTO(id));
	}
	
	@GetMapping("/is-publish/{id}")
	@PreAuthorize(DogConstants.ADMIN_PUBLISH_AUTHORITY)
	@ApiResponse(responseCode = "200", description = DogConstants.IS_PUBLISH)
	public ResponseEntity<Void> isPublish(@PathVariable UUID id) {
		service.isPublish(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/not-publish/{id}")
	@PreAuthorize(DogConstants.ADMIN_PUBLISH_AUTHORITY)
	@ApiResponse(responseCode = "200", description = DogConstants.NOT_PUBLISH)
	public ResponseEntity<Void> notPublish(@PathVariable UUID id, @RequestParam(value = "motivo", required = true) String motivo) {
		service.notPublish(id, motivo);
		return ResponseEntity.noContent().build();
	}


	@GetMapping
	@PreAuthorize(DogConstants.ADMIN_READ_AUTHORITY)
	@ApiResponse(responseCode = "200", description = DogConstants.FIND_ALL)
	public ResponseEntity<Page<DogDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size, @ModelAttribute DogFilterDTO filter) {
		Pageable pageable = PageRequest.of(page, size);
		Page<DogDTO> resp = service.findAll(pageable, filter);
		return ResponseEntity.ok(resp);
	}

	@PostMapping
	@PreAuthorize(DogConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "201", description = DogConstants.CREATED)
	public ResponseEntity<DogDTO> add(@RequestBody final DogCreateDTO dto) {
		DogDTO response = service.add(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@PreAuthorize(DogConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = DogConstants.UPDATED)
	public ResponseEntity<DogDTO> update(@PathVariable UUID id, @Valid @RequestBody final DogUpdateDTO dto) {
		DogDTO response = service.update(id, dto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize(DogConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "204", description = DogConstants.DELETED)
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

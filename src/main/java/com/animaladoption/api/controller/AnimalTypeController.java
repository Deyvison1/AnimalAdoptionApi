package com.animaladoption.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.animaladoption.api.dto.animal.AnimalTypeDTO;
import com.animaladoption.api.dto.constants.AnimalTypeConstants;
import com.animaladoption.api.service.IAnimalTypeService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AnimalTypeConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = AnimalTypeConstants.TITLE, description = AnimalTypeConstants.DESCRIPTION)
public class AnimalTypeController {

	private final IAnimalTypeService service;

	@GetMapping("/{id}")
	@PreAuthorize(AnimalTypeConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = AnimalTypeConstants.FIND_BY_ID)
	public ResponseEntity<AnimalTypeDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findByIdToDto(id));
	}

	@GetMapping
	@PreAuthorize(AnimalTypeConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = AnimalTypeConstants.FIND_ALL)
	public ResponseEntity<Page<AnimalTypeDTO>> findAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AnimalTypeDTO> resp = service.findAll(pageable);
		return ResponseEntity.ok(resp);
	}

	@PostMapping
	@PreAuthorize(AnimalTypeConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "201", description = AnimalTypeConstants.CREATED)
	public ResponseEntity<AnimalTypeDTO> add(@RequestBody final AnimalTypeDTO dto) {
		AnimalTypeDTO response = service.add(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@PreAuthorize(AnimalTypeConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = AnimalTypeConstants.UPDATED)
	public ResponseEntity<AnimalTypeDTO> update(@PathVariable UUID id, @Valid @RequestBody final AnimalTypeDTO dto) {
		AnimalTypeDTO response = service.update(id, dto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize(AnimalTypeConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "204", description = AnimalTypeConstants.DELETED)
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

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

import com.animaladoption.api.dto.breed.BreedDTO;
import com.animaladoption.api.dto.constants.BreedConstants;
import com.animaladoption.api.service.IBreedService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(BreedConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = BreedConstants.TITLE, description = BreedConstants.DESCRIPTION)
public class BreedController {

	private final IBreedService service;

	@GetMapping("/{id}")
	@PreAuthorize(BreedConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = BreedConstants.FIND_BY_ID)
	public ResponseEntity<BreedDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findByIdToDto(id));
	}

	@GetMapping
	@PreAuthorize(BreedConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = BreedConstants.FIND_ALL)
	public ResponseEntity<Page<BreedDTO>> findAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<BreedDTO> resp = service.findAll(pageable);
		return ResponseEntity.ok(resp);
	}

	@PostMapping
	@PreAuthorize(BreedConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "201", description = BreedConstants.CREATED)
	public ResponseEntity<BreedDTO> add(@Valid @RequestBody final BreedDTO dto) {
		BreedDTO response = service.add(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@PreAuthorize(BreedConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = BreedConstants.UPDATED)
	public ResponseEntity<BreedDTO> update(@PathVariable UUID id, @Valid @RequestBody final BreedDTO dto) {
		BreedDTO response = service.update(id, dto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize(BreedConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "204", description = BreedConstants.DELETED)
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	}
}

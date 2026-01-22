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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.animaladoption.api.dto.cat.CatCreateDTO;
import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatFilterDTO;
import com.animaladoption.api.dto.cat.CatUpdateDTO;
import com.animaladoption.api.dto.constants.CatConstants;
import com.animaladoption.api.service.ICatService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CatConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = CatConstants.TITLE, description = CatConstants.DESCRIPTION)
public class CatController {

	private final ICatService service;

	@GetMapping("/{id}")
	@PreAuthorize(CatConstants.ADMIN_READ_AUTHORITY)
	@ApiResponse(responseCode = "200", description = CatConstants.FIND_BY_ID)
	public ResponseEntity<CatDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findByIdDTO(id));
	}

	@GetMapping("/is-publish/{id}")
	@PreAuthorize(CatConstants.ADMIN_PUBLISH_AUTHORITY)
	@ApiResponse(responseCode = "200", description = CatConstants.IS_PUBLISH)
	public ResponseEntity<Void> isPublish(@PathVariable UUID id) {
		service.isPublish(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/not-publish/{id}")
	@PreAuthorize(CatConstants.ADMIN_PUBLISH_AUTHORITY)
	@ApiResponse(responseCode = "200", description = CatConstants.NOT_PUBLISH)
	public ResponseEntity<Void> notPublish(@PathVariable UUID id,
			@RequestParam(value = "motivo", required = true) String motivo) {
		service.notPublish(id, motivo);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	@PreAuthorize(CatConstants.ADMIN_READ_AUTHORITY)
	@ApiResponse(responseCode = "200", description = CatConstants.FIND_ALL)
	public ResponseEntity<Page<CatDTO>> findAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @ModelAttribute CatFilterDTO filter) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CatDTO> resp = service.findAll(pageable, filter);
		return ResponseEntity.ok(resp);
	}

	@PostMapping
	@PreAuthorize(CatConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "201", description = CatConstants.CREATED)
	public ResponseEntity<CatDTO> add(@RequestBody final CatCreateDTO dto) {
		CatDTO response = service.add(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	@PreAuthorize(CatConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = CatConstants.UPDATED)
	public ResponseEntity<CatDTO> update(@PathVariable UUID id, @Valid @RequestBody final CatUpdateDTO dto) {
		CatDTO response = service.update(id, dto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize(CatConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "204", description = CatConstants.DELETED)
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

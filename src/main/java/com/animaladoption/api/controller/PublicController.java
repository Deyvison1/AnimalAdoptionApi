package com.animaladoption.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.animaladoption.api.dto.dog.DogFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.animaladoption.api.dto.constants.AnimalTypeConstants;
import com.animaladoption.api.dto.constants.DogConstants;
import com.animaladoption.api.dto.constants.PublicConstants;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.service.IDogService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PublicConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = AnimalTypeConstants.TITLE, description = PublicConstants.DESCRIPTION)
public class PublicController {
	private final IDogService dogService;
	
	@GetMapping("/dogs")
	@ApiResponse(responseCode = "200", description = DogConstants.FIND_ALL)
	public ResponseEntity<Page<DogDTO>> findAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @ModelAttribute DogFilterDTO filter) {
		Pageable pageable = PageRequest.of(page, size);

	    Page<DogDTO> result = dogService.findAll(pageable, filter);

	    List<DogDTO> filtered = result.getContent()
	            .stream()
	            .filter(DogDTO::getAvailable)
	            .collect(Collectors.toList());

	    Page<DogDTO> resp = new PageImpl<DogDTO>(filtered, pageable, filtered.size());

	    return ResponseEntity.ok(resp);
	}
}

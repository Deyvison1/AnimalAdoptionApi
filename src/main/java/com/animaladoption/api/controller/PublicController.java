package com.animaladoption.api.controller;


import com.animaladoption.api.dto.dog.DogFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.animaladoption.api.dto.cat.CatDTO;
import com.animaladoption.api.dto.cat.CatFilterDTO;
import com.animaladoption.api.dto.constants.AnimalTypeConstants;
import com.animaladoption.api.dto.constants.DogConstants;
import com.animaladoption.api.dto.constants.PublicConstants;
import com.animaladoption.api.dto.dog.DogDTO;
import com.animaladoption.api.service.ICatService;
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
	private final ICatService catService;
	
	@GetMapping("/dogs")
	@ApiResponse(responseCode = "200", description = DogConstants.FIND_ALL)
	public ResponseEntity<Page<DogDTO>> findAllDogs(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @ModelAttribute DogFilterDTO filter) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DogDTO> resp = dogService.findAllByAvaliableAndPublishIsTrue(pageable, filter);
        return ResponseEntity.ok(resp);
	}
	
	@GetMapping("/cats")
	@ApiResponse(responseCode = "200", description = DogConstants.FIND_ALL)
	public ResponseEntity<Page<CatDTO>> findAllCats(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @ModelAttribute CatFilterDTO filter) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CatDTO> resp = catService.findAllByAvaliableAndPublishIsTrue(pageable, filter);
        return ResponseEntity.ok(resp);
	}
}

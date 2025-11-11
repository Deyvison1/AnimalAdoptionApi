package com.animaladoption.api.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animaladoption.api.dto.MenuDTO;
import com.animaladoption.api.dto.constants.MenuConstants;
import com.animaladoption.api.enums.MenuEnum;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(MenuConstants.BASE_PATH)
@Tag(name = MenuConstants.TITLE, description = MenuConstants.DESCRIPTION)
public class MenuController {
	
	@GetMapping
	@PreAuthorize(MenuConstants.ADMIN_READ_AUTHORITY)
	@ApiResponse(responseCode = "201", description = MenuConstants.FIND_ALL)
	public ResponseEntity<Set<MenuDTO>> finAll() {
		return ResponseEntity.ok(MenuEnum.findAll());
	}
}

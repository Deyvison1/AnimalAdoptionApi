package com.animaladoption.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.dto.animal.ImageDTO;
import com.animaladoption.api.dto.constants.AnimalImageConstants;
import com.animaladoption.api.service.IImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador responsável pelo gerenciamento de imagens de animais. Inclui
 * upload, download, exclusão e ativação.
 */
@RestController
@RequestMapping(AnimalImageConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = AnimalImageConstants.TITLE, description = AnimalImageConstants.DESCRIPTION)
public class ImageController {

	private final IImageService service;

	@PostMapping("/{animalId}/upload")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@Operation(summary = AnimalImageConstants.OP_UPLOAD_SUMMARY, description = AnimalImageConstants.UPLOAD, responses = {
			@ApiResponse(responseCode = "201", description = AnimalImageConstants.CREATED, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
			@ApiResponse(responseCode = "400", description = "Arquivo inválido ou parâmetros incorretos"),
			@ApiResponse(responseCode = "404", description = "Animal não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno ao processar o upload") })
	public ResponseEntity<ImageDTO> uploadImage(
			@Parameter(description = AnimalImageConstants.PARAM_ANIMAL_ID, required = true) @PathVariable("animalId") UUID animalId,

			@Parameter(description = AnimalImageConstants.PARAM_FILE, required = true) @RequestParam("file") MultipartFile file,

			@Parameter(description = AnimalImageConstants.PARAM_ACTIVE) @RequestParam(defaultValue = "false") boolean active)
			throws IOException {

		ImageDTO dto = service.uploadImage(animalId, file, active);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping("/download/{id}")
	@PreAuthorize(AnimalImageConstants.ADMIN_READ_AUTHORITY)
	@Operation(summary = AnimalImageConstants.OP_DOWNLOAD_SUMMARY, description = AnimalImageConstants.DOWNLOAD, responses = {
			@ApiResponse(responseCode = "200", description = "URL da imagem retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
			@ApiResponse(responseCode = "404", description = "Imagem não encontrada") })
	public ResponseEntity<ImageDTO> findById(
			@Parameter(description = AnimalImageConstants.PARAM_IMAGE_ID, required = true) @PathVariable("id") UUID id) {
		ImageDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{id}/{animalId}")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@Operation(summary = AnimalImageConstants.OP_DELETE_SUMMARY, description = AnimalImageConstants.DELETED, responses = {
			@ApiResponse(responseCode = "204", description = AnimalImageConstants.DELETED),
			@ApiResponse(responseCode = "404", description = "Imagem não encontrada") })
	public ResponseEntity<Void> remove(
			@Parameter(description = AnimalImageConstants.PARAM_IMAGE_ID, required = true) @PathVariable UUID id, @PathVariable UUID animalId) {
		service.delete(id, animalId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/activate")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@Operation(summary = AnimalImageConstants.OP_ACTIVATE_SUMMARY, description = AnimalImageConstants.ACTIVE_IMAGE, responses = {
			@ApiResponse(responseCode = "200", description = AnimalImageConstants.ACTIVE_IMAGE),
			@ApiResponse(responseCode = "404", description = "Imagem não encontrada") })
	public ResponseEntity<Void> activateImage(
            @Parameter(description = AnimalImageConstants.PARAM_IMAGE_ID, required = true) @PathVariable UUID id, @RequestParam List<UUID> idIsDisabled) {
		service.activeImage(id, idIsDisabled);
		return ResponseEntity.ok().build();
	}
}

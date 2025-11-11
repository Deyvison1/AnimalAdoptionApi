package com.animaladoption.api.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.dto.animal.AnimalImageDTO;
import com.animaladoption.api.dto.constants.AnimalImageConstants;
import com.animaladoption.api.service.IAnimalImageService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AnimalImageConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = AnimalImageConstants.TITLE, description = AnimalImageConstants.DESCRIPTION)
public class AnimalImageController {

	private final IAnimalImageService service;

	@PostMapping("/{animalId}/upload")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "201", description = AnimalImageConstants.UPLOAD)
	public ResponseEntity<AnimalImageDTO> uploadImage(@PathVariable UUID animalId,
			@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "false") boolean active)
			throws IOException {

		AnimalImageDTO dto = service.uploadImageToDatabase(animalId, file, active);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@GetMapping("/download/{id}")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = AnimalImageConstants.DOWNLOAD)
	public ResponseEntity<byte[]> downloadImage(@PathVariable UUID id) {
		AnimalImageDTO dto = service.getImageById(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(dto.getContentType())).body(dto.getData());
	}

    /**
     * Remove uma imagem do animal pelo id.
     *
     * @param id UUID da imagem do animal que deve ser removido
     * @return 204 OK se sucesso
     */
	@DeleteMapping("/{id}")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "204", description = AnimalImageConstants.DELETED)
	public ResponseEntity<Void> remove(@PathVariable UUID id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/activate")
	@PreAuthorize(AnimalImageConstants.ADMIN_AUTHORITY)
	@ApiResponse(responseCode = "200", description = AnimalImageConstants.ACTIVE_IMAGE)
	public ResponseEntity<Void> activateImage(@PathVariable UUID id) {
		service.activeImage(id);
		return ResponseEntity.ok().build();
	}
}

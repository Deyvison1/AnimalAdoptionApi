package com.animaladoption.api.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.animaladoption.api.config.FeignAuthInterceptor;
import com.animaladoption.api.config.FeignMultipartConfig;
import com.animaladoption.api.dto.animal.ImageDTO;

@FeignClient(name = "image-api", url = "${image-api.url}", configuration = { FeignMultipartConfig.class,
		FeignAuthInterceptor.class })
public interface IImageClient {

	/**
	 * Upload imagem
	 */
	@PostMapping(value = "/api/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ImageDTO uploadImage(@RequestPart("file") MultipartFile file, @RequestParam("active") Boolean active);

	/**
	 * Busca metadata da imagem
	 */
	@GetMapping("/api/image/{id}")
	ImageDTO getImage(@PathVariable("id") UUID id);

	/**
	 * 👇 NOVO: pega URL do S3
	 */
	@GetMapping("/api/image/{id}/url")
	String getImageUrl(@PathVariable("id") UUID id);

	/**
	 * Ativar imagem
	 */
	@PutMapping("/api/image/{id}/activate")
	void activeImage(@PathVariable("id") UUID id);

	/**
	 * Desativar em lote (corrigido)
	 */
	@PutMapping("/api/image/disabled")
	void disabledImages(@RequestBody List<UUID> ids);

	/**
	 * Delete imagem
	 */
	@DeleteMapping("/api/image/{id}")
	void delete(@PathVariable("id") UUID id);
}
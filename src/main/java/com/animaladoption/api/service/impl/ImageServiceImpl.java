package com.animaladoption.api.service.impl;

import com.animaladoption.api.client.IImageClient;
import com.animaladoption.api.dto.animal.ImageDTO;
import com.animaladoption.api.exception.ImageApiException;
import com.animaladoption.api.model.Animal;
import com.animaladoption.api.repository.IAnimalRepository;
import com.animaladoption.api.service.IImageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import feign.FeignException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {
	private final IAnimalRepository repository;
	private final IImageClient imageClient;
    @Value("${image-api.url}")
    private String imageApiBaseUrl;
    // ✅ defina conforme seu ambiente

    /**
	 * Faz upload da imagem para o serviço de imagens e associa ao animal.
	 *
	 * @param animalId ID do animal
	 * @param file     arquivo da imagem
	 * @param active   indica se a imagem deve estar ativa
	 * @return DTO da imagem salva
	 */
	@Override
	@Transactional
    public ImageDTO uploadImage(UUID animalId, MultipartFile file, boolean active) {
        try {
            Animal animal = repository.findById(animalId)
                    .orElseThrow(() -> new RuntimeException("Animal not found"));

            if (animal.getImages().isEmpty()) {
                active = true;
            }

            ImageDTO uploaded = imageClient.uploadImage(file, active);

            // ✅ Ajusta a URL para conter o domínio/porta da API
            if (uploaded != null && uploaded.getUrl() != null && !uploaded.getUrl().startsWith("http")) {
                uploaded.setUrl(imageApiBaseUrl + uploaded.getUrl());
            }

            UUID uploadedId = uploaded.getId();

            if (animal.getImages() == null) {
                animal.setImages(new ArrayList<>());
            }

            if (!animal.getImages().contains(uploadedId)) {
                animal.getImages().add(uploadedId);
                repository.save(animal);
            }

            return uploaded;

        } catch (Exception e) {
            if (e instanceof FeignException feignEx) {
                int status = feignEx.status(); // pega o status HTTP da Image API
                String message = extractMessage(e); // método que extrai a mensagem do JSON
                throw new ImageApiException(status, message);
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }



    /**
	 * Busca uma imagem pelo seu ID através do serviço de imagens.
	 *
	 * @param id ID da imagem
	 * @return DTO da imagem encontrada
	 */
	@Override
	public ImageDTO findById(UUID id) {
		try {
			return imageClient.getImage(id);
		} catch (Exception e) {
            if (e instanceof FeignException feignEx) {
                int status = feignEx.status(); // pega o status HTTP da Image API
                String message = extractMessage(e); // método que extrai a mensagem do JSON
                throw new ImageApiException(status, message);
            }
            throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Ativa uma imagem no serviço de imagens.
	 *
	 * @param id ID da imagem
	 */
	@Override
	public void activeImage(UUID id, List<UUID> idsIsActive) {
		try {
            if(idsIsActive.size() > 1) {
                idsIsActive.removeIf(x -> x.equals(id));
            }
            imageClient.disabledImage(idsIsActive);
			imageClient.activeImage(id);
		} catch (Exception e) {
            if (e instanceof FeignException feignEx) {
                int status = feignEx.status(); // pega o status HTTP da Image API
                String message = extractMessage(e); // método que extrai a mensagem do JSON
                throw new ImageApiException(status, message);
            }
		}
	}

	/**
	 * Remove uma imagem do serviço de imagens.
	 *
	 * @param id ID da imagem
	 */
	@Override
	@Transactional
	public void delete(UUID id, UUID animalId) {
		try {
            Animal dog = repository.findById(animalId).orElseThrow(() -> new RuntimeException("Dog not found"));
            dog.getImages().removeIf(x -> x.equals(id));
            repository.save(dog);
			imageClient.delete(id);
		} catch (Exception e) {
            if (e instanceof FeignException feignEx) {
                int status = feignEx.status(); // pega o status HTTP da Image API
                String message = extractMessage(e); // método que extrai a mensagem do JSON
                throw new ImageApiException(status, message);
            }
		}
	}

    private String extractMessage(Exception e) {
        if (e instanceof FeignException feignEx) {
            try {
                // Pega o corpo da resposta se existir
                String body = feignEx.responseBody()
                        .map(bytes -> new String(bytes.array()))
                        .orElse(feignEx.getMessage());

                // Tenta ler JSON e extrair "message"
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);
                if (node.has("message")) {
                    return node.get("message").asText();
                }
                return body; // fallback se não tiver "message"
            } catch (Exception ex) {
                return feignEx.getMessage();
            }
        }
        return e.getMessage();
    }

}

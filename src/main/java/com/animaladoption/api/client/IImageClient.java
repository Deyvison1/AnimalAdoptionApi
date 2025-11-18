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

@FeignClient(
        name = "image-api",
        url = "${image-api.url}",
        configuration = {FeignMultipartConfig.class, FeignAuthInterceptor.class}
)
public interface IImageClient {

    @PostMapping(
            value = "/api/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ImageDTO uploadImage(
            @RequestPart("file") MultipartFile file, @RequestParam("active") Boolean active
    );

    @GetMapping("/api/image/{id}")
    ImageDTO getImage(@PathVariable("id") UUID id);

    @PutMapping("/api/image/{id}/activate")
    void activeImage(@PathVariable("id") UUID id);

    @PutMapping("/api/image/{ids}/disabled")
    void disabledImage(@PathVariable("ids") List<UUID> ids);

    @GetMapping("/api/image/{id}/download")
    void download(@PathVariable("id") UUID id);

    @DeleteMapping("/api/image/{id}")
    void delete(@PathVariable("id") UUID id);
}

package com.fooddelivery.restaurants.controller;

import com.fooddelivery.restaurants.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "ImageStorage API",description = "API for managing dish's images")
public class ImageController {
    private final ImageStorageService imageStorageService;

    @Operation(summary = "Get the image",
            description = "Returns the image of dish",
            parameters = {@Parameter(name = "subdirectory", description = "The name of subfolder where images will be loaded",
                    example = "images", required = true),
                    @Parameter(name = "filename", description = "The name of file that we want to get",
                            example = "photo.jpg", required = true)})
    @ApiResponse(responseCode = "200", description = "File found")
    @ApiResponse(responseCode = "404", description = "File not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{subdirectory}/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String subdirectory,
                                           @PathVariable String filename) {
        try {
            String imagePath = subdirectory + "/" + filename;
            byte[] imageBytes = imageStorageService.getImage(imagePath);

            MediaType mediaType = determineMediaType(filename);
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Hidden
    private MediaType determineMediaType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}

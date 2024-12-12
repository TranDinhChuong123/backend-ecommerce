package com.shop.ecommerce.controllers;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/images")
    public ApiResponse<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
       return new ApiResponse<>(
               200,
               "Images uploaded successfully",
               imageUploadService.uploadImages(files)
       );
    }
}

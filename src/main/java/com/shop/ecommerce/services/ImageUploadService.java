package com.shop.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    List<String> uploadImages(List<MultipartFile> images);
}

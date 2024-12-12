package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Inventory;
import com.shop.ecommerce.models.Product;
import com.shop.ecommerce.models.ProductVariation;
import com.shop.ecommerce.models.enums.ProductStatus;
import com.shop.ecommerce.repositories.InventoryRepository;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.requests.FilterRequest;
import com.shop.ecommerce.responses.InventoryResponse;
import com.shop.ecommerce.responses.StockVariationResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements  InventoryService{

    private final MongoTemplate mongoTemplate;
    private final InventoryRepository repository;
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public Inventory addInventory(Inventory inventory) {
        Optional<Inventory> findInventory = repository.findByVariationId(inventory.getVariationId());

        if (findInventory.isPresent()) {
            throw new RuntimeException("Inventory already exists");
        }

        Inventory savedInventory = repository.save(inventory);

            Product product = productRepository.findById(inventory.getProductId().toString())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStatus(ProductStatus.PUBLISHED);
            productRepository.save(product);

        return savedInventory;
    }


    @Override
    public List<InventoryResponse> findAllListInventory(FilterRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getLimit()
        );
        Query query = new Query().with(pageable);

        if (request.getSelectFields() != null && !request.getSelectFields().isEmpty()) {
            for (String field : request.getSelectFields()) {
                query.fields().include(field);
            }
        }
        if (request.getUnSelectFields() != null && !request.getUnSelectFields().isEmpty()) {
            for (String field : request.getUnSelectFields()) {
                query.fields().exclude(field);
            }
        }

        List<Inventory> list = mongoTemplate.find(query, Inventory.class);

        List<InventoryResponse> result = list.stream().map(inventory -> {
            Product product = productRepository.findById(inventory.getProductId().toString())
                    .orElseThrow(()-> new RuntimeException("Product not found"));
            ProductVariation variation = product.getProductVariations()
                    .stream()
                    .filter(v -> v.getId().equals(inventory.getVariationId()))
                    .findFirst()
                    .orElse(null);

            return InventoryResponse.builder()
                    .id(inventory.getId())
                    .urlImage(product.getImages().get(0).getUrlImage())
                    .productId(inventory.getProductId())
                    .productName(product.getName())
                    .variationId(inventory.getVariationId())
                    .color(variation.getColor())
                    .size(variation.getSize())
                    .capacity(variation.getCapacity())
                    .date(inventory.getCreatedAt())
                    .build();
        }).toList();
        return result;
    }

    @Override
    public List<InventoryResponse> findInventoriesByProductID(String productID) {
        List<Inventory> inventories = repository
                .findByProductId(new ObjectId(productID) ).orElseThrow(()-> new RuntimeException("Couldn't find inventories'"));
        logger.info("Found inventories {}", inventories.size());


        List<InventoryResponse> result = inventories.stream().map(inventory -> {

            Product product = productRepository.findById(inventory.getProductId().toString())
                    .orElseThrow(()-> new RuntimeException("Product not found"));

            ProductVariation variation = product.getProductVariations()
                    .stream()
                    .filter(v -> v.getId().equals(inventory.getVariationId()))
                    .findFirst()
                    .orElse(null);

            return InventoryResponse.builder()
                    .id(inventory.getId())
                    .productId(inventory.getProductId())
                    .urlImage(product.getImages().get(0).getUrlImage())
                    .productName(product.getName())
                    .variationId(inventory.getVariationId())
                    .color(variation.getColor())
                    .size(variation.getSize())
                    .capacity(variation.getCapacity())
                    .quantityInStock(inventory.getQuantityInStock())
                    .totalQuantityIn(inventory.getTotalQuantityIn())
                    .date(inventory.getCreatedAt())
                    .build();
        }).toList();
        return result;
    }

    @Override
    public Inventory findByVariationId(String variationId) {
        return repository.findByVariationId(variationId)
                .orElseThrow(()-> new RuntimeException("Could not find"));
    }

    @Override
    public List<StockVariationResponse> getStockVariationByProductId(String productID) {
        List<Inventory> inventories = repository
                .findByProductId(new ObjectId(productID) ).orElseThrow(()-> new RuntimeException("Couldn't find inventories'"));
        return inventories.stream().map(
                inventory-> StockVariationResponse.builder()
                        .variationId(inventory.getVariationId())
                        .quantity(inventory.getQuantityInStock())
                        .build()
        ).toList();
    }




}

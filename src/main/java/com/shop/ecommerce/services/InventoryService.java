package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Inventory;
import com.shop.ecommerce.requests.FilterRequest;
import com.shop.ecommerce.responses.InventoryResponse;
import com.shop.ecommerce.responses.StockVariationResponse;

import java.util.List;

public interface InventoryService {

    Inventory addInventory(Inventory inventory);
    List<InventoryResponse> findAllListInventory(FilterRequest filterRequest);
    List<InventoryResponse> findInventoriesByProductID(String productID);

    Inventory findByVariationId(String variationId);

    List<StockVariationResponse>  getStockVariationByProductId(String productID);

}

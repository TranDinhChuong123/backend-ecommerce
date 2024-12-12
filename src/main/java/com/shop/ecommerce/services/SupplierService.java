package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Supplier;

import java.util.List;

public interface SupplierService {
    boolean createSupplier(Supplier supplier);

    List<Supplier> getSuppliers();
}

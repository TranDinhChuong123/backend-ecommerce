package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Supplier;
import com.shop.ecommerce.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService{
    private final SupplierRepository supplierRepository;

    @Override
    public boolean createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier).getId() !=null;
    }

    @Override
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }
}

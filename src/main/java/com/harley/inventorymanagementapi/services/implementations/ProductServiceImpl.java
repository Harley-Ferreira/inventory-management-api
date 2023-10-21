package com.harley.inventorymanagementapi.services.implementations;

import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.exceptions.ExistsObjectInDBException;
import com.harley.inventorymanagementapi.exceptions.ObjectNotFoundBDException;
import com.harley.inventorymanagementapi.repositories.ProductRepository;
import com.harley.inventorymanagementapi.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final static String OBJECT_NOT_FOUND_MESSAGE = "Não foi possível encontrar o produto.";
    private final static String OBJECT_EXIST_MESSAGE = "Já existe um produto com este código.";
    private final static String INVALID_ID = "O id do produto é inválido";
    @Override
    public Product register(Product product) {
        if (product == null) throw new IllegalArgumentException("O objeto produto é inválido.");

        if (productRepository.existsByCode(product.getCode())) {
            throw new ExistsObjectInDBException("Já existe um produto com este código.");
        }

        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        if (product == null) throw new IllegalArgumentException("O objeto produto é inválido.");
        getById(product.getId());
        return productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        if (id == null) throw new IllegalArgumentException("O id do produto é inválido.");

        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundBDException("Não foi possível encontrar o produto."));
    }

    @Override
    public Page<Product> getPageProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        var product = getById(id);
        productRepository.delete(product);
    }
}

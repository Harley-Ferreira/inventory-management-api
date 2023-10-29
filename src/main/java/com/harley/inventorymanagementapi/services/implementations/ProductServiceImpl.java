package com.harley.inventorymanagementapi.services.implementations;

import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.exceptions.ExistsObjectInDBException;
import com.harley.inventorymanagementapi.exceptions.ObjectNotFoundBDException;
import com.harley.inventorymanagementapi.repositories.ProductRepository;
import com.harley.inventorymanagementapi.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
    public Page<Product> getPageProducts(Pageable pageable, String filter) {
        Product product = new Product();
        if (!filter.isBlank()) {
            try {
                product.setCode(Long.valueOf(filter));
            } catch (NumberFormatException e) {
                product.setName(filter);
            }
        }

        Example<Product> example = Example.of(product, matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(CONTAINING));

        return productRepository.findAll(example, pageable);
    }

    @Override
    public void delete(Long id) {
        var product = getById(id);
        productRepository.delete(product);
    }
}

package com.harley.inventorymanagementapi.controllers;

import com.harley.inventorymanagementapi.dtos.product.ProductDTO;
import com.harley.inventorymanagementapi.dtos.product.ProductPageDTO;
import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.services.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> register(@RequestBody @Valid ProductDTO productDTO, UriComponentsBuilder uriComponentsBuilder) {
        var product = new Product(productDTO);
        product = productService.register(product);
        productDTO = new ProductDTO(product);
        var uri = uriComponentsBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable("id") Long id, @RequestBody @Valid ProductDTO productDTO) {
        var product = new Product(productDTO);
        product = productService.update(product);
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ProductDTO(productService.getById(id)));
    }

    @GetMapping
    public Page<ProductPageDTO> getByPage(@PageableDefault(sort = "code") Pageable pageable, @RequestParam("filter") String filter) {
        Page<Product> productPage = productService.getPageProducts(pageable, filter);
        List<ProductPageDTO> list = productPage.getContent()
                .stream()
                .map(ProductPageDTO::new)
                .toList();
        return new PageImpl<>(list, pageable, productPage.getTotalElements());
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.harley.inventorymanagementapi.services;

import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.exceptions.ExistsObjectInDBException;
import com.harley.inventorymanagementapi.exceptions.ObjectNotFoundBDException;
import com.harley.inventorymanagementapi.repositories.ProductRepository;
import com.harley.inventorymanagementapi.services.implementations.ProductServiceImpl;
import com.harley.inventorymanagementapi.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.harley.inventorymanagementapi.factories.ProductFactory.createNewProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProductServiceTest {

    ProductService productService;
    @MockBean
    ProductRepository productRepository;

    private final static String OBJECT_NOT_FOUND_MESSAGE = "Não foi possível encontrar o produto.";
    private final static String OBJECT_EXIST_MESSAGE = "Já existe um produto com este código.";
    private final static String INVALID_ID = "O id do produto é inválido.";

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    // REGISTER
    @Test
    void GivenAProduct_WhenCallRegister_ThenSaveAndReturnProduct() {
        // Scenary
        var request = createNewProduct(null);
        var product = createNewProduct();
        when(productRepository.existsByCode(Mockito.anyLong())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Execution
        var registedProduct = productService.register(request);

        // Verification
        verify(productRepository, times(1)).save(request);
        assertNotNull(registedProduct.getId());
        assertEquals(request.getCode(), registedProduct.getCode());
        assertEquals(request.getName(), registedProduct.getName());
        assertEquals(request.getPrice(), registedProduct.getPrice());
    }

    @Test
    void GivenAProduct_WhenCallRegister_ThenThrowAnException() {
        // Scenary
        var request = createNewProduct(null);
        when(productRepository.existsByCode(Mockito.anyLong())).thenReturn(true);

        // Execution and Verification
        ExistsObjectInDBException exception = assertThrows(ExistsObjectInDBException.class, () -> productService.register(request));
        assertEquals(OBJECT_EXIST_MESSAGE, exception.getMessage());
        verify(productRepository, never()).save(request);
    }

    @Test
    void GivenNullProduct_WhenCallRegister_ThenThrowAnException() {
        // Scenary
        Product request = null;

        // Execution
        var exception = assertThrows(IllegalArgumentException.class, () -> productService.register(request));

        // Verification
        var expectedMessage = "O objeto produto é inválido.";
        assertEquals(exception.getMessage(), expectedMessage);
    }

    // UPDATE
    @Test
    void GivenAnIdAndProduct_WhenCallUpdate_ThenUpdateAndReturnProduct() {
        Product request = createNewProduct();
        Product product = createNewProduct();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Execution
        var updatedProduct = productService.update(request);

        // Verification
        verify(productRepository, times(1)).save(request);
        assertEquals(request.getId(), updatedProduct.getId());
        assertEquals(request.getCode(), updatedProduct.getCode());
        assertEquals(request.getName(), updatedProduct.getName());
        assertEquals(request.getPrice(), updatedProduct.getPrice());
    }

    @Test
    void GivenNonExistingProduct_WhenCallUpdate_ThenThrowAnException() {
        Product request = createNewProduct();
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execution and  Verification
        ObjectNotFoundBDException exception = assertThrows(ObjectNotFoundBDException.class, () -> productService.update(request));
        assertEquals(OBJECT_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void GivenNullProduct_WhenCallUpdate_ThenThrowAnException() {
        // Scenary
        Product request = null;

        // Execution
        var exception = assertThrows(IllegalArgumentException.class, () -> productService.update(request));

        // Verification
        var expectedMessage = "O objeto produto é inválido.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    // GET BY ID
    @Test
    void GivenExistingId_WhenCallGetById_ThenReturnProduct() {
        // Scenary
        Long request = 2L;
        var product = createNewProduct(2L);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Execution
        var foundProduct = productService.getById(request);

        // Verification
        assertEquals(request, foundProduct.getId());
    }

    @Test
    void GivenNonExistingId_WhenCallGetById_ThenThrowAnException() {
        // Scenary
        Long request = 2L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execution
        var exception = assertThrows(ObjectNotFoundBDException.class, () -> productService.getById(request));

        // Verification
        assertEquals(OBJECT_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void GivenNullId_WhenCallGetById_ThenThrowAnException() {
        // Scenary
        Long request = null;

        // Execution and Verification
        var exception = assertThrows(IllegalArgumentException.class, () -> productService.getById(request));
        assertEquals(INVALID_ID, exception.getMessage());
    }

    @Test
    void GivenAnPageble_WhenCallFindAll_ThenReturnPagePatient() {
        // Scenary
        PageRequest request = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(createNewProduct(2l), createNewProduct(3l));
        var page = new PageImpl<>(productList, request, productList.size());
        when(productRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);

        // Execution
        var result = productService.getPageProducts(request, "");

        // Verification
        assertEquals(2, result.getTotalElements());
        assertEquals(productList, result.getContent());
        assertEquals(0, result.getPageable().getPageNumber());
        assertEquals(10, result.getPageable().getPageSize());
    }

    @Test
    void GivenExistingId_WhenCallDelete_ThenSuccessfullyDeleteProduct() {
        // Scenary
        Long request = 1L;
        Product product = createNewProduct();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Execution
        productService.delete(request);

        // Verification
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void GivenNullId_WhenCallDelete_ThenThrowAnException() {
        // Scenary
        Long request = null;

        // Execution and Verification
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.delete(request));
        assertEquals(INVALID_ID, exception.getMessage());
        verify(productRepository, never()).delete(any());
    }

    @Test
    void GivenNonExistingId_WhenCallDelete_ThenThrowAnException() {
        // Scenary
        Long request = 1L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execution and Verification
        ObjectNotFoundBDException exception = assertThrows(ObjectNotFoundBDException.class, () -> productService.delete(request));
        assertEquals(OBJECT_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(productRepository, never()).delete(any());
    }
}

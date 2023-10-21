package com.harley.inventorymanagementapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harley.inventorymanagementapi.dtos.ProductDTO;
import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.exceptions.ExistsObjectInDBException;
import com.harley.inventorymanagementapi.exceptions.ObjectNotFoundBDException;
import com.harley.inventorymanagementapi.factories.ProductFactory;
import com.harley.inventorymanagementapi.services.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static com.harley.inventorymanagementapi.factories.ProductFactory.createNewProduct;
import static com.harley.inventorymanagementapi.factories.ProductFactory.createNewProductDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    private static String PRODUCT_URL = "/product";

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    // REGISTER
    @Test
    void GivenProductDTO_WhenCallRegister_ThenSaveAndReturnProduct() throws Exception {
        // Scenary
        var product = createNewProduct();
        BDDMockito.given(productService.register(any(Product.class))).willReturn(product);

        var productDTO = ProductFactory.createNewProductDTO();
        String json = new ObjectMapper().writeValueAsString(productDTO);

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void GivenProductDTO_WhenCallRegister_ThenThrowAnException() throws Exception {
        // Scenary
        String errorMessage = "Já existe um funcionário com este crm.";
        BDDMockito.given(productService.register(any(Product.class))).willThrow(new ExistsObjectInDBException(errorMessage));

        var productDTO = ProductFactory.createNewProductDTO();
        String json = new ObjectMapper().writeValueAsString(productDTO);

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_URL)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)));
    }

    // UPDATE
    @Test
    void GivenAnIdAndProductDTO_WhenCallUpdate_ThenSaveAndReturnProduct() throws Exception {
        // Scenary
        var product = createNewProduct();
        BDDMockito.given(productService.update(anyLong(), any(Product.class))).willReturn(product);

        var productDTO = ProductFactory.createNewProductDTO();
        String json = new ObjectMapper().writeValueAsString(productDTO);

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_URL.concat("/" + productDTO.id()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void GivenAnIdAndProductDTO_WhenCallUpdate_ThenThrowAnException() throws Exception {
        // Scenary
        String errorMessage = "Não foi possivel encontrar o produto.";
        BDDMockito.given(productService.update(anyLong(), any(Product.class))).willThrow(new ObjectNotFoundBDException(errorMessage));

        var productDTO = ProductFactory.createNewProductDTO();
        String json = new ObjectMapper().writeValueAsString(productDTO);

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_URL.concat("/" + productDTO.id()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)));
    }

    // GET BY ID
    @Test
    void GivenAnId_WhenCallGetProductById_ThenReturnProduct() throws Exception {
        // Scenary
        Long id = 1L;
        Product product = createNewProduct(id);
        BDDMockito.given(productService.getById(id)).willReturn(product);
        ProductDTO productDTO = createNewProductDTO();

        // Execution and Verification
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("code").value(productDTO.code()))
                .andExpect(jsonPath("name").value(productDTO.name()));

    }

    @Test
    void GivenNonExistentProduct_WhenCallGetById_ThenThrowAnException() throws Exception {
        // Scenary
        Long id = 1L;
        String errorMessage = "Não foi possível encontrar o produto.";
        BDDMockito.given(productService.getById(id)).willThrow(new ObjectNotFoundBDException(errorMessage));

        // Execution and Verification
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(errorMessage));
    }

    // PAGE
    @Test
    void GivenAnPageable_WhenCallGetProductPage_ThenReturnProductPage() throws Exception {
        // Scenary
        Product product1 = createNewProduct(1L);
        Product product2 = createNewProduct(2L);

        BDDMockito.given(productService.getPageProducts(Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(Arrays.asList(product1, product2), PageRequest.of(0, 100), 2));

        // Execution and Verification
        String url = String.format("?page=0&size=100");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_URL.concat(url))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(2)))
                .andExpect(jsonPath("totalElements").value(2))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    // DELETE
    @Test
    void GivenAnId_WhenCallDeleteProductById_ThenDeleteProductAndReturnStatus204() throws Exception {
        // Scenary
        BDDMockito.given(productService.getById(anyLong())).willReturn(createNewProduct(1L));

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PRODUCT_URL.concat("/" + 1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(productService, times(1)).delete(anyLong());
    }

    @Test
    void GivenAnId_WhenCallDeleteProductById_ThenThrowAnException() throws Exception {
        // Scenary
        String errorMessage = "Não foi possível encontrar o produto.";
        BDDMockito.given(productService.getById(anyLong())).willThrow(new ObjectNotFoundBDException(errorMessage));

        // Execution and Verification
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PRODUCT_URL.concat("/" + 1));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(productService, never()).delete(anyLong());
    }
}

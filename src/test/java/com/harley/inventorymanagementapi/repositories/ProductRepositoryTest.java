package com.harley.inventorymanagementapi.repositories;

import com.harley.inventorymanagementapi.entities.Product;
import com.harley.inventorymanagementapi.factories.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ProductRepository productRepository;

    @Test
    void GivenProduct_WhenCallExistsByCode_ThenReturnTrue() {
        // Scenary
        var product = ProductFactory.createNewProduct();
        testEntityManager.merge(product);

        // Execution
        var code = 33L;
        boolean exist = productRepository.existsByCode(code);

        // Verification
        Assertions.assertTrue(exist);
    }

    @Test
    void GivenNonExistentProduct_WhenCallExistsByCode_ThenReturnFalse() {
        // Scenary

        // Execution
        var code = 33L;
        boolean exist = productRepository.existsByCode(code);

        // Verification
        Assertions.assertFalse(exist);
    }
}

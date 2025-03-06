package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
    }

    @Test
void testEditProduct_Success() {
    // Arrange
    Product existingProduct = new Product();
    existingProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
    existingProduct.setProductName("Original Product");
    existingProduct.setProductQuantity(10);

    Product updatedProduct = new Product();
    updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
    updatedProduct.setProductName("Updated Product");
    updatedProduct.setProductQuantity(20);
    when(productRepository.update(updatedProduct)).thenReturn(updatedProduct);

    // Act
    Product result = productService.update(updatedProduct);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Product", result.getProductName());
    assertEquals(20, result.getProductQuantity());
    verify(productRepository, times(1)).update(updatedProduct);
    }

    @Test
    void testEditProduct_ProductNotFound() {
        // Arrange
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Non-existent Product");
        nonExistentProduct.setProductQuantity(5);

        when(productRepository.update(nonExistentProduct)).thenReturn(null);

        // Act
        Product result = productService.update(nonExistentProduct);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).update(nonExistentProduct);
    }

    @Test
void testDeleteProduct_Success() {
    // Arrange
    String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
    doNothing().when(productRepository).delete(productId);

    // Act
    productService.delete(productId);

    // Verify the delete method was called
    verify(productRepository, times(1)).delete(productId);
    }

    

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Arrange
        String nonExistentId = "non-existent-id";

        // Act - This should not throw an exception
        productService.delete(nonExistentId);

        // Assert - Verify the delete method was still called
        verify(productRepository, times(1)).delete(nonExistentId);
    }

    @Test
    void testFindById_Success() {
        // Arrange
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Test Product");
        existingProduct.setProductQuantity(10);

        when(productRepository.findById(productId)).thenReturn(existingProduct);

        // Act
        Product result = productService.findById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(10, result.getProductQuantity());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(productRepository.findById(nonExistentId)).thenReturn(null);

        // Act
        Product result = productService.findById(nonExistentId);

        // Assert
        assertNull(result);
    }
}
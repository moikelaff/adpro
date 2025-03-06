package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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

    // Test for create(Product product)
    @Test
    void testCreateProduct_Success() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setProductId("12345");
        newProduct.setProductName("New Product");
        newProduct.setProductQuantity(15);

        when(productRepository.create(newProduct)).thenReturn(newProduct);

        // Act
        Product result = productService.create(newProduct);

        // Assert
        assertNotNull(result);
        assertEquals("New Product", result.getProductName());
        assertEquals(15, result.getProductQuantity());
        verify(productRepository, times(1)).create(newProduct);
    }

    // Test for findAll()
    @Test
    void testFindAllProducts_Success() {
        // Arrange
        Product product1 = new Product();
        product1.setProductId("101");
        product1.setProductName("Product A");

        Product product2 = new Product();
        product2.setProductId("102");
        product2.setProductName("Product B");

        List<Product> productList = Arrays.asList(product1, product2);
        Iterator<Product> mockIterator = productList.iterator();

        when(productRepository.findAll()).thenReturn(mockIterator);

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getProductName());
        assertEquals("Product B", result.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }


    @Test
    void testEditProduct_Success() {
        Product existingProduct = new Product();
        existingProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        existingProduct.setProductName("Original Product");
        existingProduct.setProductQuantity(10);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(20);

        when(productRepository.update(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.update(updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        assertEquals(20, result.getProductQuantity());
        verify(productRepository, times(1)).update(updatedProduct);
    }

    @Test
    void testDeleteProduct_Success() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        doNothing().when(productRepository).delete(productId);

        productService.delete(productId);

        verify(productRepository, times(1)).delete(productId);
    }

    @Test
    void testFindById_Success() {
        String productId = "eb558e9f-1c39-460e-8860-71af6af63bd6";
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("Test Product");
        existingProduct.setProductQuantity(10);

        when(productRepository.findById(productId)).thenReturn(existingProduct);

        Product result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(10, result.getProductQuantity());
    }
}

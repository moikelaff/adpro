package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        Product savedProduct = productRepository.create(product);

        assertNotNull(savedProduct.getProductId());
        
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product retrievedProduct = productIterator.next();
        assertEquals(savedProduct.getProductId(), retrievedProduct.getProductId());
        assertEquals(product.getProductName(), retrievedProduct.getProductName());
        assertEquals(product.getProductQuantity(), retrievedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }
    
    @Test
    void testFindById_Success() {
        // Create a product first
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(25);
        Product savedProduct = productRepository.create(product);
        
        // Find the product by ID
        Product foundProduct = productRepository.findById(savedProduct.getProductId());
        
        // Verify
        assertNotNull(foundProduct);
        assertEquals(savedProduct.getProductId(), foundProduct.getProductId());
        assertEquals("Test Product", foundProduct.getProductName());
        assertEquals(25, foundProduct.getProductQuantity());
    }
    
    @Test
    void testFindById_NotFound() {
        // Try to find a product with non-existent ID
        Product foundProduct = productRepository.findById("non-existent-id");
        
        // Verify
        assertNull(foundProduct);
    }
    
    @Test
    void testUpdate_Success() {
        // Create a product first
        Product product = new Product();
        product.setProductName("Original Product");
        product.setProductQuantity(10);
        Product savedProduct = productRepository.create(product);
        
        // Update the product
        savedProduct.setProductName("Updated Product");
        savedProduct.setProductQuantity(20);
        Product updatedProduct = productRepository.update(savedProduct);
        
        // Verify the update was successful
        assertNotNull(updatedProduct);
        assertEquals(savedProduct.getProductId(), updatedProduct.getProductId());
        assertEquals("Updated Product", updatedProduct.getProductName());
        assertEquals(20, updatedProduct.getProductQuantity());
        
        // Verify the product was actually updated in the repository
        Product retrievedProduct = productRepository.findById(savedProduct.getProductId());
        assertEquals("Updated Product", retrievedProduct.getProductName());
        assertEquals(20, retrievedProduct.getProductQuantity());
    }
    
    @Test
    void testUpdate_ProductNotFound() {
        // Try to update a non-existent product
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Non-existent Product");
        nonExistentProduct.setProductQuantity(5);
        
        Product result = productRepository.update(nonExistentProduct);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    void testDelete_Success() {
        // Create a product first
        Product product = new Product();
        product.setProductName("Product to Delete");
        product.setProductQuantity(15);
        Product savedProduct = productRepository.create(product);
        
        // Verify product exists
        assertNotNull(productRepository.findById(savedProduct.getProductId()));
        
        // Delete the product
        productRepository.delete(savedProduct.getProductId());
        
        // Verify product no longer exists
        assertNull(productRepository.findById(savedProduct.getProductId()));
        
        // Verify product is no longer in the list
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }
    
    @Test
    void testDelete_ProductNotFound() {
        // Delete a non-existent product (should not throw exception)
        productRepository.delete("non-existent-id");
        
        // Verify repository state remains unchanged
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }
    
    @Test
    void testDelete_OneOfMultipleProducts() {
        // Create multiple products
        Product product1 = new Product();
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);
        Product savedProduct1 = productRepository.create(product1);
        
        Product product2 = new Product();
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);
        Product savedProduct2 = productRepository.create(product2);
        
        // Delete one product
        productRepository.delete(savedProduct1.getProductId());
        
        // Verify only that product was deleted
        assertNull(productRepository.findById(savedProduct1.getProductId()));
        assertNotNull(productRepository.findById(savedProduct2.getProductId()));
        
        // Verify only one product remains in the list
        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product remainingProduct = productIterator.next();
        assertEquals(savedProduct2.getProductId(), remainingProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }
}
package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;
    
    @Mock
    private ProductValidationService productValidationService;

    @InjectMocks
    private ProductController productController;

    private Model model;
    private Product product;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        verify(model).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost_Valid() {
        when(productService.create(any(Product.class))).thenReturn(product);
        when(productValidationService.getValidationMessage(any(Product.class))).thenReturn(null); // Valid product

        String viewName = productController.createProductPost(product, model);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).create(product);
    }

    @Test
    void testCreateProductPost_Invalid() {
        product.setProductName("");
        when(productValidationService.getValidationMessage(any(Product.class))).thenReturn("Product name cannot be empty");

        String viewName = productController.createProductPost(product, model);
        assertEquals("createProduct", viewName);
        verify(model).addAttribute(eq("error"), eq("Product name cannot be empty"));
        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void testProductListPage() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(productService.findAll()).thenReturn(products);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        verify(model).addAttribute(eq("products"), eq(products));
    }

    @Test
    void testEditProductPage() {
        when(productService.findById(anyString())).thenReturn(product);

        String viewName = productController.editProductPage("1", model);
        assertEquals("editProduct", viewName);
        verify(model).addAttribute(eq("product"), eq(product));
    }

    @Test
    void testEditProductPost() {
        when(productService.update(any(Product.class))).thenReturn(product);
        when(productValidationService.getValidationMessage(any(Product.class))).thenReturn(null); // Valid product

        String viewName = productController.editProductPost(product, model);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).update(product);
    }

    @Test
    void testDeleteProductPage() {
        when(productService.findById(anyString())).thenReturn(product);

        String viewName = productController.deleteProductPage("1", model);
        assertEquals("deleteProduct", viewName);
        verify(model).addAttribute(eq("product"), eq(product));
    }

    @Test
    void testDeleteProductPost() {
        doNothing().when(productService).delete(anyString());

        String viewName = productController.deleteProductPost(product);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).delete(product.getProductId());
    }
}
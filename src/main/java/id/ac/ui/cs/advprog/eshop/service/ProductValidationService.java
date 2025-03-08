package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductValidationService {
    public boolean isValid(Product product) {
        return product.getProductName() != null && !product.getProductName().trim().isEmpty();
    }
    
    public String getValidationMessage(Product product) {
        if (!isValid(product)) {
            return "Product name cannot be empty";
        }
        return null;
    }
}
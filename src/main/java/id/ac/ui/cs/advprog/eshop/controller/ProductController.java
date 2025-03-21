package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.service.ProductValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductValidationService validationService;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        String validationError = validationService.getValidationMessage(product);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            return "createProduct";
        }
        
        service.create(product);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }
    
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }
    
    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product, Model model) {
        String validationError = validationService.getValidationMessage(product);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            return "editProduct";
        }
        
        service.update(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute("product", product);
        return "deleteProduct"; 
    }

    @PostMapping("/delete")
    public String deleteProductPost(@ModelAttribute Product product) {
        service.delete(product.getProductId());
        return "redirect:/product/list";
    }
}
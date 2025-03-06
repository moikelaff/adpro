package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void testCreateProduct_Success(ChromeDriver driver) throws Exception {
        // 1. Navigate to product creation page
        driver.get(baseUrl + "/product/create");
        
        // 2. Verify we're on the correct page
        assertEquals("Create New Product", driver.getTitle());
        
        // 3. Fill in the product form
        String testProductName = "Test Product " + System.currentTimeMillis(); // Unique name
        int testProductQuantity = 50;
        
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.tagName("button"));
        
        nameInput.clear();
        nameInput.sendKeys(testProductName);
        
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(testProductQuantity));
        
        // 4. Submit the form
        submitButton.click();
        
        // 5. Verify we're redirected to the product list page
        assertEquals("Product List", driver.getTitle());
        
        // 6. Check if our new product appears in the list
        List<WebElement> productRows = driver.findElements(By.cssSelector("table tbody tr"));
        
        boolean productFound = false;
        for (WebElement row : productRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 3) { // Assuming at least 3 columns (ID, Name, Quantity)
                String productName = cells.get(1).getText();
                String productQuantity = cells.get(2).getText();
                
                if (productName.equals(testProductName) && productQuantity.equals(String.valueOf(testProductQuantity))) {
                    productFound = true;
                    break;
                }
            }
        }
        
        assertTrue(productFound, "The created product was not found in the product list");
    }
    
    @Test
    void testCreateProduct_EmptyName(ChromeDriver driver) throws Exception {
        // 1. Navigate to product creation page
        driver.get(baseUrl + "/product/create");
        
        // 2. Fill in the product form with empty name
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.tagName("button"));
        
        nameInput.clear(); // Leave the name empty
        
        quantityInput.clear();
        quantityInput.sendKeys("10");
        
        // 3. Submit the form
        submitButton.click();
        
        // 4. Check if we're still on the create product page (form validation should prevent submission)
        // This test assumes there's client-side validation
        assertEquals("Create New Product", driver.getTitle());
    }
    
    @Test
    void testCreateProduct_NavigateFromHomePage(ChromeDriver driver) throws Exception {
        // 1. Start from the home page
        driver.get(baseUrl);
        
        // 2. Navigate to product creation using the UI navigation
        WebElement createProductLink = driver.findElement(By.linkText("Create Product"));
        createProductLink.click();
        
        // 3. Verify we're on the create product page
        assertEquals("Create New Product", driver.getTitle());
        
        // 4. Fill in the product form
        String testProductName = "Navigated Product " + System.currentTimeMillis(); // Unique name
        int testProductQuantity = 25;
        
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        WebElement submitButton = driver.findElement(By.tagName("button"));
        
        nameInput.clear();
        nameInput.sendKeys(testProductName);
        
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(testProductQuantity));
        
        // 5. Submit the form
        submitButton.click();
        
        // 6. Verify we're redirected to the product list page
        assertEquals("Product List", driver.getTitle());
        
        // 7. Check if our new product appears in the list
        List<WebElement> productRows = driver.findElements(By.cssSelector("table tbody tr"));
        
        boolean productFound = false;
        for (WebElement row : productRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 3) {
                String productName = cells.get(1).getText();
                String productQuantity = cells.get(2).getText();
                
                if (productName.equals(testProductName) && productQuantity.equals(String.valueOf(testProductQuantity))) {
                    productFound = true;
                    break;
                }
            }
        }
        
        assertTrue(productFound, "The created product was not found in the product list");
    }
}
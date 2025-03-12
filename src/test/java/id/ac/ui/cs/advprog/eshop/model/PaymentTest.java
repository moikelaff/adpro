package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        // Create sample product
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(2);

        List<Product> products = new ArrayList<>();
        products.add(product);

        // Create a sample order
        order = Order.builder()
                .id("order-123")
                .products(products)
                .orderTime(1708560000L)
                .author("user123")
                .status("WAITING_PAYMENT")
                .build();
                
        // Create payment data
        paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123456");
    }

    @Test
    void testCreatePayment() {
        // Test creating a payment using constructor
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        
        assertEquals("payment-123", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }
    
    @Test
    void testCreatePaymentWithBuilder() {
        // Test creating a payment using builder
        Payment payment = Payment.builder()
                .id("payment-123")
                .method("BANK_TRANSFER")
                .status(PaymentStatus.PENDING.getValue())
                .paymentData(paymentData)
                .order(order)
                .build();
        
        assertEquals("payment-123", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }
    
    @Test
    void testSetId() {
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setId("payment-456");
        assertEquals("payment-456", payment.getId());
    }
    
    @Test
    void testSetMethod() {
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setMethod("VOUCHER");
        assertEquals("VOUCHER", payment.getMethod());
    }
    
    @Test
    void testSetStatus() {
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals("SUCCESS", payment.getStatus());
    }
    
    @Test
    void testSetPaymentData() {
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        
        Map<String, String> newPaymentData = new HashMap<>();
        newPaymentData.put("voucherCode", "ESHOP1234ABCD5678");
        
        payment.setPaymentData(newPaymentData);
        assertEquals(newPaymentData, payment.getPaymentData());
        assertEquals("ESHOP1234ABCD5678", payment.getPaymentData().get("voucherCode"));
    }
    
    @Test
    void testSetOrder() {
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        
        // Create a product for the new order
        Product newProduct = new Product();
        newProduct.setId("product-456");
        newProduct.setName("New Product");
        newProduct.setQuantity(1);
        
        List<Product> newProducts = new ArrayList<>();
        newProducts.add(newProduct);
        
        // Create a new order with at least one product
        Order newOrder = Order.builder()
                .id("order-456")
                .products(newProducts)  // Add the product list
                .orderTime(1708560001L)
                .author("user456")
                .status("WAITING_PAYMENT")
                .build();
        
        payment.setOrder(newOrder);
        assertEquals(newOrder, payment.getOrder());
        assertEquals("order-456", payment.getOrder().getId());
    }
}
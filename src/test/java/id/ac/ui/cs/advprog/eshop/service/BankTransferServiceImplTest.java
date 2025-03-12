package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankTransferServiceImplTest {

    @InjectMocks
    BankTransferServiceImpl bankTransferService;

    @Mock
    PaymentService paymentService;

    private Order order;
    private Payment validPayment;
    private Payment invalidPayment;

    @BeforeEach
    void setUp() {
        // Create a sample product
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(2);

        List<Product> products = new ArrayList<>();
        products.add(product);

        // Create a sample order using Order.builder() as you're still using that
        order = Order.builder()
                .id("order-123")
                .products(products)
                .orderTime(System.currentTimeMillis())
                .author("user123")
                .status("WAITING_PAYMENT")
                .build();

        // Create a valid payment
        Map<String, String> validPaymentData = new HashMap<>();
        validPaymentData.put("bankName", "BCA");
        validPaymentData.put("referenceCode", "REF123456");
        
        validPayment = Payment.builder()
                .id("payment-123")
                .method("BANK_TRANSFER")
                .status("PENDING")
                .paymentData(validPaymentData)
                .order(order)
                .build();
                
        // Create an invalid payment with empty fields
        Map<String, String> invalidPaymentData = new HashMap<>();
        invalidPaymentData.put("bankName", "");
        invalidPaymentData.put("referenceCode", "REF123456");
        
        invalidPayment = Payment.builder()
                .id("payment-456")
                .method("BANK_TRANSFER")
                .status("PENDING")
                .paymentData(invalidPaymentData)
                .order(order)
                .build();
    }

    @Test
    void testCreateBankTransferPaymentWithValidData() {
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class)))
                .thenReturn(validPayment);

        Payment result = bankTransferService.createBankTransferPayment(order, "BCA", "REF123456");

        assertNotNull(result);
        assertEquals("BANK_TRANSFER", result.getMethod());
        assertEquals("PENDING", result.getStatus());
        assertEquals("BCA", result.getPaymentData().get("bankName"));
        assertEquals("REF123456", result.getPaymentData().get("referenceCode"));
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class));
        // Should not set status to REJECTED for valid data
        verify(paymentService, never()).setStatus(validPayment, "REJECTED");
    }

    @Test
    void testCreateBankTransferPaymentWithEmptyBankName() {
        // Create payment with empty bank name
        Map<String, String> emptyBankData = new HashMap<>();
        emptyBankData.put("bankName", "");
        emptyBankData.put("referenceCode", "REF123456");
        
        Payment emptyBankPayment = Payment.builder()
                .id("payment-invalid")
                .method("BANK_TRANSFER")
                .status("PENDING")
                .paymentData(emptyBankData)
                .order(order)
                .build();
        
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class)))
                .thenReturn(emptyBankPayment);

        Payment result = bankTransferService.createBankTransferPayment(order, "", "REF123456");

        assertNotNull(result);
        verify(paymentService, times(1)).addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(emptyBankPayment, "REJECTED");
    }
    
    @Test
    void testCreateBankTransferPaymentWithEmptyReferenceCode() {
        // Create payment with empty reference code
        Map<String, String> emptyRefData = new HashMap<>();
        emptyRefData.put("bankName", "BCA");
        emptyRefData.put("referenceCode", "");
        
        Payment emptyRefPayment = Payment.builder()
                .id("payment-invalid")
                .method("BANK_TRANSFER")
                .status("PENDING")
                .paymentData(emptyRefData)
                .order(order)
                .build();
        
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class)))
                .thenReturn(emptyRefPayment);

        Payment result = bankTransferService.createBankTransferPayment(order, "BCA", "");

        assertNotNull(result);
        verify(paymentService, times(1)).addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(emptyRefPayment, "REJECTED");
    }
    
    @Test
    void testCreateBankTransferPaymentWithNullValues() {
        // Create payment with null values
        Map<String, String> nullData = new HashMap<>();
        nullData.put("bankName", null);
        nullData.put("referenceCode", null);
        
        Payment nullPayment = Payment.builder()
                .id("payment-invalid")
                .method("BANK_TRANSFER")
                .status("PENDING")
                .paymentData(nullData)
                .order(order)
                .build();
        
        when(paymentService.addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class)))
                .thenReturn(nullPayment);

        Payment result = bankTransferService.createBankTransferPayment(order, null, null);

        assertNotNull(result);
        verify(paymentService, times(1)).addPayment(eq(order), eq("BANK_TRANSFER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(nullPayment, "REJECTED");
    }

    @Test
    void testConfirmBankTransferPayment() {
        when(paymentService.getPayment("payment-123")).thenReturn(validPayment);
        when(paymentService.setStatus(validPayment, "SUCCESS")).thenReturn(validPayment);

        Payment result = bankTransferService.confirmBankTransferPayment("payment-123");

        assertNotNull(result);
        verify(paymentService, times(1)).getPayment("payment-123");
        verify(paymentService, times(1)).setStatus(validPayment, "SUCCESS");
    }

    @Test
    void testRejectBankTransferPayment() {
        when(paymentService.getPayment("payment-123")).thenReturn(validPayment);
        when(paymentService.setStatus(validPayment, "REJECTED")).thenReturn(validPayment);

        Payment result = bankTransferService.rejectBankTransferPayment("payment-123");

        assertNotNull(result);
        verify(paymentService, times(1)).getPayment("payment-123");
        verify(paymentService, times(1)).setStatus(validPayment, "REJECTED");
    }
}
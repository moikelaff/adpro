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
public class VoucherPaymentServiceImplTest {

    @InjectMocks
    VoucherPaymentServiceImpl voucherPaymentService;

    @Mock
    PaymentService paymentService;

    private Order order;
    private Payment validPayment;
    private Payment invalidPayment;

    @BeforeEach
    void setUp() {
        // Create a sample product
        Product product = Product.builder()
                .id("eb558e9f-1c39-460e-8860-71af6af63bd6")
                .name("Sampo Cap Bambang")
                .quantity(2)
                .price("12000")
                .build();

        List<Product> products = new ArrayList<>();
        products.add(product);

        // Create a sample order
        order = Order.builder()
                .id("order-123")
                .products(products)
                .orderTime(System.currentTimeMillis())
                .author("user123")
                .status("WAITING_PAYMENT")
                .build();

        // Create a valid payment (voucher code: ESHOP1234ABCD5678)
        Map<String, String> validPaymentData = new HashMap<>();
        validPaymentData.put("voucherCode", "ESHOP1234ABCD5678");
        
        validPayment = Payment.builder()
                .id("payment-123")
                .method("VOUCHER")
                .status("PENDING")
                .paymentData(validPaymentData)
                .order(order)
                .build();
                
        // Create an invalid payment (voucher code: INVALID)
        Map<String, String> invalidPaymentData = new HashMap<>();
        invalidPaymentData.put("voucherCode", "INVALID");
        
        invalidPayment = Payment.builder()
                .id("payment-456")
                .method("VOUCHER")
                .status("PENDING")
                .paymentData(invalidPaymentData)
                .order(order)
                .build();
    }

    @Test
    void testCreateVoucherPaymentWithValidVoucher() {
        // Test with a valid voucher that meets all criteria
        // 16 chars, starts with ESHOP, has 8 numerical chars
        String validVoucher = "ESHOP1234ABCD5678";
        
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), any(Map.class)))
                .thenReturn(validPayment);
        
        Payment result = voucherPaymentService.createVoucherPayment(order, validVoucher);
        
        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("VOUCHER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(validPayment, "SUCCESS");
    }

    @Test
    void testCreateVoucherPaymentWithInvalidLength() {
        // Test with voucher that is too short
        String invalidVoucher = "ESHOP123456";
        
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), any(Map.class)))
                .thenReturn(invalidPayment);
        
        Payment result = voucherPaymentService.createVoucherPayment(order, invalidVoucher);
        
        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("VOUCHER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(invalidPayment, "REJECTED");
    }
    
    @Test
    void testCreateVoucherPaymentWithInvalidPrefix() {
        // Test with voucher that doesn't start with ESHOP
        String invalidVoucher = "SHOP12345678ABCDE";
        
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), any(Map.class)))
                .thenReturn(invalidPayment);
        
        Payment result = voucherPaymentService.createVoucherPayment(order, invalidVoucher);
        
        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("VOUCHER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(invalidPayment, "REJECTED");
    }
    
    @Test
    void testCreateVoucherPaymentWithInsufficientNumbers() {
        // Test with voucher that doesn't have 8 numerical characters
        String invalidVoucher = "ESHOPABCDEFGHIJKL";
        
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), any(Map.class)))
                .thenReturn(invalidPayment);
        
        Payment result = voucherPaymentService.createVoucherPayment(order, invalidVoucher);
        
        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("VOUCHER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(invalidPayment, "REJECTED");
    }
    
    @Test
    void testCreateVoucherPaymentWithNullVoucher() {
        // Test with null voucher
        when(paymentService.addPayment(eq(order), eq("VOUCHER"), any(Map.class)))
                .thenReturn(invalidPayment);
        
        Payment result = voucherPaymentService.createVoucherPayment(order, null);
        
        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        
        verify(paymentService, times(1)).addPayment(eq(order), eq("VOUCHER"), any(Map.class));
        verify(paymentService, times(1)).setStatus(invalidPayment, "REJECTED");
    }
}
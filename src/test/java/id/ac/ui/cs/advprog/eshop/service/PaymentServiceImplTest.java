package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    private Order order;
    private Payment payment;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        // Create a sample product
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(2);

        products = new ArrayList<>();
        products.add(product);

        // Create a sample order
        order = Order.builder()
                .id("order-123")
                .products(products)
                .orderTime(System.currentTimeMillis())
                .author("user123")
                .status("WAITING_PAYMENT")
                .build();

        // Create a sample payment
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "DISCOUNT50");
        
        payment = Payment.builder()
                .id("payment-123")
                .method("VOUCHER")
                .status("PENDING")
                .paymentData(paymentData)
                .order(order)
                .build();
    }

    @Test
    void testAddPayment() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "DISCOUNT50");
        
        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertNotNull(result);
        assertEquals("VOUCHER", result.getMethod());
        assertEquals("PENDING", result.getStatus());
        assertEquals(paymentData, result.getPaymentData());
        assertEquals(order, result.getOrder());
        
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        
        verify(orderService, times(1)).updateStatus(order.getId(), "SUCCESS");
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        
        verify(orderService, times(1)).updateStatus(order.getId(), "FAILED");
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPayment() {
        when(paymentRepository.findById("payment-123")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-123");

        assertNotNull(result);
        assertEquals(payment, result);
        verify(paymentRepository, times(1)).findById("payment-123");
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        
        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
        verify(paymentRepository, times(1)).findAll();
    }
}
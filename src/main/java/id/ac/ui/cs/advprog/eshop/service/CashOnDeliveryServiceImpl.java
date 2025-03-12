package id.ac.ui.cs.advprog.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.Map;
import java.util.HashMap;

@Service
public class CashOnDeliveryServiceImpl {
    
    @Autowired
    private PaymentService paymentService;
    
    public Payment createCODPayment(Order order, String address, String phoneNumber) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("deliveryAddress", address);
        paymentData.put("phoneNumber", phoneNumber);
        
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        
        return payment;
    }
    
    public Payment confirmCODPayment(String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null && "CASH_ON_DELIVERY".equals(payment.getMethod())) {
            return paymentService.setStatus(payment, "SUCCESS");
        }
        return payment;
    }
    
    public Payment rejectCODPayment(String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null && "CASH_ON_DELIVERY".equals(payment.getMethod())) {
            return paymentService.setStatus(payment, "REJECTED");
        }
        return payment;
    }
}
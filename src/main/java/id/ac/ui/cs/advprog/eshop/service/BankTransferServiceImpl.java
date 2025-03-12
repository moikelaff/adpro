package id.ac.ui.cs.advprog.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.Map;
import java.util.HashMap;

@Service
public class BankTransferServiceImpl {
    
    @Autowired
    private PaymentService paymentService;
    
    public Payment createBankTransferPayment(Order order, String bankName, String referenceCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", bankName);
        paymentData.put("referenceCode", referenceCode);
        
        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);
        
        // Validate bank transfer details
        if (isValidBankTransfer(bankName, referenceCode)) {
            // In a real system, payment would remain PENDING until confirmation
            // But according to the requirements, we'll leave it as PENDING
        } else {
            paymentService.setStatus(payment, "REJECTED");
        }
        
        return payment;
    }
    
    private boolean isValidBankTransfer(String bankName, String referenceCode) {
        // Payment is REJECTED if any field is empty or null
        return bankName != null && !bankName.isEmpty() && 
               referenceCode != null && !referenceCode.isEmpty();
    }
    
    public Payment confirmBankTransferPayment(String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null && "BANK_TRANSFER".equals(payment.getMethod())) {
            return paymentService.setStatus(payment, "SUCCESS");
        }
        return payment;
    }
    
    public Payment rejectBankTransferPayment(String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null && "BANK_TRANSFER".equals(payment.getMethod())) {
            return paymentService.setStatus(payment, "REJECTED");
        }
        return payment;
    }
}
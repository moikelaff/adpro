package id.ac.ui.cs.advprog.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.Map;
import java.util.HashMap;

@Service
public class VoucherPaymentServiceImpl {
    
    @Autowired
    private PaymentService paymentService;
    
    public Payment createVoucherPayment(Order order, String voucherCode) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", voucherCode);
        
        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);
        
        // Validate voucher and set status accordingly
        if (isValidVoucher(voucherCode)) {
            paymentService.setStatus(payment, "SUCCESS");
        } else {
            paymentService.setStatus(payment, "REJECTED");
        }
        
        return payment;
    }
    
    private boolean isValidVoucher(String voucherCode) {
        // Rule 1: Must be 16 characters long
        if (voucherCode == null || voucherCode.length() != 16) {
            return false;
        }
        
        // Rule 2: Must start with "ESHOP"
        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }
        
        // Rule 3: Must contain 8 numerical characters
        int numCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numCount++;
            }
        }
        
        return numCount == 8;
    }
}
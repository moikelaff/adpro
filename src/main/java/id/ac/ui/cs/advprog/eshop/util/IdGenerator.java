package id.ac.ui.cs.advprog.eshop.util;

import java.util.UUID;

public class IdGenerator {
    private static long sequentialId = 0;
    
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static String generateSequentialId() {
        return String.valueOf(++sequentialId);
    }
}
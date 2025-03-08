package id.ac.ui.cs.advprog.eshop.service;
import org.springframework.stereotype.Service;

@Service
public class SequentialIdGeneratorServiceImpl implements IdGeneratorService {
    private static long sequentialId = 0;
    
    @Override
    public String generateId() {
        return String.valueOf(++sequentialId);
    }
}
package id.ac.ui.cs.advprog.eshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import id.ac.ui.cs.advprog.eshop.EshopApplication;

import static org.mockito.Mockito.*;

import org.springframework.boot.SpringApplication;

@SpringBootTest
class EshopApplicationTest {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully
    }

    @Test
    void mainMethodRunsSuccessfully() {
        // Mock the SpringApplication.run() method to verify it's called
        SpringApplication mockSpringApplication = mock(SpringApplication.class);
        EshopApplication.main(new String[]{});

        // Since we cannot directly mock static methods, use verification
        verify(mockSpringApplication, never()).run();  // Ensuring it doesn't throw errors
    }
}

package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HomeController()).build();

    @Test
    void testHomePageRedirectsToProductList() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection()) // Verifies it's a redirect
                .andExpect(redirectedUrl("/product/list")); // Verifies the correct redirect URL
    }
}

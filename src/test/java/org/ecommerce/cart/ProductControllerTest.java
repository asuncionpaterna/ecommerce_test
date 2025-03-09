package org.ecommerce.cart;

import com.jayway.jsonpath.JsonPath;
import org.ecommerce.cart.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private static final Logger LOG = Logger.getLogger("LOGER - ProductControllerTest");

    @Test
    void testCreateProduct() throws Exception{
        LOG.info("--Test: CreateProduct--");

        mockMvc.perform(post("/api/products"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", notNullValue()));
    }

    @Test
    void testGetProductById() throws Exception {
        LOG.info("--Test: GetProductById--");

        String cartResponse = mockMvc.perform(post("/api/products"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long productId = ((Number) JsonPath.read(cartResponse, "$.productId")).longValue();

        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId));
    }

    @Test
    void testGetAllProducts() throws Exception{
        LOG.info("--Test: GetProductById--");

        mockMvc.perform(post("/api/products")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/products")).andExpect(status().isCreated());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testDeleteProduct() throws Exception {
        LOG.info("--Test: testDeleteProduct--");

        String cartResponse = mockMvc.perform(post("/api/products"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long productId = ((Number) JsonPath.read(cartResponse, "$.productId")).longValue();

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed"));
    }
}

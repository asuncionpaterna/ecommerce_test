package org.ecommerce.cart;

import org.ecommerce.cart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService;

    @Test
    void testCreateCart() throws Exception {
        mockMvc.perform(post("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId", notNullValue()));
    }

    @Test
    void testAddProductToCart() throws Exception {
        Long cartId = cartService.createCart().getCartId();
        String productJson = """
                {
                    "description": "Teclado",
                    "amount": 2
                }
                """;

        mockMvc.perform(post("/api/carts/" + cartId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto añadido al carrito"));
    }

    @Test
    void testCartAutoDeletion() throws Exception {

        Long cartId = cartService.createCart().getCartId();

        // 70 segundos
        Thread.sleep(70000);


        mockMvc.perform(get("/api/carts/" + cartId))
                .andExpect(status().isNotFound());
    }
}

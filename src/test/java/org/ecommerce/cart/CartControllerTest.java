package org.ecommerce.cart;

import com.jayway.jsonpath.JsonPath;
import org.ecommerce.cart.model.Cart;
import org.ecommerce.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService;

    private static final Logger LOG = Logger.getLogger("LOOGER - CartControllerTest");

    @BeforeEach
    void cleanUp() throws Exception {
        LOG.info("--Cleaning up all carts before test--");

        var result = mockMvc.perform(delete("/api/carts")).andReturn();
        int status = result.getResponse().getStatus();

        assertThat(status).isIn(200, 204);
    }


    @Test
    void testCreateCart() throws Exception {
        LOG.info("--Test: CreateCart--");
        mockMvc.perform(post("/api/carts"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId", notNullValue()));
    }

    @Test
    void testGetCartById() throws Exception {
        LOG.info("--Test: GetCartById--");
        String cartResponse = mockMvc.perform(post("/api/carts"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long cartId = ((Number) JsonPath.read(cartResponse, "$.cartId")).longValue();

        mockMvc.perform(get("/api/carts/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cartId));
    }

    @Test
    void testGetAllCarts() throws Exception {
        LOG.info("--Test: GetAllCarts--");

        cartService.deleteAllCarts();

        mockMvc.perform(post("/api/carts")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/carts")).andExpect(status().isCreated());

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testAddProductToCart() throws Exception {
        LOG.info("--Test: AddProductToCart--");

        Long cartId = cartService.createCart().getCartId();
        String productJson = """
                {
                    "description": "Notebook",
                    "amount": 2
                }
                """;

        mockMvc.perform(post("/api/carts/" + cartId + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to cart"));
    }

    @Test
    void testRemoveProductFromCart() throws Exception {
        LOG.info("--Test: RemoveProductFromCart--");

        Cart cart = cartService.createCart();

        String productJson = """
                {
                    "description": "Notebook",
                    "amount": 2
                }
                """;

        mockMvc.perform(post("/api/carts/" + cart.getCartId() + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to cart"));

        LOG.info(""+cart.getProductList());
/*
        mockMvc.perform(delete("/api/carts/" + cart.getCartId() + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from cart"));*/
    }

    @Test
    void testDeleteCart() throws Exception {
        LOG.info("--Test: testDeleteCart--");
        String cartResponse = mockMvc.perform(post("/api/carts"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long cartId = ((Number) JsonPath.read(cartResponse, "$.cartId")).longValue();

        mockMvc.perform(delete("/api/carts/" + cartId))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart removed"));
    }

    @Test
    void testDeleteAllCarts() throws Exception {
        LOG.info("--Test: DeleteAllCarts--");

        mockMvc.perform(post("/api/carts")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/carts")).andExpect(status().isCreated());

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(delete("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(content().string("All carts have been removed"));

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/carts"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No carts to remove"));
    }


    @Test
    void testCleanInactiveCarts() throws Exception {
        LOG.info("--Test: cleanInactiveCarts--");
        Long cartId = cartService.createCart().getCartId();

        // 70 segundos
        Thread.sleep(30000);
        mockMvc.perform(get("/api/carts/" + cartId))
                .andExpect(status().isNotFound());
    }
}

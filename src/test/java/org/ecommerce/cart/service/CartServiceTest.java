package org.ecommerce.cart.service;

import org.ecommerce.cart.model.Cart;
import org.ecommerce.cart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class CartServiceTest {
    private CartService cartService;
    private static final Logger LOG = Logger.getLogger("LOGER - CartServiceTest");


    @BeforeEach
    void setUp() {
        LOG.info("--Test: SetUp--");
        cartService = new CartService();
        //cartService.deleteAllCarts();
    }

    @Test
    void testCreateCart() {
        LOG.info("--Test: Create cart--");
        Cart cart = cartService.createCart();
        assertThat(cart).isNotNull();
        assertThat(cart.getCartId()).isNotNull();
    }

    @Test
    void testGetCartById() {
        LOG.info("--Test: Get cart by id--");
        Cart cart = cartService.createCart();
        assertThat(cartService.getCartById(cart.getCartId())).isPresent();
    }

    @Test
    void testFindAllCarts_NoCartsExist(){
        LOG.info("--Test: FindAllCarts - No Exist--");
        List<Cart> carts = cartService.findAllCarts();
        assertThat(carts).isEmpty();
    }

    @Test
    void testFindAllCarts_CartsExist(){
        LOG.info("--Test: FindAllCarts - Exist--");
        cartService.createCart();
        cartService.createCart();

        List<Cart> carts = cartService.findAllCarts();

        assertThat(carts).isNotEmpty();
        assertThat(carts).hasSize(2);
    }

    @Test
    void testAddProductToCart() {
        LOG.info("--Test: Add product to cart--");
        Cart cart = cartService.createCart();
        Product product = Product.builder()
                .description("Laptop")
                .amount(1)
                .build();

        boolean added = cartService.addProductToCart(cart.getCartId(), product);
        assertThat(added).isTrue();
        assertThat(cart.getProductList()).isNotEmpty();
    }

    @Test
    void testRemoveProductFromCart() {
        LOG.info("--Test: Remove product from cart--");
        Cart cart = cartService.createCart();
        Product product = Product.builder()
                .productId(1L)
                .description("Lamp")
                .amount(1)
                .build();

        cartService.addProductToCart(cart.getCartId(), product);
        boolean removed = cartService.deleteProductCart(cart.getCartId(),product.getProductId());
        assertThat(removed).isTrue();
        assertThat(cart.getProductList()).isEmpty();
    }

    @Test
    void testDeleteCart() {
        LOG.info("--Test: Delete cart--");
        Cart cart = cartService.createCart();
        boolean deleted = cartService.deleteCart(cart.getCartId());
        assertThat(deleted).isTrue();
        assertThat(cartService.getCartById(cart.getCartId())).isEmpty();
    }

    @Test
    void testDeleteAllInactiveCarts() throws InterruptedException {
        LOG.info("--Test: Delete all inactive carts--");

        Cart cart = cartService.createCart();
        Instant time = (Instant.now()).minus(Duration.ofMinutes(10));
        cart.setLastUpdate(time);

        //30 segundos
        Thread.sleep(30000);
        cartService.cleanInactiveCarts();

        assertThat(cartService.getCartById(cart.getCartId())).isEmpty();
    }
}

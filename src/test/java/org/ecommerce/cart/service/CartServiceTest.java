package org.ecommerce.cart.service;

import org.ecommerce.cart.model.Cart;
import org.ecommerce.cart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static java.lang.Thread.sleep;

class CartServiceTest {
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    @Test
    void testCreateCart() {
        System.out.println("Crear carrito");
        Cart cart = cartService.createCart();
        assertThat(cart).isNotNull();
        assertThat(cart.getCartId()).isNotNull();
    }

    @Test
    void testAddProductToCart() {
        System.out.println("Añadir producto al carrito");
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
    void testCartAutoDeletion() throws InterruptedException {
        System.out.println("Limpieza carritos inactivos");
        // se crea un carrito
        Cart cart = cartService.createCart();
        Long cartId = cart.getCartId();

        // //70 segundos
        sleep(70000);

        // Ejecutar la limpieza de carritos inactivos
        cartService.cleanInactiveCarts();

        // Verificar que el carrito ya no existe
        assertThat(cartService.getCart(cartId)).isEmpty();
    }
}

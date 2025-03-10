package org.ecommerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.cart.model.*;
import org.ecommerce.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        //Ternario
        return cartService.getCartById(cartId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.findAllCarts();
        return carts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carts);
    }


    @PostMapping("/{cartId}/product")
    public ResponseEntity<String>  addProductToCart(@PathVariable Long cartId, @RequestBody Product product) {
        return cartService.addProductToCart(cartId, product)
                ? ResponseEntity.ok("Product added to cart")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return cartService.deleteProductCart(cartId, productId)
                ? ResponseEntity.ok("Product removed from cart")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {
        return cartService.deleteCart(cartId)
                ? ResponseEntity.ok("Cart removed")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllCarts() {
        if (cartService.findAllCarts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No carts to remove");
        }
        cartService.deleteAllCarts();
        return ResponseEntity.ok("All carts have been removed");
    }
}

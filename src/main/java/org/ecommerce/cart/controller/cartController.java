package org.ecommerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.cart.model.*;
import org.ecommerce.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class cartController {


    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        //Ternario
        return cartService.getCart(cartId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/products")
    public ResponseEntity<String>  addProductToCart(@PathVariable Long cartId, @RequestBody Product product) {
        return cartService.addProductToCart(cartId, product)
                ? ResponseEntity.ok("Producto añadido al carrito")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {
        return cartService.deleteCart(cartId)
                ? ResponseEntity.ok("Carrito eliminado")
                : ResponseEntity.notFound().build();
    }

}

package org.ecommerce.cart.controller;

import lombok.NoArgsConstructor;
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

    @PostMapping //Crear carrito vacio
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping("/{cartId}") //Coger el carrito
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        //Ternario
        return cartService.getCartById(cartId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping //Coger todos los carritos
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.findAllCarts();
        return carts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carts);
    }


    @PostMapping("/{cartId}/product") //Añadir un producto a carrito
    public ResponseEntity<String>  addProductToCart(@PathVariable Long cartId, @RequestBody Product product) {
        return cartService.addProductToCart(cartId, product)
                ? ResponseEntity.ok("Product added to cart")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}/product") // Eliminar un producto a carrito
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long cartId, @RequestBody Product product) {
        return cartService.deleteProductCart(cartId, product)
                ? ResponseEntity.ok("Product removed from cart")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}") //Eliminar carrito
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

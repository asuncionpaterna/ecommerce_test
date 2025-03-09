package org.ecommerce.cart.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.cart.model.Product;
import org.ecommerce.cart.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(){
        Product product = productService.createProduct();
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{cartId}") //Coger el carrito
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {

        return productService.getProductById(productId) //getId
                .map(ResponseEntity::ok) //Si se mapea OK
                .orElseGet(()-> ResponseEntity.notFound().build()); //Si no que responda con un notfound
    }

    @GetMapping //Coger todos los carritos
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @DeleteMapping("/{productId}") //Eliminar carrito
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId)
                ? ResponseEntity.ok("Product removed")
                : ResponseEntity.notFound().build();
    }

}

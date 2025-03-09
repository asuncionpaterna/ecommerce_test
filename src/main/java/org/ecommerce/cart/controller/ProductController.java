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

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {

        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId)
                ? ResponseEntity.ok("Product removed")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllProducts() {
        if (productService.findAllProducts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No products to remove");
        }
        productService.deleteAllProducts();
        return ResponseEntity.ok("All products have been removed");
    }
}

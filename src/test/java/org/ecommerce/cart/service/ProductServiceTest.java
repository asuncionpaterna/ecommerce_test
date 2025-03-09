package org.ecommerce.cart.service;

import org.ecommerce.cart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {
    private ProductService productService;

    private static final Logger LOG = Logger.getLogger("LOGER - ProductServiceTest");

    @BeforeEach
    void setUp() {
        LOG.info("--Test: SetUp--");
        productService = new ProductService();
    }

    @Test
    void testCreateProduct() {
        LOG.info("--Test: Create product--");
        Product product = productService.createProduct();
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isNotNull();
    }

    @Test
    void testGetProductById() {
        LOG.info("--Test: Get product by id--");
        Product product = productService.createProduct();
        assertThat(productService.getProductById(product.getProductId())).isPresent();
    }

    @Test
    void testFindAllProducts_NoProductsExist(){
        LOG.info("--Test: FindAllProducts - No Exist--");
        List<Product> products = productService.findAllProducts();
        assertThat(products).isEmpty();
    }

    @Test
    void testFindAllProducts_ProductsExist(){
        LOG.info("--Test: FindAllProducts - Exist--");
        productService.createProduct();
        productService.createProduct();

        List<Product> products = productService.findAllProducts();
        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(2);
    }

    @Test
    void testDeleteProduct() {
        LOG.info("--Test: Delete product--");
        Product product = productService.createProduct();
        boolean deleted = productService.deleteProduct(product.getProductId());
        assertThat(deleted).isTrue();
        assertThat(productService.getProductById(product.getProductId())).isEmpty();
    }
}

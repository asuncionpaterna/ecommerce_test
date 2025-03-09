package org.ecommerce.cart.service;


import org.ecommerce.cart.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class ProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger("LOGGER - ProductService");

    public Product createProduct(){
        Product product = new Product();
        products.put(product.getProductId(), product);
        LOG.info("Product created with ID: "+ product.getProductId());
        return product;
    }

    public List<Product> findAllProducts(){
        return  new ArrayList<>(products.values());
    }

    public Optional<Product> getProductById(Long productId){
        return Optional.ofNullable(products.get(productId));
    }

    public boolean deleteProduct(Long productId){
        if (products.containsKey(productId)) {
            products.remove(productId);
            LOG.info("Product with ID "+ productId+" deleted ");
            return true;
        }
        return false;
    }

    public void deleteAllProducts(){
        if(products.isEmpty()){
            LOG.info("No products to remove.");
        }
        products.clear();
        LOG.info("All products have been remove.");

    }
}

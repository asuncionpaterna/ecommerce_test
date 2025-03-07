package org.ecommerce.cart.service;

import org.ecommerce.cart.model.Cart;
import org.ecommerce.cart.model.Product;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class CartService {
    private final Map<Long, Cart> carts = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger("LOGGER - CartService");
    public Cart createCart() {
        Cart cart = new Cart();
        carts.put(cart.getCartId(), cart);
        LOG.info("Cart created with ID: "+ cart.getCartId());
        return cart;
    }

    public List<Cart> findAllCarts(){
        return new ArrayList<>(carts.values());
    }

    public Optional<Cart> getCartById(Long cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }

    public boolean addProductToCart(Long cartId, Product product) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            product.setProductId(Product.generateId());
            cart.getProductList().add(product);
            cart.setLastUpdate(Instant.now());
            LOG.info("Product  "+product+" added to cart "+ cartId);
            return true;
        }
        return false;
    }

    public boolean deleteProductCart(Long cartId, Product product){
        Cart cart = carts.get(cartId);
        return cart != null && cart.getProductList().removeIf(pr -> pr.equals(product));
    }

    public boolean deleteCart(Long cartId) {
        if (carts.containsKey(cartId)) {
            carts.remove(cartId);
            LOG.info("Cart with ID "+ cartId+" deleted ");
            return true;
        }
        return false;
    }

    public void deleteAllCarts() {
        if (carts.isEmpty()) {
            LOG.info("No carts to remove.");
            return;
        }
        carts.clear();
        LOG.info("All carts have been removed.");
    }

    @Scheduled(fixedRate = 6000) //6 Segundos
    public void cleanInactiveCarts() {
       // LOG.info("Comprobando los carritos inactivos");
        Instant now = Instant.now();
        carts.entrySet().removeIf(entry -> entry.getValue().getLastUpdate().plusSeconds(60).isBefore(now)); //
    }
}

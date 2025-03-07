package org.ecommerce.cart.service;

import org.ecommerce.cart.model.Cart;
import org.ecommerce.cart.model.Product;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class CartService {
    private final Map<Long, Cart> carts = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger("CarritoServiceLog");
    public Cart createCart() {
        Cart cart = new Cart();
        carts.put(cart.getCartId(), cart);
        LOG.info("Carrito creado con ID: "+ cart.getCartId());
        return cart;
    }

    public Optional<Cart> getCart(Long cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }

    public boolean addProductToCart(Long cartId, Product product) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            product.setProductId(Product.generateId());
            cart.getProductList().add(product);
            cart.setLastUpdated(Instant.now());
            LOG.info("Producto "+product+" agregado al carrito "+ cartId);
            return true;
        }
        return false;
    }

    public boolean deleteCart(Long cartId) {
        if (carts.containsKey(cartId)) {
            carts.remove(cartId);
            LOG.info("Carrito con ID "+ cartId+" eliminado ");
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate =30000)
    public void cleanInactiveCarts() {
        Instant now = Instant.now();
        carts.entrySet().removeIf(entry -> entry.getValue().getLastUpdated().plusSeconds(60).isBefore(now));
        LOG.info("Carritos inactivos eliminados");
    }
}

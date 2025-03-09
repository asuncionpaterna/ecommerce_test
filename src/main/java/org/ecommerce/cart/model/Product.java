package org.ecommerce.cart.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Product {

    private static long contadorId = 0;
    protected Long productId;
    protected String description;
    protected double amount;

    public Product(){
        this.productId = generateId();
    }
    public static long generateId(){
        return ++contadorId;
    }
}

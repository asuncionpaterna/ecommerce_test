package org.ecommerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Cart {

    private static long contadorId =0;
    private Long cartId;
    private List<Product> productList = new ArrayList<>();
    private Instant lastUpdated;

    public Cart(){
        this.cartId=generateId();
        this.lastUpdated = Instant.now();
    }

    public static long generateId(){
        return ++contadorId;
    }


}

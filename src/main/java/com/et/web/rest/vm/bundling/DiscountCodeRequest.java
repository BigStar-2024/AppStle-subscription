
package com.et.web.rest.vm.bundling;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cart",
})
public class DiscountCodeRequest {
    @JsonProperty("cart")
    private Cart cart;

    @JsonProperty("cart")
    public Cart getCart() {
        return cart;
    }

    @JsonProperty("cart")
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

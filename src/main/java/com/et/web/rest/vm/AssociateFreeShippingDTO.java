package com.et.web.rest.vm;

import java.util.HashSet;
import java.util.Set;

public class AssociateFreeShippingDTO {

    private Long id;
    private Set<String> sellerGroupIds = new HashSet<>();

    public Set<String> getSellerGroupIds() {
        return sellerGroupIds;
    }

    public void setSellerGroupIds(Set<String> sellerGroupIds) {
        this.sellerGroupIds = sellerGroupIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

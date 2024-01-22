package com.et.pojo;

import com.et.service.dto.CollectionVM;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ProductFilterDTO {
    private Set<String> vendor;
    private Set<String> tags;
    private Set<String> productType;
    private Set<CollectionVM> collection;

    public ProductFilterDTO(Set<String> vendor, Set<String> tags, Set<String> productType) {
        this.vendor = vendor;
        this.tags = tags;
        this.productType = productType;
    }

    public ProductFilterDTO() {
    }

    public Set<String> getVendor() {
        return vendor;
    }

    public void setVendor(Set<String> vendor) {
        this.vendor = vendor;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getProductType() {
        return productType;
    }

    public void setProductType(Set<String> productType) {
        this.productType = productType;
    }

    public Set<CollectionVM> getCollection() {
        return collection;
    }

    public void setCollection(Set<CollectionVM> collection) {
        this.collection = collection;
    }

    public ProductFilterDTO(String vendor, String tags, String productType) {
       this.vendor = StringUtils.isNotBlank(vendor) ? Set.of(vendor.split(",")) : new HashSet<>();
       this.tags = StringUtils.isNotBlank(tags) ? Set.of(tags.split(",")) : new HashSet<>();
       this.productType = StringUtils.isNotBlank(productType) ? Set.of(productType.split(",")) : new HashSet<>();
    }
}

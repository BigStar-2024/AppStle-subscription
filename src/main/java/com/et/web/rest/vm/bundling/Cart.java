
package com.et.web.rest.vm.bundling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "token",
    "note",
    "attributes",
    "original_total_price",
    "total_price",
    "total_discount",
    "total_weight",
    "item_count",
    "items",
    "requires_shipping",
    "currency",
    "items_subtotal_price",
    "cart_level_discount_applications"
})
@Generated("jsonschema2pojo")
public class Cart implements Serializable
{

    @JsonProperty("token")
    private String token;
    @JsonProperty("note")
    private Object note;
    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonProperty("original_total_price")
    private Long originalTotalPrice;
    @JsonProperty("total_price")
    private Long totalPrice;
    @JsonProperty("total_discount")
    private Long totalDiscount;
    @JsonProperty("total_weight")
    private Long totalWeight;
    @JsonProperty("item_count")
    private Long itemCount;
    @JsonProperty("items")
    private List<Item> items = new ArrayList<Item>();
    @JsonProperty("requires_shipping")
    private Boolean requiresShipping;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("items_subtotal_price")
    private Long itemsSubtotalPrice;
    @JsonProperty("cart_level_discount_applications")
    private List<Object> cartLevelDiscountApplications = new ArrayList<Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8867840603609001614L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cart() {
    }

    /**
     * 
     * @param note
     * @param itemsSubtotalPrice
     * @param totalPrice
     * @param token
     * @param itemCount
     * @param requiresShipping
     * @param cartLevelDiscountApplications
     * @param originalTotalPrice
     * @param totalWeight
     * @param totalDiscount
     * @param attributes
     * @param currency
     * @param items
     */
    public Cart(String token, Object note, Attributes attributes, Long originalTotalPrice, Long totalPrice, Long totalDiscount, Long totalWeight, Long itemCount, List<Item> items, Boolean requiresShipping, String currency, Long itemsSubtotalPrice, List<Object> cartLevelDiscountApplications) {
        super();
        this.token = token;
        this.note = note;
        this.attributes = attributes;
        this.originalTotalPrice = originalTotalPrice;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.totalWeight = totalWeight;
        this.itemCount = itemCount;
        this.items = items;
        this.requiresShipping = requiresShipping;
        this.currency = currency;
        this.itemsSubtotalPrice = itemsSubtotalPrice;
        this.cartLevelDiscountApplications = cartLevelDiscountApplications;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("note")
    public Object getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(Object note) {
        this.note = note;
    }

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("original_total_price")
    public Long getOriginalTotalPrice() {
        return originalTotalPrice;
    }

    @JsonProperty("original_total_price")
    public void setOriginalTotalPrice(Long originalTotalPrice) {
        this.originalTotalPrice = originalTotalPrice;
    }

    @JsonProperty("total_price")
    public Long getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("total_price")
    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("total_discount")
    public Long getTotalDiscount() {
        return totalDiscount;
    }

    @JsonProperty("total_discount")
    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @JsonProperty("total_weight")
    public Long getTotalWeight() {
        return totalWeight;
    }

    @JsonProperty("total_weight")
    public void setTotalWeight(Long totalWeight) {
        this.totalWeight = totalWeight;
    }

    @JsonProperty("item_count")
    public Long getItemCount() {
        return itemCount;
    }

    @JsonProperty("item_count")
    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("requires_shipping")
    public Boolean getRequiresShipping() {
        return requiresShipping;
    }

    @JsonProperty("requires_shipping")
    public void setRequiresShipping(Boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("items_subtotal_price")
    public Long getItemsSubtotalPrice() {
        return itemsSubtotalPrice;
    }

    @JsonProperty("items_subtotal_price")
    public void setItemsSubtotalPrice(Long itemsSubtotalPrice) {
        this.itemsSubtotalPrice = itemsSubtotalPrice;
    }

    @JsonProperty("cart_level_discount_applications")
    public List<Object> getCartLevelDiscountApplications() {
        return cartLevelDiscountApplications;
    }

    @JsonProperty("cart_level_discount_applications")
    public void setCartLevelDiscountApplications(List<Object> cartLevelDiscountApplications) {
        this.cartLevelDiscountApplications = cartLevelDiscountApplications;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Cart.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("token");
        sb.append('=');
        sb.append(((this.token == null)?"<null>":this.token));
        sb.append(',');
        sb.append("note");
        sb.append('=');
        sb.append(((this.note == null)?"<null>":this.note));
        sb.append(',');
        sb.append("attributes");
        sb.append('=');
        sb.append(((this.attributes == null)?"<null>":this.attributes));
        sb.append(',');
        sb.append("originalTotalPrice");
        sb.append('=');
        sb.append(((this.originalTotalPrice == null)?"<null>":this.originalTotalPrice));
        sb.append(',');
        sb.append("totalPrice");
        sb.append('=');
        sb.append(((this.totalPrice == null)?"<null>":this.totalPrice));
        sb.append(',');
        sb.append("totalDiscount");
        sb.append('=');
        sb.append(((this.totalDiscount == null)?"<null>":this.totalDiscount));
        sb.append(',');
        sb.append("totalWeight");
        sb.append('=');
        sb.append(((this.totalWeight == null)?"<null>":this.totalWeight));
        sb.append(',');
        sb.append("itemCount");
        sb.append('=');
        sb.append(((this.itemCount == null)?"<null>":this.itemCount));
        sb.append(',');
        sb.append("items");
        sb.append('=');
        sb.append(((this.items == null)?"<null>":this.items));
        sb.append(',');
        sb.append("requiresShipping");
        sb.append('=');
        sb.append(((this.requiresShipping == null)?"<null>":this.requiresShipping));
        sb.append(',');
        sb.append("currency");
        sb.append('=');
        sb.append(((this.currency == null)?"<null>":this.currency));
        sb.append(',');
        sb.append("itemsSubtotalPrice");
        sb.append('=');
        sb.append(((this.itemsSubtotalPrice == null)?"<null>":this.itemsSubtotalPrice));
        sb.append(',');
        sb.append("cartLevelDiscountApplications");
        sb.append('=');
        sb.append(((this.cartLevelDiscountApplications == null)?"<null>":this.cartLevelDiscountApplications));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.note == null)? 0 :this.note.hashCode()));
        result = ((result* 31)+((this.itemsSubtotalPrice == null)? 0 :this.itemsSubtotalPrice.hashCode()));
        result = ((result* 31)+((this.totalPrice == null)? 0 :this.totalPrice.hashCode()));
        result = ((result* 31)+((this.token == null)? 0 :this.token.hashCode()));
        result = ((result* 31)+((this.itemCount == null)? 0 :this.itemCount.hashCode()));
        result = ((result* 31)+((this.requiresShipping == null)? 0 :this.requiresShipping.hashCode()));
        result = ((result* 31)+((this.cartLevelDiscountApplications == null)? 0 :this.cartLevelDiscountApplications.hashCode()));
        result = ((result* 31)+((this.originalTotalPrice == null)? 0 :this.originalTotalPrice.hashCode()));
        result = ((result* 31)+((this.totalWeight == null)? 0 :this.totalWeight.hashCode()));
        result = ((result* 31)+((this.totalDiscount == null)? 0 :this.totalDiscount.hashCode()));
        result = ((result* 31)+((this.attributes == null)? 0 :this.attributes.hashCode()));
        result = ((result* 31)+((this.currency == null)? 0 :this.currency.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.items == null)? 0 :this.items.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cart) == false) {
            return false;
        }
        Cart rhs = ((Cart) other);
        return (((((((((((((((this.note == rhs.note)||((this.note!= null)&&this.note.equals(rhs.note)))&&((this.itemsSubtotalPrice == rhs.itemsSubtotalPrice)||((this.itemsSubtotalPrice!= null)&&this.itemsSubtotalPrice.equals(rhs.itemsSubtotalPrice))))&&((this.totalPrice == rhs.totalPrice)||((this.totalPrice!= null)&&this.totalPrice.equals(rhs.totalPrice))))&&((this.token == rhs.token)||((this.token!= null)&&this.token.equals(rhs.token))))&&((this.itemCount == rhs.itemCount)||((this.itemCount!= null)&&this.itemCount.equals(rhs.itemCount))))&&((this.requiresShipping == rhs.requiresShipping)||((this.requiresShipping!= null)&&this.requiresShipping.equals(rhs.requiresShipping))))&&((this.cartLevelDiscountApplications == rhs.cartLevelDiscountApplications)||((this.cartLevelDiscountApplications!= null)&&this.cartLevelDiscountApplications.equals(rhs.cartLevelDiscountApplications))))&&((this.originalTotalPrice == rhs.originalTotalPrice)||((this.originalTotalPrice!= null)&&this.originalTotalPrice.equals(rhs.originalTotalPrice))))&&((this.totalWeight == rhs.totalWeight)||((this.totalWeight!= null)&&this.totalWeight.equals(rhs.totalWeight))))&&((this.totalDiscount == rhs.totalDiscount)||((this.totalDiscount!= null)&&this.totalDiscount.equals(rhs.totalDiscount))))&&((this.attributes == rhs.attributes)||((this.attributes!= null)&&this.attributes.equals(rhs.attributes))))&&((this.currency == rhs.currency)||((this.currency!= null)&&this.currency.equals(rhs.currency))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.items == rhs.items)||((this.items!= null)&&this.items.equals(rhs.items))));
    }

}


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
    "id",
    "properties",
    "quantity",
    "variant_id",
    "key",
    "title",
    "price",
    "original_price",
    "discounted_price",
    "line_price",
    "original_line_price",
    "total_discount",
    "discounts",
    "sku",
    "grams",
    "vendor",
    "taxable",
    "product_id",
    "product_has_only_default_variant",
    "gift_card",
    "final_price",
    "final_line_price",
    "url",
    "featured_image",
    "image",
    "handle",
    "requires_shipping",
    "product_type",
    "product_title",
    "product_description",
    "variant_title",
    "variant_options",
    "options_with_values",
    "line_level_discount_allocations",
    "line_level_total_discount"
})
@Generated("jsonschema2pojo")
public class Item implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("properties")
    private Properties properties;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("variant_id")
    private Long variantId;
    @JsonProperty("key")
    private String key;
    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private Long price;
    @JsonProperty("original_price")
    private Long originalPrice;
    @JsonProperty("discounted_price")
    private Long discountedPrice;
    @JsonProperty("line_price")
    private Long linePrice;
    @JsonProperty("original_line_price")
    private Long originalLinePrice;
    @JsonProperty("total_discount")
    private Long totalDiscount;
    @JsonProperty("discounts")
    private List<Object> discounts = new ArrayList<Object>();
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("grams")
    private Long grams;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("taxable")
    private Boolean taxable;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("product_has_only_default_variant")
    private Boolean productHasOnlyDefaultVariant;
    @JsonProperty("gift_card")
    private Boolean giftCard;
    @JsonProperty("final_price")
    private Long finalPrice;
    @JsonProperty("final_line_price")
    private Long finalLinePrice;
    @JsonProperty("url")
    private String url;
    @JsonProperty("featured_image")
    private FeaturedImage featuredImage;
    @JsonProperty("image")
    private String image;
    @JsonProperty("handle")
    private String handle;
    @JsonProperty("requires_shipping")
    private Boolean requiresShipping;
    @JsonProperty("product_type")
    private String productType;
    @JsonProperty("product_title")
    private String productTitle;
    @JsonProperty("product_description")
    private String productDescription;
    @JsonProperty("variant_title")
    private Object variantTitle;
    @JsonProperty("variant_options")
    private List<String> variantOptions = new ArrayList<String>();
    @JsonProperty("options_with_values")
    private List<OptionsWithValue> optionsWithValues = new ArrayList<OptionsWithValue>();
    @JsonProperty("line_level_discount_allocations")
    private List<Object> lineLevelDiscountAllocations = new ArrayList<Object>();
    @JsonProperty("line_level_total_discount")
    private Long lineLevelTotalDiscount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8085000438712768601L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Item() {
    }

    /**
     * 
     * @param variantTitle
     * @param lineLevelTotalDiscount
     * @param originalPrice
     * @param productHasOnlyDefaultVariant
     * @param finalPrice
     * @param title
     * @param discounts
     * @param variantOptions
     * @param price
     * @param vendor
     * @param optionsWithValues
     * @param id
     * @param variantId
     * @param sku
     * @param grams
     * @param key
     * @param productType
     * @param productDescription
     * @param image
     * @param quantity
     * @param taxable
     * @param productId
     * @param linePrice
     * @param handle
     * @param url
     * @param productTitle
     * @param requiresShipping
     * @param discountedPrice
     * @param lineLevelDiscountAllocations
     * @param featuredImage
     * @param originalLinePrice
     * @param giftCard
     * @param totalDiscount
     * @param finalLinePrice
     * @param properties
     */
    public Item(Long id, Properties properties, Long quantity, Long variantId, String key, String title, Long price, Long originalPrice, Long discountedPrice, Long linePrice, Long originalLinePrice, Long totalDiscount, List<Object> discounts, String sku, Long grams, String vendor, Boolean taxable, Long productId, Boolean productHasOnlyDefaultVariant, Boolean giftCard, Long finalPrice, Long finalLinePrice, String url, FeaturedImage featuredImage, String image, String handle, Boolean requiresShipping, String productType, String productTitle, String productDescription, Object variantTitle, List<String> variantOptions, List<OptionsWithValue> optionsWithValues, List<Object> lineLevelDiscountAllocations, Long lineLevelTotalDiscount) {
        super();
        this.id = id;
        this.properties = properties;
        this.quantity = quantity;
        this.variantId = variantId;
        this.key = key;
        this.title = title;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.linePrice = linePrice;
        this.originalLinePrice = originalLinePrice;
        this.totalDiscount = totalDiscount;
        this.discounts = discounts;
        this.sku = sku;
        this.grams = grams;
        this.vendor = vendor;
        this.taxable = taxable;
        this.productId = productId;
        this.productHasOnlyDefaultVariant = productHasOnlyDefaultVariant;
        this.giftCard = giftCard;
        this.finalPrice = finalPrice;
        this.finalLinePrice = finalLinePrice;
        this.url = url;
        this.featuredImage = featuredImage;
        this.image = image;
        this.handle = handle;
        this.requiresShipping = requiresShipping;
        this.productType = productType;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.variantTitle = variantTitle;
        this.variantOptions = variantOptions;
        this.optionsWithValues = optionsWithValues;
        this.lineLevelDiscountAllocations = lineLevelDiscountAllocations;
        this.lineLevelTotalDiscount = lineLevelTotalDiscount;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("properties")
    public Properties getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonProperty("quantity")
    public Long getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("variant_id")
    public Long getVariantId() {
        return variantId;
    }

    @JsonProperty("variant_id")
    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("price")
    public Long getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Long price) {
        this.price = price;
    }

    @JsonProperty("original_price")
    public Long getOriginalPrice() {
        return originalPrice;
    }

    @JsonProperty("original_price")
    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    @JsonProperty("discounted_price")
    public Long getDiscountedPrice() {
        return discountedPrice;
    }

    @JsonProperty("discounted_price")
    public void setDiscountedPrice(Long discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @JsonProperty("line_price")
    public Long getLinePrice() {
        return linePrice;
    }

    @JsonProperty("line_price")
    public void setLinePrice(Long linePrice) {
        this.linePrice = linePrice;
    }

    @JsonProperty("original_line_price")
    public Long getOriginalLinePrice() {
        return originalLinePrice;
    }

    @JsonProperty("original_line_price")
    public void setOriginalLinePrice(Long originalLinePrice) {
        this.originalLinePrice = originalLinePrice;
    }

    @JsonProperty("total_discount")
    public Long getTotalDiscount() {
        return totalDiscount;
    }

    @JsonProperty("total_discount")
    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @JsonProperty("discounts")
    public List<Object> getDiscounts() {
        return discounts;
    }

    @JsonProperty("discounts")
    public void setDiscounts(List<Object> discounts) {
        this.discounts = discounts;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("grams")
    public Long getGrams() {
        return grams;
    }

    @JsonProperty("grams")
    public void setGrams(Long grams) {
        this.grams = grams;
    }

    @JsonProperty("vendor")
    public String getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("taxable")
    public Boolean getTaxable() {
        return taxable;
    }

    @JsonProperty("taxable")
    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    @JsonProperty("product_id")
    public Long getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @JsonProperty("product_has_only_default_variant")
    public Boolean getProductHasOnlyDefaultVariant() {
        return productHasOnlyDefaultVariant;
    }

    @JsonProperty("product_has_only_default_variant")
    public void setProductHasOnlyDefaultVariant(Boolean productHasOnlyDefaultVariant) {
        this.productHasOnlyDefaultVariant = productHasOnlyDefaultVariant;
    }

    @JsonProperty("gift_card")
    public Boolean getGiftCard() {
        return giftCard;
    }

    @JsonProperty("gift_card")
    public void setGiftCard(Boolean giftCard) {
        this.giftCard = giftCard;
    }

    @JsonProperty("final_price")
    public Long getFinalPrice() {
        return finalPrice;
    }

    @JsonProperty("final_price")
    public void setFinalPrice(Long finalPrice) {
        this.finalPrice = finalPrice;
    }

    @JsonProperty("final_line_price")
    public Long getFinalLinePrice() {
        return finalLinePrice;
    }

    @JsonProperty("final_line_price")
    public void setFinalLinePrice(Long finalLinePrice) {
        this.finalLinePrice = finalLinePrice;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("featured_image")
    public FeaturedImage getFeaturedImage() {
        return featuredImage;
    }

    @JsonProperty("featured_image")
    public void setFeaturedImage(FeaturedImage featuredImage) {
        this.featuredImage = featuredImage;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("handle")
    public String getHandle() {
        return handle;
    }

    @JsonProperty("handle")
    public void setHandle(String handle) {
        this.handle = handle;
    }

    @JsonProperty("requires_shipping")
    public Boolean getRequiresShipping() {
        return requiresShipping;
    }

    @JsonProperty("requires_shipping")
    public void setRequiresShipping(Boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

    @JsonProperty("product_type")
    public String getProductType() {
        return productType;
    }

    @JsonProperty("product_type")
    public void setProductType(String productType) {
        this.productType = productType;
    }

    @JsonProperty("product_title")
    public String getProductTitle() {
        return productTitle;
    }

    @JsonProperty("product_title")
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @JsonProperty("product_description")
    public String getProductDescription() {
        return productDescription;
    }

    @JsonProperty("product_description")
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @JsonProperty("variant_title")
    public Object getVariantTitle() {
        return variantTitle;
    }

    @JsonProperty("variant_title")
    public void setVariantTitle(Object variantTitle) {
        this.variantTitle = variantTitle;
    }

    @JsonProperty("variant_options")
    public List<String> getVariantOptions() {
        return variantOptions;
    }

    @JsonProperty("variant_options")
    public void setVariantOptions(List<String> variantOptions) {
        this.variantOptions = variantOptions;
    }

    @JsonProperty("options_with_values")
    public List<OptionsWithValue> getOptionsWithValues() {
        return optionsWithValues;
    }

    @JsonProperty("options_with_values")
    public void setOptionsWithValues(List<OptionsWithValue> optionsWithValues) {
        this.optionsWithValues = optionsWithValues;
    }

    @JsonProperty("line_level_discount_allocations")
    public List<Object> getLineLevelDiscountAllocations() {
        return lineLevelDiscountAllocations;
    }

    @JsonProperty("line_level_discount_allocations")
    public void setLineLevelDiscountAllocations(List<Object> lineLevelDiscountAllocations) {
        this.lineLevelDiscountAllocations = lineLevelDiscountAllocations;
    }

    @JsonProperty("line_level_total_discount")
    public Long getLineLevelTotalDiscount() {
        return lineLevelTotalDiscount;
    }

    @JsonProperty("line_level_total_discount")
    public void setLineLevelTotalDiscount(Long lineLevelTotalDiscount) {
        this.lineLevelTotalDiscount = lineLevelTotalDiscount;
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
        sb.append(Item.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("properties");
        sb.append('=');
        sb.append(((this.properties == null)?"<null>":this.properties));
        sb.append(',');
        sb.append("quantity");
        sb.append('=');
        sb.append(((this.quantity == null)?"<null>":this.quantity));
        sb.append(',');
        sb.append("variantId");
        sb.append('=');
        sb.append(((this.variantId == null)?"<null>":this.variantId));
        sb.append(',');
        sb.append("key");
        sb.append('=');
        sb.append(((this.key == null)?"<null>":this.key));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("price");
        sb.append('=');
        sb.append(((this.price == null)?"<null>":this.price));
        sb.append(',');
        sb.append("originalPrice");
        sb.append('=');
        sb.append(((this.originalPrice == null)?"<null>":this.originalPrice));
        sb.append(',');
        sb.append("discountedPrice");
        sb.append('=');
        sb.append(((this.discountedPrice == null)?"<null>":this.discountedPrice));
        sb.append(',');
        sb.append("linePrice");
        sb.append('=');
        sb.append(((this.linePrice == null)?"<null>":this.linePrice));
        sb.append(',');
        sb.append("originalLinePrice");
        sb.append('=');
        sb.append(((this.originalLinePrice == null)?"<null>":this.originalLinePrice));
        sb.append(',');
        sb.append("totalDiscount");
        sb.append('=');
        sb.append(((this.totalDiscount == null)?"<null>":this.totalDiscount));
        sb.append(',');
        sb.append("discounts");
        sb.append('=');
        sb.append(((this.discounts == null)?"<null>":this.discounts));
        sb.append(',');
        sb.append("sku");
        sb.append('=');
        sb.append(((this.sku == null)?"<null>":this.sku));
        sb.append(',');
        sb.append("grams");
        sb.append('=');
        sb.append(((this.grams == null)?"<null>":this.grams));
        sb.append(',');
        sb.append("vendor");
        sb.append('=');
        sb.append(((this.vendor == null)?"<null>":this.vendor));
        sb.append(',');
        sb.append("taxable");
        sb.append('=');
        sb.append(((this.taxable == null)?"<null>":this.taxable));
        sb.append(',');
        sb.append("productId");
        sb.append('=');
        sb.append(((this.productId == null)?"<null>":this.productId));
        sb.append(',');
        sb.append("productHasOnlyDefaultVariant");
        sb.append('=');
        sb.append(((this.productHasOnlyDefaultVariant == null)?"<null>":this.productHasOnlyDefaultVariant));
        sb.append(',');
        sb.append("giftCard");
        sb.append('=');
        sb.append(((this.giftCard == null)?"<null>":this.giftCard));
        sb.append(',');
        sb.append("finalPrice");
        sb.append('=');
        sb.append(((this.finalPrice == null)?"<null>":this.finalPrice));
        sb.append(',');
        sb.append("finalLinePrice");
        sb.append('=');
        sb.append(((this.finalLinePrice == null)?"<null>":this.finalLinePrice));
        sb.append(',');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
        sb.append(',');
        sb.append("featuredImage");
        sb.append('=');
        sb.append(((this.featuredImage == null)?"<null>":this.featuredImage));
        sb.append(',');
        sb.append("image");
        sb.append('=');
        sb.append(((this.image == null)?"<null>":this.image));
        sb.append(',');
        sb.append("handle");
        sb.append('=');
        sb.append(((this.handle == null)?"<null>":this.handle));
        sb.append(',');
        sb.append("requiresShipping");
        sb.append('=');
        sb.append(((this.requiresShipping == null)?"<null>":this.requiresShipping));
        sb.append(',');
        sb.append("productType");
        sb.append('=');
        sb.append(((this.productType == null)?"<null>":this.productType));
        sb.append(',');
        sb.append("productTitle");
        sb.append('=');
        sb.append(((this.productTitle == null)?"<null>":this.productTitle));
        sb.append(',');
        sb.append("productDescription");
        sb.append('=');
        sb.append(((this.productDescription == null)?"<null>":this.productDescription));
        sb.append(',');
        sb.append("variantTitle");
        sb.append('=');
        sb.append(((this.variantTitle == null)?"<null>":this.variantTitle));
        sb.append(',');
        sb.append("variantOptions");
        sb.append('=');
        sb.append(((this.variantOptions == null)?"<null>":this.variantOptions));
        sb.append(',');
        sb.append("optionsWithValues");
        sb.append('=');
        sb.append(((this.optionsWithValues == null)?"<null>":this.optionsWithValues));
        sb.append(',');
        sb.append("lineLevelDiscountAllocations");
        sb.append('=');
        sb.append(((this.lineLevelDiscountAllocations == null)?"<null>":this.lineLevelDiscountAllocations));
        sb.append(',');
        sb.append("lineLevelTotalDiscount");
        sb.append('=');
        sb.append(((this.lineLevelTotalDiscount == null)?"<null>":this.lineLevelTotalDiscount));
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
        result = ((result* 31)+((this.variantTitle == null)? 0 :this.variantTitle.hashCode()));
        result = ((result* 31)+((this.lineLevelTotalDiscount == null)? 0 :this.lineLevelTotalDiscount.hashCode()));
        result = ((result* 31)+((this.originalPrice == null)? 0 :this.originalPrice.hashCode()));
        result = ((result* 31)+((this.productHasOnlyDefaultVariant == null)? 0 :this.productHasOnlyDefaultVariant.hashCode()));
        result = ((result* 31)+((this.finalPrice == null)? 0 :this.finalPrice.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.discounts == null)? 0 :this.discounts.hashCode()));
        result = ((result* 31)+((this.variantOptions == null)? 0 :this.variantOptions.hashCode()));
        result = ((result* 31)+((this.price == null)? 0 :this.price.hashCode()));
        result = ((result* 31)+((this.vendor == null)? 0 :this.vendor.hashCode()));
        result = ((result* 31)+((this.optionsWithValues == null)? 0 :this.optionsWithValues.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.variantId == null)? 0 :this.variantId.hashCode()));
        result = ((result* 31)+((this.sku == null)? 0 :this.sku.hashCode()));
        result = ((result* 31)+((this.grams == null)? 0 :this.grams.hashCode()));
        result = ((result* 31)+((this.key == null)? 0 :this.key.hashCode()));
        result = ((result* 31)+((this.productType == null)? 0 :this.productType.hashCode()));
        result = ((result* 31)+((this.productDescription == null)? 0 :this.productDescription.hashCode()));
        result = ((result* 31)+((this.image == null)? 0 :this.image.hashCode()));
        result = ((result* 31)+((this.quantity == null)? 0 :this.quantity.hashCode()));
        result = ((result* 31)+((this.taxable == null)? 0 :this.taxable.hashCode()));
        result = ((result* 31)+((this.productId == null)? 0 :this.productId.hashCode()));
        result = ((result* 31)+((this.linePrice == null)? 0 :this.linePrice.hashCode()));
        result = ((result* 31)+((this.handle == null)? 0 :this.handle.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        result = ((result* 31)+((this.productTitle == null)? 0 :this.productTitle.hashCode()));
        result = ((result* 31)+((this.requiresShipping == null)? 0 :this.requiresShipping.hashCode()));
        result = ((result* 31)+((this.discountedPrice == null)? 0 :this.discountedPrice.hashCode()));
        result = ((result* 31)+((this.lineLevelDiscountAllocations == null)? 0 :this.lineLevelDiscountAllocations.hashCode()));
        result = ((result* 31)+((this.featuredImage == null)? 0 :this.featuredImage.hashCode()));
        result = ((result* 31)+((this.originalLinePrice == null)? 0 :this.originalLinePrice.hashCode()));
        result = ((result* 31)+((this.giftCard == null)? 0 :this.giftCard.hashCode()));
        result = ((result* 31)+((this.totalDiscount == null)? 0 :this.totalDiscount.hashCode()));
        result = ((result* 31)+((this.finalLinePrice == null)? 0 :this.finalLinePrice.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.properties == null)? 0 :this.properties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Item) == false) {
            return false;
        }
        Item rhs = ((Item) other);
        return (((((((((((((((((((((((((((((((((((((this.variantTitle == rhs.variantTitle)||((this.variantTitle!= null)&&this.variantTitle.equals(rhs.variantTitle)))&&((this.lineLevelTotalDiscount == rhs.lineLevelTotalDiscount)||((this.lineLevelTotalDiscount!= null)&&this.lineLevelTotalDiscount.equals(rhs.lineLevelTotalDiscount))))&&((this.originalPrice == rhs.originalPrice)||((this.originalPrice!= null)&&this.originalPrice.equals(rhs.originalPrice))))&&((this.productHasOnlyDefaultVariant == rhs.productHasOnlyDefaultVariant)||((this.productHasOnlyDefaultVariant!= null)&&this.productHasOnlyDefaultVariant.equals(rhs.productHasOnlyDefaultVariant))))&&((this.finalPrice == rhs.finalPrice)||((this.finalPrice!= null)&&this.finalPrice.equals(rhs.finalPrice))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.discounts == rhs.discounts)||((this.discounts!= null)&&this.discounts.equals(rhs.discounts))))&&((this.variantOptions == rhs.variantOptions)||((this.variantOptions!= null)&&this.variantOptions.equals(rhs.variantOptions))))&&((this.price == rhs.price)||((this.price!= null)&&this.price.equals(rhs.price))))&&((this.vendor == rhs.vendor)||((this.vendor!= null)&&this.vendor.equals(rhs.vendor))))&&((this.optionsWithValues == rhs.optionsWithValues)||((this.optionsWithValues!= null)&&this.optionsWithValues.equals(rhs.optionsWithValues))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.variantId == rhs.variantId)||((this.variantId!= null)&&this.variantId.equals(rhs.variantId))))&&((this.sku == rhs.sku)||((this.sku!= null)&&this.sku.equals(rhs.sku))))&&((this.grams == rhs.grams)||((this.grams!= null)&&this.grams.equals(rhs.grams))))&&((this.key == rhs.key)||((this.key!= null)&&this.key.equals(rhs.key))))&&((this.productType == rhs.productType)||((this.productType!= null)&&this.productType.equals(rhs.productType))))&&((this.productDescription == rhs.productDescription)||((this.productDescription!= null)&&this.productDescription.equals(rhs.productDescription))))&&((this.image == rhs.image)||((this.image!= null)&&this.image.equals(rhs.image))))&&((this.quantity == rhs.quantity)||((this.quantity!= null)&&this.quantity.equals(rhs.quantity))))&&((this.taxable == rhs.taxable)||((this.taxable!= null)&&this.taxable.equals(rhs.taxable))))&&((this.productId == rhs.productId)||((this.productId!= null)&&this.productId.equals(rhs.productId))))&&((this.linePrice == rhs.linePrice)||((this.linePrice!= null)&&this.linePrice.equals(rhs.linePrice))))&&((this.handle == rhs.handle)||((this.handle!= null)&&this.handle.equals(rhs.handle))))&&((this.url == rhs.url)||((this.url!= null)&&this.url.equals(rhs.url))))&&((this.productTitle == rhs.productTitle)||((this.productTitle!= null)&&this.productTitle.equals(rhs.productTitle))))&&((this.requiresShipping == rhs.requiresShipping)||((this.requiresShipping!= null)&&this.requiresShipping.equals(rhs.requiresShipping))))&&((this.discountedPrice == rhs.discountedPrice)||((this.discountedPrice!= null)&&this.discountedPrice.equals(rhs.discountedPrice))))&&((this.lineLevelDiscountAllocations == rhs.lineLevelDiscountAllocations)||((this.lineLevelDiscountAllocations!= null)&&this.lineLevelDiscountAllocations.equals(rhs.lineLevelDiscountAllocations))))&&((this.featuredImage == rhs.featuredImage)||((this.featuredImage!= null)&&this.featuredImage.equals(rhs.featuredImage))))&&((this.originalLinePrice == rhs.originalLinePrice)||((this.originalLinePrice!= null)&&this.originalLinePrice.equals(rhs.originalLinePrice))))&&((this.giftCard == rhs.giftCard)||((this.giftCard!= null)&&this.giftCard.equals(rhs.giftCard))))&&((this.totalDiscount == rhs.totalDiscount)||((this.totalDiscount!= null)&&this.totalDiscount.equals(rhs.totalDiscount))))&&((this.finalLinePrice == rhs.finalLinePrice)||((this.finalLinePrice!= null)&&this.finalLinePrice.equals(rhs.finalLinePrice))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.properties == rhs.properties)||((this.properties!= null)&&this.properties.equals(rhs.properties))));
    }

}

import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import { faPlusCircle, faMinusCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { formatPrice } from './Bundle.util';
import { Add, Remove } from 'react-ionicons';


function SelectedProduct(props) {
  const {
    product,
    variant,
    showAddProduct,
    onVariantChange,
    showVariant,
    onProductAdd,
    showRemoveProduct,
    onProductRemove,
    showQuantity,
    quantity,
    selectedProducts,
    selectedSellingPlan,
    bundleData,
    subscriptionBundleSettingsEntity
  } = props;
  const [selectedVariant, setSelectedVariant] = useState(null);

  const selectVariant = variantId => {
    let tempVarient = _.find(product.variants, o => o.id == variantId);
    setSelectedVariant(tempVarient);
    if (onVariantChange) {
      onVariantChange(product, tempVarient);
    }
  };
  const isSelected = () => {
    return _.findIndex(selectedProducts, o => o.product.id == product.id) !== -1;
  };

  const getDiscountedPrice = (price) => {
    if (bundleData.bundle.discount && (Number(bundleData.bundle.minProductCount) > 0)) {
        return Math.round(((100 - Number(bundleData.bundle.discount)) /  100) * price);
    } else {
      return price;
    }
  }


  useEffect(() => {
    if (variant) {
      selectVariant(variant.id);
    } else {
      selectVariant(product.variants[0].id);
    }
  }, []);
  return (
    <div className="appstleBundle_productCard appstleBundle_productCard--selectedProduct">
      <div className="appstle_product_top">
        <div className="appstleBundle_product_image appstleBundle_product_image--selectedProduct">
          <img src={`https:${product?.featured_image}`} />
        </div>
      </div>
      <div className="appstleBundle_product_bottom  appstleBundle_product_bottom--selectedProduct">
        <div className="appstleBundle_product_title appstleBundle_product_title--selectedProduct"
        style={{color: subscriptionBundleSettingsEntity?.productTitleFontColor || "#fcba03"}}>{product.title}{`${(product?.variants?.length > 1) ? (` - ${variant?.title}`) : ""}`}</div>
        <div className="appstleBundle_product_price appstleBundle_product_price--selectedProduct" style={{marginTop: "5px"}}>
        {formatPrice(getDiscountedPrice((selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
              return sellingPlan?.selling_plan_id == selectedSellingPlan
            }).pop())?.per_delivery_price))}
            </div>
        {showVariant && (product?.variants?.length > 1) && (
          <div className="appstleBundle_product_variant">
            <select
              class="custom-select"
              value={selectedVariant?.id}
              onChange={e => {
                selectVariant(e.target.value);
              }}
            >
              {product.variants?.map((variant, idx) => {
                return (
                  <option key={idx} value={variant.id}>
                    {variant.title}
                  </option>
                );
              })}
            </select>
          </div>
        )}
        <div className="d-flex align-center appstle_product-Button_wrapper" style={{backgroundColor: (subscriptionBundleSettingsEntity?.backgroundColor || "#fffbe7"), marginTop: "7px"}}>
          <button className="appstle_product-button-action"
          style={{backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03"}}
            onClick={() => {
              if (onProductRemove) {
                onProductRemove(variant);
              }
            }}
          >
            <Remove width="20px" height="20px" stroke="4" color={subscriptionBundleSettingsEntity?.buttonColor || "#3a3a3a"} />
          </button>
          {showQuantity && (
            <div class="ml-1 mr-1 appstleBundle_product_quantity appstleBundle_product_quantity--selectedProduct appstle_product-qty"
            style={{color: subscriptionBundleSettingsEntity?.productTitleFontColor || "#fcba03"}}>
              {quantity}
            </div>
          )}
          <button className="appstle_product-button-action"
          style={{backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03"}}
              onClick={() => {
                if (onProductAdd) {
                  onProductAdd(product);
                }
              }}
            >
            <Add width="20px" height="20px" stroke="4" color={subscriptionBundleSettingsEntity?.buttonColor || "#3a3a3a"} />
          </button>

        </div>
      </div>
    </div>
  );
}

export default SelectedProduct;

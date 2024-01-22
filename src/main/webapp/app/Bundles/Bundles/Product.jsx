import React, { useEffect, useState, useRef } from 'react';
import _ from 'lodash';
import { formatPrice } from './Bundle.util'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { Add, Remove } from 'react-ionicons';


function Product(props) {
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
    subscriptionBundleSettingsEntity,
    variants,
    selectedSellingPlan,
    bundleData
  } = props;
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [sellingPlanVariants, setSellingPlanVariants] = useState([])
  const refContainer = useRef(null);

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

  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };

  useEffect(() => {
    let variantWithSelectedSellingPlan = []
    product?.variants?.forEach((variant, idx) => {
      let isVariantAdded = false;
      variant?.selling_plan_allocations?.forEach(item => {
        if ((props?.sellingPlanIds?.indexOf(item?.selling_plan_id) !== -1)) {
          if (!isVariantAdded && variant?.available){
            variantWithSelectedSellingPlan.push(variant);
            isVariantAdded = true;
          }
        }
      })
    })
    selectVariant(variantWithSelectedSellingPlan[0]?.id);
    setSellingPlanVariants([...variantWithSelectedSellingPlan])
  }, []);
  return (
    <div className="appstleBundle_productCard">
      <div className="appstle_product_top">
        <div className="appstleBundle_product_image">
        {
          product?.featured_image ?
          <img src={`https:${product?.featured_image}`} />
          : <img src={require("./BlankImage.jpg")} />
        }
        </div>
      </div>
      <div className="appstleBundle_product_bottom">
        {
          selectedVariant && <div className="d-flex">
          <div class="appstleBundle_product_price">
            <span class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName}`}>
              {
                  subscriptionBundleSettingsEntity?.productPriceFormatField
                  ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(`{{price}}`,
                    formatPrice(getDiscountedPrice((selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
                    return sellingPlan?.selling_plan_id == selectedSellingPlan
                    }).pop())?.per_delivery_price)))
                  :
                    formatPrice(getDiscountedPrice((selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
                      return sellingPlan?.selling_plan_id == selectedSellingPlan
                    }).pop())?.per_delivery_price))
              }
            </span>
          </div>
          {(bundleData?.bundle?.discount && (Number(bundleData?.bundle?.discount) > 0) && (Number(bundleData.bundle.minProductCount) > 0)) ? <div class="appstleBundle_product_price appstle-compare-at-price">
            <span class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName}`}>
            {
                  subscriptionBundleSettingsEntity?.productPriceFormatField
                  ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(`{{price}}`,
                    formatPrice((selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
                    return sellingPlan?.selling_plan_id == selectedSellingPlan
                    }).pop())?.per_delivery_price))
                  :
                    formatPrice((selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
                      return sellingPlan?.selling_plan_id == selectedSellingPlan
                    }).pop())?.per_delivery_price)
              }
              </span>
          </div> : ""}
        </div>
        }
        <a className="appstleBundle_product_title"
        style={{color: subscriptionBundleSettingsEntity?.productTitleFontColor || "#3a3a3a"}} href={`https://${Shopify.shop}/${product?.url}`} target='_blank'>{product.title}</a>

        {
          product?.description && <p className="appstleBundle_product_description">
             {
               extractTextFromHtml(product?.description).length > 0 &&
               extractTextFromHtml(product?.description)?.substring(0, subscriptionBundleSettingsEntity?.descriptionLength ?? 100) + "..."
             }
          </p>
        }
        <div className="mt-auto">
        {showVariant && (product?.variants?.length > 1) && (
          <div className="appstleBundle_product_variant">
            <select
              class="custom-select"
              value={selectedVariant?.id}
              onChange={e => {
                selectVariant(e.target.value);
              }}
            >
              {sellingPlanVariants?.map((variant, idx) => {
                return (
                  <option key={idx} value={variant?.id}>
                    {variant?.title}
                  </option>
                );
              })}
            </select>
          </div>
        )}

        {/* {showQuantity && (
          <div className="appstle_product_quantity">
            <p>Quantity : {quantity}</p>
          </div>
        )} */}

        {showAddProduct && (
          selectedVariant ?
          <div className="d-flex align-center appstle_product-Button_wrapper" style={{backgroundColor: subscriptionBundleSettingsEntity?.backgroundColor || "#fffbe7"}}>
            <button className="appstle_product-button-action"
            style={{backgroundColor: subscriptionBundleSettingsEntity?.buttonBackgroundColor || "#fcba03"}}
              onClick={() => {
                if (onProductRemove) {
                  onProductRemove(selectedVariant);
                }
              }}
            >
              <Remove width="20px" height="20px" stroke="4" color={subscriptionBundleSettingsEntity?.buttonColor|| "#3a3a3a"} />
            </button>
            <div class="ml-1 mr-1 appstleBundle_product_quantity appstleBundle_product_quantity--selectedProduct appstle_product-qty"
              style={{color: subscriptionBundleSettingsEntity?.productTitleFontColor || "#fcba03"}}>
              {(_.find(selectedProducts, o => o.variant?.id == selectedVariant?.id))?.quantity || '0'}
            </div>
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
        :
        <div className="d-flex align-center appstle_product-Button_wrapper justify-content-center">
          <button disabled
                    className="w-100 btn" style={{
                      fontSize: '13px',
                      height: "36px",
                      margin: '7px',
                      borderRadius: "4px",
                      backgroundColor: "#3a3a3a",
                      borderColor: "#3a3a3a"}}>
                  { `${subscriptionBundleSettingsEntity?.variantNotAvailable || "Not Available"}`}
              </button>
              </div>
        )}
        </div>
        {showRemoveProduct && (
          <div className="appstle_product_button">
            <button
              onClick={() => {
                if (onProductRemove) {
                  onProductRemove(variant);
                }
              }}
            >
              Remove
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default Product;

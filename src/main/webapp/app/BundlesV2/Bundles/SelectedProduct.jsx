import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import { formatPrice, getCurrentConvertedCurrencyPrice } from './Bundle.util';
import { MinusIcon, PlusIcon } from '@heroicons/react/24/outline';

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
    subscriptionBundleSettingsEntity,
    selectedSellingPlanId,
    getDiscountedPrice,
    onProductDelete,
    eligibleDiscount,
    selectedProductData,
    getDiscountedPerDeliveryPrice
  } = props;
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [productBasePrice, setProductBasePrice] = useState(0);
  const [discountedPrice, setDiscountedPrice] = useState(0);
  
  

  const selectVariant = variantId => {
    let tempVarient = _.find(product.variants, o => o.id === variantId);
    setSelectedVariant(tempVarient);
    if (onVariantChange) {
      onVariantChange(product, tempVarient);
    }
  };
  const isSelected = () => {
    return _.findIndex(selectedProducts, o => o.product.id === product.id) !== -1;
  };

  useEffect(() => {
    if (variant) {
      selectVariant(variant.id);
    } else {
      selectVariant(product.variants[0].id);
    }
  }, [selectedProducts]);

  useEffect(() => {
    setProductBasePrice(selectedProductData?.variant?.price || 0);
    setDiscountedPrice(getDiscountedPrice(
      selectedProductData?.variant?.selling_plan_allocations?.filter(sellingPlan => {
          return sellingPlan?.selling_plan_id == Number(selectedProductData.selling_plan);
        }).pop()?.per_delivery_price, false, selectedProductData?.selectedSellingPlan, selectedProductData?.eligibleDiscount
    ))
  },[selectedProductData, product]);
  
  return (
    <li key={product.id} className="as-flex as-py-6">
      <div className="as-h-24 as-w-24 as-flex-shrink-0 as-overflow-hidden as-rounded-md as-border as-border-gray-200">
        <img
          src={selectedVariant?.featured_image?.src ? selectedVariant?.featured_image?.src : (product?.featured_image ? product?.featured_image : (product?.media && product?.media.length) ? product?.media[0]?.preview_image?.src : require('./BlankImage.jpg'))}
          className="as-h-full as-w-full as-object-cover as-object-center"
        />
      </div>

      <div className="as-ml-4 as-flex as-flex-1 as-flex-col">
        <div>
          <div className="as-flex as-justify-between as-text-xs as-font-bold as-text-gray-900">
            <h3>
              <a href={product.href}> {product.title}</a>
            </h3>
            <div className='as-ml-4 as-flex as-text-right'>
              {subscriptionBundleSettingsEntity?.showCompareAtPrice && productBasePrice > discountedPrice && (
                <p className="as-line-through as-mr-2 as-text-sm as-text-gray-500 transcy-money as-text-sm as-bab-product-base-total">
                  {subscriptionBundleSettingsEntity?.productPriceFormatField
                    ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                        `{{price}}`,
                        formatPrice(getCurrentConvertedCurrencyPrice(productBasePrice)),
                      )
                    : formatPrice(getCurrentConvertedCurrencyPrice(productBasePrice))}
                </p>
              )}
              <p className="transcy-money as-text-sm  as-text-sm">
                {subscriptionBundleSettingsEntity?.productPriceFormatField
                  ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                      `{{price}}`,
                      formatPrice(getCurrentConvertedCurrencyPrice(
                        getDiscountedPrice(
                          selectedProductData?.variant?.selling_plan_allocations?.filter(sellingPlan => {
                              return sellingPlan?.selling_plan_id == Number(selectedProductData.selling_plan);
                            }).pop()?.per_delivery_price, false, selectedProductData?.selectedSellingPlan, selectedProductData?.eligibleDiscount
                        )
                      ))
                    )
                  : formatPrice(getCurrentConvertedCurrencyPrice(
                    getDiscountedPrice(
                      selectedProductData?.variant?.selling_plan_allocations?.filter(sellingPlan => {
                          return sellingPlan?.selling_plan_id == Number(selectedProductData.selling_plan);
                        }).pop()?.per_delivery_price, false, selectedProductData?.selectedSellingPlan, selectedProductData?.eligibleDiscount
                    )
                  ))}
              </p>
              {(selectedProductData?.buyPrice !== selectedProductData?.perDeliveryBuyPrice) && <p className="transcy-money as-text-xs as-font-normal as-text-gray-500">
                {subscriptionBundleSettingsEntity?.productPriceFormatField
                  ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                    `{{price}}`,
                    formatPrice(getCurrentConvertedCurrencyPrice(getDiscountedPerDeliveryPrice(selectedProductData?.variant?.selling_plan_allocations?.filter(sellingPlan => {
                      return sellingPlan?.selling_plan_id == Number(selectedProductData.selling_plan);
                    }).pop()?.per_delivery_price, selectedProductData?.eligibleDiscount)))
                  )
                  : formatPrice(getCurrentConvertedCurrencyPrice(getDiscountedPerDeliveryPrice(selectedProductData?.variant?.selling_plan_allocations?.filter(sellingPlan => {
                    return sellingPlan?.selling_plan_id == Number(selectedProductData.selling_plan);
                  }).pop()?.per_delivery_price, selectedProductData?.eligibleDiscount)))}{subscriptionBundleSettingsEntity?.perDeliveryLabelTextV2 || '/delivery'}
              </p>}
            </div>
            
          </div>
          <p className="as-mt-1 as-text-xs as-text-gray-500">{`${product?.variants?.length > 1 ? `${variant?.title}` : ''}`} </p>
        </div>
        <div className="as-flex as-justify-between as-mt-auto">
          <div className="as-border-gray-200 as-flex as-flex-row as-h-6 as-w-full as-rounded-lg as-relative">
            <button
              data-action="decrement"
              class="as-flex as-justify-center as-items-center as-bg-gray-300  as-w-8 as-rounded-l as-cursor-pointer as-outline-none"
              onClick={() => {
                if (onProductRemove) {
                  onProductRemove(variant, product, -1, selectedProductData);
                }
              }}
            >
              <MinusIcon className="as-h-3 as-w-3" />
            </button>
            <div
              className="as-border-gray-300 as-border-3 as-w-8 as-justify-center as-outline-none focus:as-outline-none as-text-center  as-font-semibold as-text-xs hover:as-text-black focus:as-text-black  md:as-text-basecursor-default as-flex as-items-center as-text-gray-700"
              name="custom-input-number"
            >
              {quantity}
            </div>
            <button
              data-action="increment"
              class="as-bg-gray-300  as-w-8 as-rounded-r as-cursor-pointer as-flex as-justify-center as-items-center"
              onClick={() => {
                if (onProductAdd) {
                  onProductAdd(variant, product, 1, selectedProductData);
                }
              }}
            >
              <PlusIcon className="as-h-3 as-w-3" />
            </button>
          </div>
          <div className="as-flex">
            <button
              type="button"
              className="as-text-xs as-font-medium as-text-indigo-600 hover:as-text-indigo-500 as-shrink-0"
              onClick={() => {
                onProductDelete(variant);
              }}
              title={subscriptionBundleSettingsEntity?.removeItem || 'Remove'}
            >
              🗑️
            </button>
          </div>
        </div>
      </div>
    </li>
  );
}

export default SelectedProduct;

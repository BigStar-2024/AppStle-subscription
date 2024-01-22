import React, {useState} from 'react';
import {Input} from 'reactstrap';
import {formatPrice} from 'app/BundlesV2/Bundles/Bundle.util';
import {useMainContext} from 'app/AppstleMenu/context';
import _ from 'lodash';
import DeliverySelect from 'app/AppstleMenu/AppstleMenu/Components/deliverySelect';

export default function ProductDetail(props) {
  const {
    product,
    subscriptionBundleSettingsEntity,
    isQuantityInputInValid,
    setIsModalOpen,
    selectedVariant,
    setSelectedVariant,
    setSelectedOptions,
    selectedOptions,
    selectedSellingPlan,
    setSelectedSellingPlan,
    sellingPlans,
    handleVariantChange,
    selectedSellingPlanDetail
  } = props;
  const {selectedFilterMenu, addToCart, appstleMenuLabels} = useMainContext();
  const [quantity, setQuantity] = useState(1);

  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };

  const quantityInputChangeHandler = value => {
    setQuantity(value);
  };

  const processOptions = options => {
    let list = [];
    options.map((option, index) => {
      list.push({name: option.name, value: option.value});
    });
    return list;
  };

  return (
    <>
      <div className="md:as-grid as-grid-cols-12 as-gap-4 as-py-2 as-mb-3">
        <div className="as-col-span-5">
          <img src={selectedVariant?.image?.src ? selectedVariant?.image?.src : (product?.featuredImage?.url ? product?.featuredImage?.url : require('./BlankImage.jpg'))} alt="" className="as-w-full as-max-w-xs"/>
        </div>
        <div className="as-col-span-7 as-flex as-flex-col">
          <p className={`as-text-2xl as-text-gray-900 as-mb-2 as-text-left`}>{product?.title}</p>
          {(product?.description || product?.body_html) && (
            <p className="as-text-sm as-text-gray-500 as-mb-2 as-text-left">
              {extractTextFromHtml(product?.description || product?.body_html).length > 0 &&
                extractTextFromHtml(product?.description || product?.body_html)?.substring(
                  0,
                  subscriptionBundleSettingsEntity?.descriptionLength ?? 200
                ) + '...'}
            </p>
          )}
          <div className="as-flex">
          <div className="as-mr-1">
            {((selectedVariant.compareAtPrice && parseInt(selectedVariant.compareAtPrice?.amount) && (parseFloat(selectedVariant?.price?.amount) !== selectedVariant?.compareAtPrice)) || (selectedSellingPlanDetail && selectedSellingPlanDetail?.node?.priceAdjustments[0]?.perDeliveryPrice?.amount !== selectedSellingPlanDetail?.node?.priceAdjustments[0]?.compareAtPrice?.amount))  ? (
              <>
                <p
                  className={`as-line-through as-mr-2 as-font-medium as-text-gray-500 appstle-product-compare-at-price`}>
                  {' '}
                  {formatPrice((parseInt(selectedSellingPlanDetail?.node?.priceAdjustments[0]?.compareAtPrice?.amount) ? selectedSellingPlanDetail?.node?.priceAdjustments[0]?.compareAtPrice?.amount : selectedVariant?.compareAtPrice?.amount) * 100 || 0)}
                </p>
                <p className={`as-mr-2 as-font-medium as-text-gray-900 appstle-product-price`}>
                  {formatPrice((selectedSellingPlanDetail ? selectedSellingPlanDetail?.node?.priceAdjustments[0]?.perDeliveryPrice?.amount : selectedVariant?.price?.amount) * 100 || 0)}
                </p>
              </>
            ) : (
              <span className={`as-font-medium as-text-gray-900 appstle-product-price`}>
                    {formatPrice((selectedSellingPlanDetail ? selectedSellingPlanDetail?.node?.priceAdjustments[0]?.perDeliveryPrice?.amount : selectedVariant?.price?.amount) * 100 || 0)}
                </span>
            )}
              </div>
          </div>
          <div className="">
            <div className="as-mb-3 as-mt-5">
              <label
                className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left as-mb-2"><>{appstleMenuLabels?.editQuantity || 'Edit Quantity'}</>
              </label>
              <Input
                value={quantity || 1}
                type="number"
                onInput={event => quantityInputChangeHandler(event.target.value)}
                // onBlur={event => quantityInputBlurHandler(event.target.value)}
                onChange={event => quantityInputChangeHandler(event.target.value)}
                className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full sm:as-text-sm as-border-gray-300 as-rounded-md disabled:as-bg-gray-100 disabled:as-border disabled:as-border-gray-300 disabled:as-text-gray-900 disabled:as-cursor-not-allowed"
              />
              {isQuantityInputInValid &&
                <p class="as-mt-2 as-text-sm as-text-red-600">{'Please enter valid number'}</p>}
            </div>
            <div className={`as-grid ${product?.options.length > 1 ? 'as-grid-cols-2' : ''} as-gap-5`}>
              {' '}
              {product?.options.map((item, optionIndex) => {
                return (
                  <>
                    {product?.variants?.length > 1 ? (
                      <div className={`as-mb-3 as-mt-auto ${product?.options.length === 1 ? 'as-w-full' : ''}`}>
                        <label
                          className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">{item?.name}</label>
                        <div className="d-flex" style={{alignItems: 'center'}}>
                          <Input
                            name={item.name}
                            value={selectedVariant?.selectedOptions[optionIndex]?.value || ''}
                            type="select"
                            onChange={event => {
                              handleVariantChange(event);
                            }}
                            className="as-mt-2 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                          >
                            {item?.values.map((variant, variantIndex) => {
                              return (
                                <option key={variantIndex} value={variant.value}>
                                  {variant.value}
                                </option>
                              );
                            })}
                          </Input>
                        </div>
                      </div>
                    ) : (
                      ''
                    )}
                  </>
                );
              })}
            </div>
            <div className={'as-grid as-grid-cols-2 as-gap-5'}>
              {selectedFilterMenu?.menuType === 'SUBSCRIBE' ? (
                <DeliverySelect
                  sellingPlans={sellingPlans}
                  selectedSellingPlan={selectedSellingPlan}
                  setSelectedSellingPlan={setSelectedSellingPlan}
                />
              ) : (
                ''
              )}
              {product?.variants?.edges?.length > 1 && (
                  <div className="as-mb-3 as-px-4">
                    <Input
                      value={selectedVariant?.id}
                      type="select"
                      onChange={event => {
                        handleVariantChange(event);
                      }}
                      className="as-mt-2 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                    >
                      {product?.variants?.edges?.map((variant, index) => {
                        return (
                          <option key={index} value={variant?.node?.id}>
                            {variant?.node?.title}
                          </option>
                        );
                      })}
                    </Input>
                  </div>
                )}
            </div>
          </div>
          <div className={`as-mt-3 as-grid as-gap-2 as-modal-footer as-modal-cta-wrapper`}>
            <button
              disabled={!selectedVariant?.availableForSale}
              onClick={event => {
                addToCart(selectedVariant, quantity, selectedSellingPlan);
                setIsModalOpen(false);
              }}
              type="submit"
              className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
            >
              {selectedVariant?.availableForSale
                ? selectedFilterMenu?.menuType === 'SUBSCRIBE'
                  ? <>{appstleMenuLabels?.subscribe || 'Subscribe'}</>
                  : <>{appstleMenuLabels?.addToCart || 'Add to cart'}</>
                : <>{appstleMenuLabels?.notAvailable || 'Not Available'}</>}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

import React, { useEffect, useState } from 'react';
import { Input } from 'reactstrap';
import { formatPrice } from 'app/shared/util/customer-utils';

export default function ProductDetail(props) {
  const {
    productData,
    subscriptionBundleSettingsEntity,
    bundleData,
    getDiscountedPrice,
    setSelectedItemsToAdd,
    disableQuantity,
    quantity,
    setQuantity,
    isQuantityInputInValid,
    setIsQuantityInputInValid,
    onProductAdd,
    setIsModalOpen,
    selectedSellingPlanId,
    setCartOpen
  } = props;

  const [selectedVariant, setSelectedVariant] = useState({});
  const [selectedOptions, setSelectedOptions] = useState([]);

  let [quantityInputBlurred, setQuantityInputBlurred] = useState(false);

  useEffect(() => {
    let selectedVarObj = productData?.variants?.[0];
    setSelectedVariant(selectedVarObj);

    if (subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com') {
      setSelectedOptions(productData?.variants[0].options);
    }
  }, [productData]);

  const handleVariantChange = event => {
    if (subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com') {
      const newSelectedOptions = [...selectedOptions];
      newSelectedOptions[event.target.name] = event.target.value;
      setSelectedOptions(newSelectedOptions);
      let selectedVarObj = productData?.variants.find(variant => {
        if (variant.options.toString() === newSelectedOptions.toString()) {
          return variant;
        }
      });
      setSelectedVariant({ ...selectedVarObj });
    } else {
      let selectedVarObj = productData?.variants.find(variant => variant.id === parseInt(event.target.value));
      setSelectedVariant({ ...selectedVarObj });
    }
  };

  const validateNumber = value => {
    const type = typeof value;
    if (type === 'undefined') {
      return undefined;
    } else if (!value.trim()) {
      return `field value is required`;
    } else if (isNaN(value)) {
      return `field value should be a number`;
    } else if (parseInt(value) < 1) {
      return `field value should be greater or equal to 0.`;
    } else {
      return undefined;
    }
  };

  const quantityInputChangeHandler = value => {
    setQuantity(value);
    // if (!quantityInputBlurred) return;
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const quantityInputBlurHandler = value => {
    setQuantity(value);
    setQuantityInputBlurred(true);
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };
  return (
    <>
      <div className="md:as-grid as-grid-cols-12 as-gap-4 as-py-2 as-mb-3">
        <div className="as-col-span-5">
          <img src={selectedVariant?.featured_image?.src ? selectedVariant?.featured_image?.src : (productData?.featured_image ? productData?.featured_image : require('./BlankImage.jpg'))} alt="" className="as-w-full as-max-w-xs" />
        </div>
        <div className="as-col-span-7 as-flex as-flex-col">
          <p className={`as-text-2xl as-text-gray-900 as-mb-2 as-text-left`}>{productData?.title}</p>
          {/* {(productData?.description || productData?.body_html) && (
            <p className="as-text-sm as-text-gray-500 as-mb-2 as-text-left" dangerouslySetInnerHTML={{__html: extractTextFromHtml(productData?.description || productData?.body_html).length > 0 &&
              extractTextFromHtml(productData?.description || productData?.body_html)?.substring(
                0,
                subscriptionBundleSettingsEntity?.descriptionLength ?? 200
              ) + ((extractTextFromHtml(productData?.description || productData?.body_html).length > (subscriptionBundleSettingsEntity?.descriptionLength || 200)) ? '...' : '')}}>
            </p>
          )} */}

          {(productData?.description || productData?.body_html) && subscriptionBundleSettingsEntity?.descriptionLength > 0 && (
            <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3">
              {(productData?.description || productData?.body_html).length > 0 ? (
                <span dangerouslySetInnerHTML={{__html: (productData?.description || productData?.body_html)}} />
              ) : null}
            </p>
          )}
          <div className="as-flex as-items-baseline as-mb-2">
            {selectedVariant && (
              <>
                <div class="">
                  <span class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-text-xl as-text-gray-500 as-mt-1`}>
                    {subscriptionBundleSettingsEntity?.productPriceFormatField
                      ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                          `{{price}}`,
                          formatPrice(
                            getDiscountedPrice(
                              selectedVariant?.selling_plan_allocations
                                ?.filter(sellingPlan => {
                                  return sellingPlan?.selling_plan_id == selectedSellingPlanId;
                                })
                                .pop()?.per_delivery_price
                            )
                          )
                        )
                      : formatPrice(
                          getDiscountedPrice(
                            selectedVariant?.selling_plan_allocations
                              ?.filter(sellingPlan => {
                                return sellingPlan?.selling_plan_id == selectedSellingPlanId;
                              })
                              .pop()?.per_delivery_price
                          )
                        )}
                  </span>
                </div>
                {bundleData?.bundle?.discount &&
                Number(bundleData?.bundle?.discount) > 0 &&
                Number(bundleData.bundle.minProductCount) > 0 ? (
                  <div>
                    <span
                      class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-text-sm  as-mt-1 as-line-through as-ml-1 as-text-red-700`}
                    >
                      {subscriptionBundleSettingsEntity?.productPriceFormatField
                        ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                            `{{price}}`,
                            formatPrice(
                              selectedVariant?.selling_plan_allocations
                                ?.filter(sellingPlan => {
                                  return sellingPlan?.selling_plan_id == selectedSellingPlanId;
                                })
                                .pop()?.per_delivery_price
                            )
                          )
                        : formatPrice(
                            selectedVariant?.selling_plan_allocations
                              ?.filter(sellingPlan => {
                                return sellingPlan?.selling_plan_id == selectedSellingPlanId;
                              })
                              .pop()?.per_delivery_price
                          )}
                    </span>
                  </div>
                ) : (
                  ''
                )}
              </>
            )}
            {/* <p className="as-text-md as-text-gray-600 as-text-left">{formatPrice(quantity * selectedVariant?.appstleSellingPrice)} <span className="as-text-xs as-text-gray-500">({formatPrice(selectedVariant?.appstleSellingPrice)}/quantity)</span></p> */}
          </div>
          <div className="">
            <div className="as-mb-3 as-mt-5">
              <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left as-mb-2">
                {subscriptionBundleSettingsEntity?.editQuantity || 'Edit Quantity'}
              </label>
              <Input
                value={quantity}
                type="number"
                onInput={event => quantityInputChangeHandler(event.target.value)}
                onBlur={event => quantityInputBlurHandler(event.target.value)}
                onChange={event => quantityInputChangeHandler(event.target.value)}
                className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full sm:as-text-sm as-border-gray-300 as-rounded-md disabled:as-bg-gray-100 disabled:as-border disabled:as-border-gray-300 disabled:as-text-gray-900 disabled:as-cursor-not-allowed"
              />
              {isQuantityInputInValid && <p class="as-mt-2 as-text-sm as-text-red-600">{'Please enter valid number'}</p>}
            </div>

            {subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com' ? (
              <>
                {' '}
                {productData?.options.map((item, optionIndex) => {
                  const options = _.reject(_.uniqBy(productData?.variants || [], `option${optionIndex + 1}`), [
                    `option${optionIndex + 1}`,
                    null
                  ]);
                  const titleName = `option${optionIndex + 1}`;
                  return (
                    <>
                      {options.length > 1 && (
                        <div className="as-mb-3">
                          <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">
                            {item?.name || item || `Option${optionIndex}`}
                          </label>
                          <div className="d-flex" style={{ alignItems: 'center' }}>
                            <Input
                              name={optionIndex}
                              value={selectedVariant[`${titleName}`]}
                              type="select"
                              onChange={event => {
                                handleVariantChange(event);
                              }}
                              className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                            >
                              {options.map((variant, index) => {
                                return (
                                  <option key={variant?.id} value={variant[`${titleName}`]}>
                                    {variant[`${titleName}`]}
                                  </option>
                                );
                              })}
                            </Input>
                          </div>
                        </div>
                      )}
                    </>
                  );
                })}
              </>
            ) : (
              <>
                {productData?.variants?.length > 1 && (
                  <div className="as-mb-3">
                    <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">{'Variant'}</label>
                    <div className="d-flex" style={{ alignItems: 'center' }}>
                      <Input
                        value={selectedVariant?.id}
                        type="select"
                        onChange={event => {
                          handleVariantChange(event);
                        }}
                        className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                      >
                        {productData?.variants.map((variant, index) => {
                          return (
                            <option key={variant?.id} value={variant?.id}>
                              {variant?.title}
                            </option>
                          );
                        })}
                      </Input>
                    </div>
                  </div>
                )}
              </>
            )}
          </div>
          <div className={`as-mt-3 as-grid as-gap-2 as-modal-footer as-modal-cta-wrapper`}>
            <button
              disabled={!selectedVariant?.available}
              onClick={event => {
                onProductAdd(selectedVariant, productData, quantity);
                setIsModalOpen(false);
                let interval = setInterval(function() {
                  var htmlElement = document.querySelector('html');
                  var cssObj = getComputedStyle(htmlElement, null);
                  if (cssObj?.overflow !== 'hidden') {
                    setCartOpen(true);
                    clearInterval(interval);
                  }
                }, 30);
              }}
              type="submit"
              className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
            >
              {selectedVariant?.available
                ? subscriptionBundleSettingsEntity?.addButtonText
                : `${subscriptionBundleSettingsEntity?.variantNotAvailable || 'Not Available'}`}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

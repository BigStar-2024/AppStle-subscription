import React, { useEffect, useRef, useState } from 'react';
import _ from 'lodash';
import { formatPrice, getCurrentConvertedCurrencyPrice } from './Bundle.util';
import TailwindModal from './TailwindModal';
import ProductDetail from './ProductDetails';
import { MinusIcon, PlusIcon } from '@heroicons/react/24/outline';
import { Input } from 'reactstrap';
import ProductCard from './ProductCard';

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
    selectedProducts,
    subscriptionBundleSettingsEntity,
    variants,
    selectedSellingPlan,
    bundleData,
    setCartOpen,
    selectedSellingPlanId,
    sellingPlanIds,
    isCartOpened,
    minProduct,
    maxProduct,
    eligibleDiscount
  } = props;
  const [dataLoading, setDataLoading] = useState(true);
  const [sellingPlanVariants, setSellingPlanVariants] = useState([]);
  const [quantity, setQuantity] = useState(1);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const [selectedProductItem, setSelectedProductItem] = useState(null);
  const [isMaxProductLimitReached, setIsMaxProductLimitReached] = useState(false);

  const [selectedVariant, setSelectedVariant] = useState({});
  const [selectedOptions, setSelectedOptions] = useState([]);

  const [compareAtPrice, setCompareAtPrice] = useState(null);
  const [filterVariantsByAllocationsId, setFilterVariantsByAllocationsId]= useState([])
  const [isReadMore, setIsReadMore]= useState(false);
  const [isNewProductAdd, setIsNewProductAdd]= useState(false);

  const [discountPercentage, setDiscountPercentage] = useState(null)

  const refContainer = useRef(null);

  useEffect(() => {
    let comparePrice = 0;
    if (subscriptionBundleSettingsEntity?.showCompareAtPrice) {
      comparePrice = compareAtPrice
    } else {
      comparePrice = selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
        return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
      }).pop()?.per_delivery_price
    }

    let actualPrice = getDiscountedPrice(
      selectedVariant?.selling_plan_allocations?.filter(sellingPlan => {
          return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
        }).pop()?.per_delivery_price
    )

    if (comparePrice > actualPrice) {
      let disc = Math.round(100 - (actualPrice/comparePrice)*100).toFixed(2);
      if (Number.isInteger(parseFloat(disc))) {
        setDiscountPercentage(parseInt(disc))
      } else {
        setDiscountPercentage(disc)
      }

    } else {
      setDiscountPercentage(null)
    }
  }, [selectedVariant, compareAtPrice, eligibleDiscount, subscriptionBundleSettingsEntity])

  useEffect(() => {
    setIsMaxProductLimitReached(_.sumBy(selectedProducts, 'quantity') >= maxProduct);
    if (subscriptionBundleSettingsEntity?.enableOpeningSidebar && isNewProductAdd && _.sumBy(selectedProducts, 'quantity') >= maxProduct) {
      setCartOpen(true);
    }
  }, [selectedProducts]);

  const selectVariant = variantId => {
    let tempVarient = _.find(product.variants, o => o.id == variantId);
    if (variantId) {
      setSelectedVariant(tempVarient);
      if (subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com') {
        setSelectedOptions(tempVarient?.options || []);
      }
    }
    if (onVariantChange) {
      onVariantChange(product, tempVarient);
    }
  };

  const isSelected = () => {
    return _.findIndex(selectedProducts, o => o.product.id == product.id) !== -1;
  };

  const getDiscountedPrice = price => {
    if (eligibleDiscount && eligibleDiscount?.discount && Number(bundleData.bundle.minProductCount) > 0) {
      return Math.round(((100 - Number(eligibleDiscount?.discount)) / 100) * price);
    } else {
      return price;
    }
  };

  const extractTextFromHtml = text => {
    let parser = new DOMParser();
    let htmlDoc = parser?.parseFromString(text, 'text/html');
    return htmlDoc?.querySelector('body')?.innerText;
  };

  useEffect(() => {
    let variantWithSelectedSellingPlan = [];
    if (product && product?.variants?.length && sellingPlanIds?.length && dataLoading) {
      setDataLoading(false);
      product?.variants?.forEach((variant, idx) => {
        let isVariantAdded = false;
        variant?.selling_plan_allocations?.forEach(item => {
          if (sellingPlanIds?.indexOf(item?.selling_plan_id) !== -1) {
            if (!isVariantAdded && variant?.available) {
              variantWithSelectedSellingPlan.push(variant);
              isVariantAdded = true;
            }
          }
        });
      });
      selectVariant(variantWithSelectedSellingPlan[0]?.id);
      setSellingPlanVariants([...variantWithSelectedSellingPlan]);
    }
  }, [sellingPlanIds]);

  useEffect(
    function() {
      if (selectedProducts?.length) {
        let item = null;
        item = selectedProducts
          ?.filter(item => {
            return item?.variant?.id === selectedVariant?.id;
          })
          .pop();
        setSelectedProductItem({ ...item });
      } else {
        setSelectedProductItem(null);
      }
      calculateCompareAtPrice();
    },
    [selectedVariant, selectedProducts]
  );

  useEffect(() => {
    let filteredVariants = product?.variants?.filter(item =>
      item.selling_plan_allocations?.some(subITem => subITem.selling_plan_id === Number(selectedSellingPlanId))
    );
    filteredVariants = filteredVariants?.filter(item => item?.available);
    setSelectedVariant(filteredVariants[0]);
      if(filteredVariants?.length){
        setFilterVariantsByAllocationsId(filteredVariants)
      }
     // setSelectedItemsToAdd([{id: selectedVarObj?.id, uniqueId: selectedVarObj?.uniqueId, title: selectedVarObj?.title, imageSrc: product?.imageSrc, prdHandleName : product?.prdHandleName}])
  }, [product]);

  const calculateCompareAtPrice = () => {
    if(selectedVariant){
      let tempCompareAtPrice = selectedVariant.compare_at_price ? selectedVariant.compare_at_price :
      selectedVariant?.selling_plan_allocations
      ?.filter(sellingPlan => {
        return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
      })
      .pop()?.compare_at_price;

      let preDeliveryPrice = selectedVariant?.selling_plan_allocations
      ?.filter(sellingPlan => {
        return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
      })
      .pop()?.per_delivery_price;

      if(tempCompareAtPrice !== preDeliveryPrice){
        setCompareAtPrice(tempCompareAtPrice)
      }else{
        setCompareAtPrice(0)
      }
    }
  }

  const handleVariantChange = event => {
    if (subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com') {
      const newSelectedOptions = [...selectedOptions];
      newSelectedOptions[event.target.name] = event.target.value;
      setSelectedOptions(newSelectedOptions);
      let selectedVarObj = product?.variants.find(variant => {
        if (variant.options.toString() === newSelectedOptions.toString()) {
          return variant;
        }
      });
      setSelectedVariant({ ...selectedVarObj });
    } else {
      let selectedVarObj = product?.variants.find(variant => variant.id === parseInt(event.target.value));
      setSelectedVariant({ ...selectedVarObj });
    }
  };

  let slideIndex = 1;
  function plusSlides(n, id) {
    showSlides((slideIndex += n), id);
  }
  function showSlides(n, id) {
    let i;
    let slides = document.getElementsByClassName(`mySlides__${id}`);
    if (n > slides.length) {
      slideIndex = 1;
    }
    if (n < 1) {
      slideIndex = slides.length;
    }
    for (i = 0; i < slides.length; i++) {
      slides[i].style.display = 'none';
    }
    slides[slideIndex - 1].style.display = 'block';
  }

  return (
    <>
      <div className="as-group as-relative as-cursor-pointer as-h-full as-flex as-flex-col as-shadow-md as-rounded-lg as-overflow-hidden hover:as-shadow-lg as-transition-all as-bg-white">
        <div
          className="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
          // onClick={() => setIsModalOpen(true)}
        >

          {(discountPercentage && subscriptionBundleSettingsEntity?.saveDiscountText) && <>

            <div className='as-product-discount-badge as-absolute as-p-1 as-bg-red as-text-white as-z-10 as-text-sm as-left-0 as-top-0 as-rounded-br-md as-bg-red-500'>{(subscriptionBundleSettingsEntity?.saveDiscountText)?.replace("{{discount}}", discountPercentage)}</div>
          </>}
          {subscriptionBundleSettingsEntity?.enableRedirectToProductPage ? (
            <a href={`https://${Shopify.shop}/${product?.url}`} target="_blank">
              <ProductCard product={product} selectedVariant={selectedVariant} filterVariantsByAllocationsId={filterVariantsByAllocationsId} />
            </a>
          ) : (
            <ProductCard product={product} selectedVariant={selectedVariant} filterVariantsByAllocationsId={filterVariantsByAllocationsId} />
          )}
          {(product?.images && product?.images.length > 1 && filterVariantsByAllocationsId?.length <= 1) ? (
            <>
              <a className="appstle-slider-prev" onClick={() => plusSlides(-1, product?.id)}>
                ❮
              </a>
              <a className="appstle-slider-next" onClick={() => plusSlides(1, product?.id)}>
                ❯
              </a>
            </>
          ) : null}
        </div>
        <>
          {
            <>
            <div className="as-mt-4 as-mb-6">
                <h3 className="as-text-gray-900 as-text-sm lg:as-text-lg  as-text-center as-px-3 lg:as-px-6">
                  {subscriptionBundleSettingsEntity?.enableRedirectToProductPage ? (
                    <a href={`https://${Shopify.shop}/${product?.url}`} target="_blank">
                      {product.title}
                    </a>
                  ) : (
                    <>{product.title}</>
                  )}
                </h3>
                {((!subscriptionBundleSettingsEntity?.disableProductDescription) && (product?.description || product?.body_html) && (subscriptionBundleSettingsEntity?.descriptionLength > 0)) && (
                  <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3" style={{"padding": "5px 15px", textAlign: "center"}}>
                    {extractTextFromHtml(product?.description || product?.body_html).length > 0 ?
                       <>
                      <span>
                        {extractTextFromHtml(product?.description || product?.body_html)?.substring(0, isReadMore ? extractTextFromHtml(product?.description || product?.body_html).length : subscriptionBundleSettingsEntity?.descriptionLength ?? 200 )}</span>

                        {
                          ((extractTextFromHtml(product?.description || product?.body_html).length > subscriptionBundleSettingsEntity?.descriptionLength ?? 200) && (subscriptionBundleSettingsEntity?.readLessTextV2 && subscriptionBundleSettingsEntity?.readMoreTextV2)) ?
                         <> {' '}
                          <button className="as-w-full as-border as-border-transparent  as-text-sm sm:as-text-base as-font-medium as-text-black  focus:as-outline-none  sm:as-w-auto sm:as-text-sm ReadMoreButton"
                          onClick={() => setIsReadMore(!isReadMore)}
                          ><span className="as-readMore-elipses">{ isReadMore ? '' :  '...' }</span>{ isReadMore ? subscriptionBundleSettingsEntity?.readLessTextV2 || 'Read Less' :  subscriptionBundleSettingsEntity?.readMoreTextV2 || 'Read More' } </button>
                          </> : <span className="as-readMore-elipses">{((extractTextFromHtml(product?.description || product?.body_html).length > subscriptionBundleSettingsEntity?.descriptionLength ?? 200) && '...')}</span>
                        }

                        </>
                    : ""}
                  </p>
                )}
                {(product?.vendor && subscriptionBundleSettingsEntity?.enableDisplayProductVendor) &&
                  <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-vendor" style={{ textAlign: "center" , fontWeight: "bold"}}>
                    <span> {product?.vendor} </span>
                  </p>
                }
                {(product?.type && subscriptionBundleSettingsEntity?.enableDisplayProductType) &&
                  <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3 appstle-custom-type" style={{ textAlign: "center", fontWeight: "500"}}>
                    <span> {product?.type} </span>
                  </p>
                }
              </div>
            <div className="as-mt-auto as-mb-6">

              {subscriptionBundleSettingsEntity.shop === 'cali-grill-meal-prep.myshopify.com' &&
              bundleData?.bundle?.productViewStyle === 'QUICK_ADD' ? (
                <>
                  {product?.options.map((item, optionIndex) => {
                    const options = _.reject(_.uniqBy(product?.variants || [], `option${optionIndex + 1}`), [
                      `option${optionIndex + 1}`,
                      null
                    ]);
                    const titleName = `option${optionIndex + 1}`;
                    return (
                      <>
                        {options.length > 1 && (
                          <div className="as-mb-3 as-mt-auto  as-px-3 lg:as-px-6">
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
              ) : subscriptionBundleSettingsEntity.shop !== 'cali-grill-meal-prep.myshopify.com' ? (
                <>
                  {filterVariantsByAllocationsId?.length > 1 && (
                    <div className="as-mb-3 as-mt-auto  as-px-3 lg:as-px-6">
                      {/* <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">{'Variant'}</label> */}
                      <div className="d-flex" style={{ alignItems: 'center' }}>
                        <Input
                          value={selectedVariant?.id}
                          type="select"
                          onChange={event => {
                            handleVariantChange(event);
                          }}
                          className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                        >
                          {filterVariantsByAllocationsId.map((variant, index) => {
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
              ) : (
                ''
              )}

              {bundleData?.bundle?.productViewStyle === 'QUICK_ADD' ? (
                <div className="as-flex as-justify-center as-items-center as-mt-auto  as-px-3 lg:as-px-6 as-flex-col">
                  {selectedVariant?.available ? (
                    <p class="as-flex as-items-baseline as-mb-3">
                      {subscriptionBundleSettingsEntity?.showCompareAtPrice && compareAtPrice && Number(compareAtPrice) > 0 ? (
                        <span
                          class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-line-through as-mr-2 as-text-sm as-text-gray-500 transcy-money as-text-sm as-bundle-product-compare-price`}
                        >
                          {subscriptionBundleSettingsEntity?.productPriceFormatField
                            ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(`{{price}}`, formatPrice(getCurrentConvertedCurrencyPrice(compareAtPrice)))
                            : formatPrice(getCurrentConvertedCurrencyPrice(compareAtPrice))}
                        </span>
                      ) : (
                        ''
                      )}
                      {eligibleDiscount &&
                      eligibleDiscount?.discount &&
                      eligibleDiscount?.discount > 0 &&
                      Number(bundleData.bundle.minProductCount) > 0 ? (
                        <span
                          class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-line-through as-mr-2 as-text-sm as-text-gray-500 transcy-money as-bundle-product-discount-price`}
                        >
                          {subscriptionBundleSettingsEntity?.productPriceFormatField
                            ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                                `{{price}}`,
                                formatPrice(
                                  getCurrentConvertedCurrencyPrice(
                                  selectedVariant?.selling_plan_allocations
                                    ?.filter(sellingPlan => {
                                      return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
                                    })
                                    .pop()?.per_delivery_price
                                )
                                )
                              )
                            : formatPrice(
                              getCurrentConvertedCurrencyPrice(selectedVariant?.selling_plan_allocations
                                  ?.filter(sellingPlan => {
                                    return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
                                  })
                                  .pop()?.per_delivery_price
                              )
                              )}
                        </span>
                      ) : (
                        ''
                      )}
                      <span
                        className={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-font-medium as-text-gray-900 transcy-money as-text-sm as-bundle-product-final-price`}
                      >
                        {subscriptionBundleSettingsEntity?.productPriceFormatField
                          ? subscriptionBundleSettingsEntity.productPriceFormatField.replace(
                              `{{price}}`,
                              formatPrice(
                                getCurrentConvertedCurrencyPrice(getDiscountedPrice(
                                  selectedVariant?.selling_plan_allocations
                                    ?.filter(sellingPlan => {
                                      return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
                                    })
                                    .pop()?.per_delivery_price
                                )
                                )
                              )
                            )
                          : formatPrice(
                            getCurrentConvertedCurrencyPrice(getDiscountedPrice(
                                selectedVariant?.selling_plan_allocations
                                  ?.filter(sellingPlan => {
                                    return sellingPlan?.selling_plan_id == parseInt(selectedSellingPlanId);
                                  })
                                  .pop()?.per_delivery_price
                              )
                            )
                            )}
                      </span>
                      
                        {(subscriptionBundleSettingsEntity?.enableShowProductBasePrice && subscriptionBundleSettingsEntity?.basePriceFormatFieldV2 && selectedVariant?.unit_price) && 
                          <span className={`as-ml-2 as-font-medium as-text-gray-500 transcy-money fs-11 as-unit-price-format`}>
                            {subscriptionBundleSettingsEntity.basePriceFormatFieldV2.replace(`{{unitPrice}}`, formatPrice(getCurrentConvertedCurrencyPrice(getDiscountedPrice(selectedVariant?.unit_price))))
                              .replace(`{{quantityUnit}}`, selectedVariant?.unit_price_measurement?.reference_unit ? selectedVariant?.unit_price_measurement?.reference_unit : '')
                            }
                          </span> 
                        }
                    </p>
                  ) : (
                    <></>
                  )}
                  {selectedVariant?.available ? (
                    <div className="as-border-gray-200 as-flex as-flex-row as-rounded-lg as-relative as-items-center">
                      {selectedProductItem?.quantity ? (
                        <>
                          <button
                            data-action="decrement"
                            class="as-inline-flex as-justify-center as-items-center as-w-9 as-h-9 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                            onClick={() => {
                              if (onProductRemove) {
                                onProductRemove(selectedVariant, product, -1);
                              }
                            }}
                          >
                            <MinusIcon className="as-h-3 as-w-3" />
                          </button>
                          <div
                            className="as-border-gray-300 as-border-3 as-w-8 as-justify-center as-outline-none focus:as-outline-none as-text-center  as-font-semibold as-text-xs hover:as-text-black focus:as-text-black  md:as-text-basecursor-default as-flex as-items-center as-text-gray-700"
                            name="custom-input-number"
                          >
                            {selectedProductItem?.quantity || 0}
                          </div>
                          <button
                            data-action="increment"
                            disabled={isMaxProductLimitReached}
                            class="as-inline-flex as-justify-center as-items-center  as-w-9 as-h-9 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                            onClick={() => {
                              if (onProductAdd) {
                                onProductAdd(selectedVariant, product, 1);
                              }
                            }}
                          >
                            <PlusIcon className="as-h-3 as-w-3" />
                          </button>
                        </>
                      ) : (
                        <button
                          disabled={isMaxProductLimitReached}
                          data-action="increment"
                          class="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-sm lg:as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                          onClick={() => {
                            if (onProductAdd) {
                              onProductAdd(selectedVariant, product, 1);
                              if (subscriptionBundleSettingsEntity?.enableOpeningSidebar) {
                                setIsNewProductAdd(true);
                              } else {
                                setCartOpen(true);
                              }
                            }
                          }}
                        >
                          {subscriptionBundleSettingsEntity?.addButtonText || 'Add'}
                        </button>
                      )}
                    </div>
                  ) : (
                    <div className={`as-mt-3 as-grid as-gap-2 as-modal-footer as-modal-cta-wrapper`}>
                      <button
                        disabled={!selectedVariant?.available}
                        className="as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-sm lg:as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
                      >
                        {subscriptionBundleSettingsEntity?.variantNotAvailable || 'Not Available'}
                      </button>
                    </div>
                  )}
                  {/* <button
                type="button"
                onClick={() => setIsModalOpen(true)}
                class="as-mt-auto as-w-full as-items-center as-px-2 as-py-3 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-gray-800 as-bg-gray-100 hover:as-bg-gray-200 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-gray-300 as-button as-button--primary as-product-card-button"
              >
                {subscriptionBundleSettingsEntity?.viewProduct || 'View Details'}
              </button> */}
                </div>
              ) : (
                ''
              )}
            </div>
            </>
          }
        </>

        {subscriptionBundleSettingsEntity?.enableProductDetailButton && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="as-mt-auto as-px-3 lg:as-px-6 as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-py-2 as-bg-indigo-600 as-text-sm lg:as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary"
          >
            {subscriptionBundleSettingsEntity?.viewProduct || 'View Details'}
          </button>
        )}

      </div>
      <TailwindModal
        open={isModalOpen}
        setOpen={setIsModalOpen}
        actionMethod={() => onProductAdd(product)}
        actionButtonText={'Add Product'}
        modalTitle={subscriptionBundleSettingsEntity?.productDetails || 'Product Detail'}
      >
        <ProductDetail
          productData={product}
          setIsModalOpen={setIsModalOpen}
          subscriptionBundleSettingsEntity={subscriptionBundleSettingsEntity}
          getDiscountedPrice={getDiscountedPrice}
          bundleData={bundleData}
          quantity={quantity}
          setQuantity={setQuantity}
          onProductAdd={onProductAdd}
          setCartOpen={setCartOpen}
          selectedSellingPlanId={selectedSellingPlanId}
        />
      </TailwindModal>
    </>
  );
}

export default Product;

import React, { useEffect, useRef, useState } from 'react';
import _ from 'lodash';
import { formatPrice } from './Bundle.util';
import TailwindModal from './TailwindModal';
import ProductDetail from './ProductDetails';
import { MinusIcon, PlusIcon } from "@heroicons/react/24/outline";
import { Input } from 'reactstrap';

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
    maxProduct
  } = props;
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [dataLoading, setDataLoading] = useState(true);
  const [sellingPlanVariants, setSellingPlanVariants] = useState([]);
  const [quantity, setQuantity] = useState(1);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const [selectedProductItem, setSelectedProductItem] = useState(null);
  const [isMaxProductLimitReached, setIsMaxProductLimitReached] = useState(false);

  const refContainer = useRef(null);

  useEffect(() => {
    setIsMaxProductLimitReached( _.sumBy(selectedProducts, 'quantity') >= maxProduct)
  }, [selectedProducts])

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

  const getDiscountedPrice = price => {
    if (bundleData.bundle.discount && Number(bundleData.bundle.minProductCount) > 0) {
      return Math.round(((100 - Number(bundleData.bundle.discount)) / 100) * price);
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

  useEffect(function() {
    if (selectedProducts?.length) {
      let item = null;
      item = (selectedProducts?.filter(item => {
        return item?.variant?.id === selectedVariant?.id
      })).pop();
      setSelectedProductItem({...item})

    } else {
      setSelectedProductItem(null)
    }
  }, [selectedVariant, selectedProducts])

  useEffect(() => {
    let selectedVarObj = product?.variants?.[0];
    setSelectedVariant(selectedVarObj);
    // setSelectedItemsToAdd([{id: selectedVarObj?.id, uniqueId: selectedVarObj?.uniqueId, title: selectedVarObj?.title, imageSrc: product?.imageSrc, prdHandleName : product?.prdHandleName}])
  }, [product]);

  const handleVariantChange = event => {
    let selectedVarObj = product?.variants.find(variant => variant.id === parseInt(event.target.value));
    setSelectedVariant({ ...selectedVarObj });
    //  setSelectedItemsToAdd([{id: selectedVarObj?.id, uniqueId: selectedVarObj?.uniqueId, title: selectedVarObj?.title, imageSrc: product?.imageSrc, prdHandleName : product?.prdHandleName}])
  };


  return (
    <>
      <div class="as-group as-relative as-cursor-pointer md:as-grid as-grid-cols-2 as-overflow-hidden as-transition-all as-items-center as-gap-4 as-pb-2" 
        style={{gridTemplateColumns: "minmax(0px, 384px) minmax(0px, 800px)"}}
      >
        <div class="as-w-full as-h-[251px] as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
         onClick={() => setIsModalOpen(true)}
        >
           <div
            style={{
              backgroundImage: `url(${product?.featured_image ? product?.featured_image : require('./BlankImage.jpg')})`,
              backgroundPosition: "center",
              backgroundRepeat: "no-repeat",
              backgroundSize: "cover",
              filter: "blur(10px)",
              position: "absolute",
              width: "100%",
              top: 0,
              left: '0',
              zIndex: "0",
              height: "100%"
            }}
          />
          <img
            src={product?.featured_image ? product?.featured_image : require('./BlankImage.jpg')}
            alt=""
            class="as-h-full as-relative as-z-10"
          />
        </div>
        <div class="as-mt-3 lg:as-mt-0 as-flex as-flex-col as-items-center md:as-items-start">
          <h3 class="as-text-gray-900 as-text-xl as-mb-3 as-text-center md:as-text-left">
            <a href={`https://${Shopify.shop}/${product?.url}`} target="_blank">
              {/* <span aria-hidden="true" class="as-absolute as-inset-0"></span> */}
              {product.title}
            </a>
          </h3>
          {/* <p class="as-mt-1 as-text-sm as-text-gray-500">Black</p> */}
          <p class="as-flex as-items-baseline as-mb-3">
              {bundleData?.bundle?.discount && Number(bundleData?.bundle?.discount) > 0 && Number(bundleData.bundle.minProductCount) > 0 ? (
                  <span
                    class={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-line-through as-mr-2 as-text-sm as-text-gray-500`}
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
                ) : (
                  ''
                )}
                <span className={`${subscriptionBundleSettingsEntity?.currencySwitcherClassName} as-font-medium as-text-gray-900`}>
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
                
              </p>
          {(product?.description || product?.body_html) && (
            <p className="as-text-sm as-text-gray-500 as-text-left as-mb-3">
              {extractTextFromHtml(product?.description || product?.body_html).length > 0 &&
                extractTextFromHtml(product?.description || product?.body_html)}
            </p>
          )}
           
        {
          <div className="as-mt-3">
            {product?.variants?.length > 1 && (
              <div className="as-mb-3 as-mt-auto">
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
                    {product?.variants.map((variant, index) => {
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
        
            <div className="">
              
              {true ? <div className="as-border-gray-200 as-flex as-flex-row as-h-6 as-rounded-lg as-relative as-items-center">
                <button
                  data-action="decrement"
                  class="as-inline-flex as-justify-center as-items-center as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                  onClick={() => {
                    if (onProductRemove) {
                      onProductRemove(selectedVariant, product, -1);
                    }
                  }}
                >
                  <MinusIcon className="as-h-3 as-w-3"/>
                </button>
                <div className="as-border-gray-300 as-border-3 as-w-8 as-justify-center as-outline-none focus:as-outline-none as-text-center  as-font-semibold as-text-xs hover:as-text-black focus:as-text-black  md:as-text-basecursor-default as-flex as-items-center as-text-gray-700" name="custom-input-number">{selectedProductItem?.quantity || 0}</div>
                <button
                  data-action="increment"
                  disabled={isMaxProductLimitReached}
                  class="as-inline-flex as-justify-center as-items-center  as-w-6 as-h-6 as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white"
                  onClick={() => {
                    if (onProductAdd) {
                      onProductAdd(selectedVariant, product, 1);
                    }
                  }}
                >
                    <PlusIcon className="as-h-3 as-w-3"/>
                </button>
              </div> : <button
                  data-action="increment"
                  class="as-ml-auto as-px-4 as-py-1 as-inline-flex as-justify-center as-items-center as-border as-border-transparent as-rounded-full as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                  onClick={() => {
                    if (onProductAdd) {
                      onProductAdd(selectedVariant, product, 1);
                      if (!isCartOpened) {
                        setCartOpen(true);
                      }
                    }
                  }}
                >
                    {subscriptionBundleSettingsEntity?.addButtonText || "Add"}
                </button>}
              {/* <button
                type="button"
                onClick={() => setIsModalOpen(true)}
                class="as-mt-auto as-w-full as-items-center as-px-2 as-py-3 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-gray-800 as-bg-gray-100 hover:as-bg-gray-200 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-gray-300 as-button as-button--primary as-product-card-button"
              >
                {subscriptionBundleSettingsEntity?.viewProduct || 'View Details'}
              </button> */}
            </div>
          </div>
          }
        </div>
   
       
        
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

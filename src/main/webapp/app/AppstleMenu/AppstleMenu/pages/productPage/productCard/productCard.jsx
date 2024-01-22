import React, {useEffect, useState} from 'react';
import _ from 'lodash';
import {Input} from 'reactstrap';
import {useMainContext} from 'app/AppstleMenu/context';
import DeliverySelect from 'app/AppstleMenu/AppstleMenu/Components/deliverySelect';
import {formatPrice} from 'app/BundlesV2/Bundles/Bundle.util';
import ProductDetail from './ProductDetails';
import TailwindModal from 'app/BundlesV2/Bundles/TailwindModal';

const ProductCard = ({product}) => {
  const {selectedFilterMenu, addToCart, adding, productId, appstleMenuLabels, appstleMenuSettings} = useMainContext();
  const [selectedVariant, setSelectedVariant] = useState(product?.variants?.edges?.[0]?.node || null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const [sellingPlans, setSellingPlans] = useState([]);
  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [selectedSellingPlanDetail, setSelectedSellingPlanDetail] = useState(null);
  useEffect(() => {
    if (product?.sellingPlanGroups?.edges?.length > 0) {
      const appstlePlanGroup = product?.sellingPlanGroups?.edges?.find(item => item?.node?.appName === 'appstle');
      if (appstlePlanGroup !== undefined || appstlePlanGroup != null) {
        setSellingPlans(appstlePlanGroup?.node?.sellingPlans?.edges);
        setSelectedSellingPlan(appstlePlanGroup?.node?.sellingPlans?.edges[0]?.node);
      }
    }
  }, [product]);

  useEffect(() => {
    if (selectedVariant && selectedSellingPlan) {
      setSelectedSellingPlanDetail((selectedVariant?.sellingPlanAllocations?.edges.filter(sellingPlan => selectedSellingPlan?.id === sellingPlan?.node?.sellingPlan?.id)).pop())
    }
  }, [selectedVariant, selectedSellingPlan])

  const processOptions = options => {
    let list = [];
    options.map((option, index) => {
      list.push({name: option.name, value: option.value});
    });
    return list;
  };

  const handleSelectVariant = variantId => {
    let detectedVariant = _.find(product?.variants?.edges, o => o.node.id.includes(variantId));
    if (variantId) {
      setSelectedVariant(detectedVariant?.node);
      setSelectedOptions(processOptions(detectedVariant?.node?.selectedOptions || []));
    }
  };

  useEffect(() => {
    let variantWithSelectedSellingPlan = [];
    if (product && product?.variants?.edges?.length) {
      handleSelectVariant(product?.variants?.edges?.[0]?.node?.id);
    }
  }, [product]);

  const handleProductAddToCart = () => {
    addToCart(selectedVariant, 1, selectedSellingPlan);
  };

  const handleVariantChange = event => {
    const variant = product?.variants?.edges?.find(value => value?.node?.id === event.target.value);
    if (variant) {
      setSelectedVariant(variant?.node);
    }
  };

  const isDetailedView = appstleMenuSettings?.productViewStyle === "VIEW_DETAILS";

  return (
    <React.Fragment>
      <div
        className="as-group as-cursor-pointer as-h-full as-flex as-flex-col as-shadow-md as-rounded-lg as-overflow-hidden hover:as-shadow-lg as-transition-all as-bg-white">
        <div
          className="as-w-full as-bg-gray-200 as-rounded-md as-overflow-hidden as-relative as-flex as-justify-center"
          onClick={() => setIsModalOpen(true)}
        >
          <div
            style={{
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat',
              backgroundSize: 'cover',
              filter: 'blur(10px)',
              position: 'absolute',
              width: '100%',
              top: 0,
              left: '0',
              zIndex: '0',
              height: '100%',
              display: 'block'
            }}
          />
          <img
            src={selectedVariant?.image?.src ? selectedVariant?.image?.src : (product?.featuredImage?.url ? product?.featuredImage?.url : require('./BlankImage.jpg'))}
            alt=""
            className="as-h-full as-relative"
          />
        </div>
        <div className="as-mt-4 as-mb-2 as-text-center">
          <h3 className="as-text-gray-900 as-text-lg as-px-4">
            <a href={`https://${window?.Shopify?.shop}/products/${product?.handle}`} target="_blank">
              {product?.title}
            </a>
          </h3>
        </div>
        <div className="as-mt-3 as-mb-2" style={{display: 'contents'}}>
          {
            isDetailedView ? <></> : <>
              <>
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
              </>

              {selectedFilterMenu?.menuType === 'SUBSCRIBE' ? (
                <div className="as-mb-3 as-px-4">
                  <DeliverySelect
                    sellingPlans={sellingPlans}
                    selectedSellingPlan={selectedSellingPlan}
                    setSelectedSellingPlan={setSelectedSellingPlan}
                  />
                </div>
              ) : (
                ''
              )}
            </>
          }
          <div
            className="xl:as-flex lg:as-flex md:as-flex sm:as-column xs:as-column as-justify-between as-items-center as-mb-3 as-mt-auto as-px-4 as-gap-5">
            <div
              className={`as-flex sm:as-px-4 md:as-px-4 lg:as-px-4 xl:as-px-4 2xl:as-px-4 as-w-full as-justify-center as-items-center ${isDetailedView ? "as-flex-col" : "as-flex-col xs:as-flex-col sm:as-flex-row md:as-flex-row lg:as-flex-row xl:as-flex-row 2xl:as-flex-row"}`}>
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
              <div className={`as-ml-1 ${isDetailedView && "as-w-full"}`}>
                {
                  isDetailedView ? <>
                    <button
                      className="as-mt-1 as-mb-1 as-text-center as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm as-button as-button--primary"
                      onClick={() => setIsModalOpen(true)}
                      style={{width: "100%"}}
                    >
                      View Details
                    </button>
                  </> : <>
                    {selectedVariant?.availableForSale ? (
                      <button
                        disabled={productId === selectedVariant.id && adding}
                        data-action="increment"
                        className="as-mt-1 as-mb-1 as-text-center as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary  appstle-add-to-cart"
                        onClick={() => {
                          handleProductAddToCart();
                        }}
                      >
                        {productId === selectedVariant.id && adding
                          ? <>{appstleMenuLabels?.adding || 'adding'}</>
                          : selectedFilterMenu?.menuType === 'SUBSCRIBE'
                            ? <>{appstleMenuLabels?.subscribe || 'Subscribe'
                            }</> : <>{appstleMenuLabels?.addToCart || 'Add to cart'}</>}
                      </button>
                    ) : (
                      <div className={`as-mt-1 as-grid as-gap-2 as-modal-footer as-modal-cta-wrapper`}>
                        <button
                          disabled={!selectedVariant?.available}
                          className="as-text-center as-w-full as-inline-flex as-items-center as-justify-center as-rounded-md as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-indigo-600 as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-button_modal-primary  appstle-not-available"
                        >
                          <>{appstleMenuLabels?.notAvailable || 'Not Available'}</>
                        </button>
                      </div>
                    )}
                  </>
                }
              </div>
            </div>
          </div>
        </div>
      </div>

      <TailwindModal
        open={isModalOpen}
        setOpen={setIsModalOpen}
        actionMethod={() => {
        }}
        actionButtonText={'Add Product'}
        modalTitle={<>{appstleMenuLabels?.productDetails || 'Product Details'}</>}
      >
        <ProductDetail
          product={product}
          setIsModalOpen={setIsModalOpen}
          selectedVariant={selectedVariant}
          setSelectedVariant={setSelectedVariant}
          setSelectedOptions={setSelectedOptions}
          selectedOptions={selectedOptions}
          selectedSellingPlan={selectedSellingPlan}
          setSelectedSellingPlan={setSelectedSellingPlan}
          sellingPlans={sellingPlans}
          handleVariantChange={handleVariantChange}
          selectedSellingPlanDetail={selectedSellingPlanDetail}
        />
      </TailwindModal>
    </React.Fragment>
  );
};
export default ProductCard;

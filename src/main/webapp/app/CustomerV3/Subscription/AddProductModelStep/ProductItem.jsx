import React, { useEffect, useState } from 'react';
import { Input } from 'reactstrap';

function ProductItem({selectedVariantItems, prdData, getdiscountedPrice, formatPrice, checkDisAllowVariantIds, purchaseOption, customerPortalSettingEntity, selectProduct,selectProductButtonText, isSwapProductModalOpen, ...props}) {
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [filterredVariants, setFilteredVariants] = useState([]);
  var modifiedPrdData = JSON.parse(JSON.stringify(prdData));
    modifiedPrdData.variants.forEach(varData => {
    var sellingPrice =
        purchaseOption == 'SUBSCRIBE'
        ? getdiscountedPrice(varData, prdData?.productData, varData?.price, customerPortalSettingEntity?.applySellingPlanBasedDiscount)
        : varData?.price;
    varData.appstleSellingPrice = sellingPrice;
    varData.appleSellingPriceWithCurrency = formatPrice(sellingPrice);
    var variantData = prdData?.productData?.variants?.filter(variant => variant?.id === varData?.uniqueId).pop();
    varData.compareAtPrice = variantData?.compare_at_price || variantData?.price;
    });

    const checkDisAllowVariantIdsToEditAndRemove = (modifiedPrdData) => {
        let visibleVariants = [];
        if (modifiedPrdData?.variants?.length > 0) {
          visibleVariants = modifiedPrdData?.variants?.filter(a => !customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(a?.uniqueId));
        }
        return visibleVariants;
    }

  useEffect(() => {
    let filterProductVariants = [];
    if (isSwapProductModalOpen || (purchaseOption == 'SUBSCRIBE')) {
      setFilteredVariants(
        prdData?.variants?.filter(
          variant => ((!customerPortalSettingEntity?.includeOutOfStockProduct ? variant?.available : true))
        )
      );
    } else {
      setFilteredVariants(
        prdData?.variants?.filter(
          variant => (!customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(variant?.uniqueId) && (!customerPortalSettingEntity?.includeOutOfStockProduct ? variant?.available : true))
        )
      );
    }
  }, [prdData, customerPortalSettingEntity]);

  useEffect(() => {
    if (filterredVariants && filterredVariants.length > 0) {
      setSelectedVariant(filterredVariants[0].id);
    }
  }, [filterredVariants]);

  const handleVariantChange = event => {
    setSelectedVariant(event?.target?.value);
  };

    return (
    modifiedPrdData?.variants?.length > 0 &&
    (isSwapProductModalOpen ? (checkDisAllowVariantIdsToEditAndRemove(modifiedPrdData)?.length > 0) : (checkDisAllowVariantIds(modifiedPrdData)?.length > 0))
        &&
    (purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag
        ? !modifiedPrdData?.variants[0]?.requires_selling_plan
        : true) && (
        <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-product-card as-flex as-flex-col">
        <div className="as-flex as-justify-between">
            {(customerPortalSettingEntity?.enableRedirectToProductPage && prdData?.prdHandleName) ? 
              <a className='as-product-title-tag-a as-h-40 as-w-full as-object-contain' href={`https://${Shopify?.shop}/products/${prdData?.prdHandleName}`} target='_blank'><img src={modifiedPrdData?.imgSrc} className="as-h-40 as-w-full as-object-contain" /></a> :
              <img src={modifiedPrdData?.imgSrc} className="as-h-40 as-w-full as-object-contain" />
            }
        </div>
        <div className="as-my-3 as-text-center" style={{ minHeight: '50px' }}>
            <p
            className="as-text-sm as-text-gray-900 as-mt-1"
            style={{
                maxHeight: '80px',
                height: '100%',
                overflow: 'hidden'
            }}
            >
            {(customerPortalSettingEntity?.enableRedirectToProductPage && prdData?.prdHandleName) ?  
              <a className='as-product-title-tag-a' href={`https://${Shopify?.shop}/products/${prdData?.prdHandleName}`} target='_blank'>{modifiedPrdData?.title}</a> :
              modifiedPrdData?.title
            }
            </p>
        </div>
        <footer className="as-mt-3">
            <div className="as-mt-1 as-text-center">
            <p className="as-text-sm as-text-gray-500">
                {/* {customerPortalSettingEntity?.fromV2}{' '} */}
                {formatPrice(((modifiedPrdData?.variants?.filter(variant => variant?.uniqueId == selectedVariant?.split('/')?.pop()).pop())?.appstleSellingPrice))}
            </p>
          </div>
          <div className="as-mt-2">
            {(filterredVariants?.length > 1 || prdData?.productData?.variantLength > 1) && (
              <div className="as-my-3">
                <div className="d-flex" style={{ alignItems: 'center' }}>
                  <Input
                    value={selectedVariant?.id}
                    type="select"
                    onChange={event => {
                        handleVariantChange(event);
                    }}
                    className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                  >
                    {filterredVariants?.map((variant, index) => {
                      return (
                        <>
                          {(purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag
                            ? !variant?.requires_selling_plan
                            : true) && (
                            <option key={variant?.id} value={variant?.id}>
                              {variant?.title}
                            </option>
                          )}
                        </>
                      );
                    })}
                  </Input>
                </div>
              </div>
            )}
            <button
                type="button"
                onClick={() => selectProduct(modifiedPrdData, selectedVariant)}
                class="as-mt-auto as-w-full as-items-center as-px-2 as-py-3 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary as-product-card-button"
            >
                {selectProductButtonText ??
                  (isSwapProductModalOpen !== undefined && isSwapProductModalOpen
                    ? customerPortalSettingEntity?.addProductSwapButtonTextV2
                    : customerPortalSettingEntity?.addProductButtonTextV2)}
            </button>
            </div>
        </footer>
        </div>
    )
    );
}

export default ProductItem;

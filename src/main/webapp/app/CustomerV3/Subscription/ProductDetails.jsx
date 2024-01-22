import React, { useEffect, useState } from 'react';
import { Input } from 'reactstrap';
import { formatPrice } from 'app/shared/util/customer-utils';

export default function ProductDetail(props) {
  const {
    purchaseOption,
    productData,
    showProductRadioButtons,
    customerPortalSettingEntity,
    setSelectedItemsToAdd,
    disableQuantity,
    quantity,
    setQuantity,
    isQuantityInputInValid,
    setIsQuantityInputInValid,
    setPurchaseOption,
    selectedProductVariant,
    oldQuantity,
    isSwapProduct
  } = props;
  console.log(purchaseOption);
  const [selectedVariant, setSelectedVariant] = useState({});
  const [filterredVariants, setFilteredVariants] = useState([]);

  let [quantityInputBlurred, setQuantityInputBlurred] = useState(false);

  useEffect(() => {
    if (validateNumber(quantity) || Number(quantity) < Number(customerPortalSettingEntity?.minQtyToAllowDuringAddProduct)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  }, [quantity]);

  useEffect(() => {
    if (oldQuantity) {
      setQuantity(oldQuantity);
    }
  },[oldQuantity]);

  useEffect(() => {
    if (selectedProductVariant) {
      handleVariantChange(isNaN(selectedProductVariant) ? selectedProductVariant : `gid://shopify/ProductVariant/${selectedProductVariant}`);
    } else {
      let selectedVarObj = filterredVariants?.[0];
      setSelectedVariant(selectedVarObj);
      setSelectedItemsToAdd([
        {
          id: selectedVarObj?.id,
          uniqueId: selectedVarObj?.uniqueId,
          title: selectedVarObj?.title,
          imageSrc: productData?.imageSrc,
          prdHandleName: productData?.prdHandleName
        }
      ]);
    }
  }, [filterredVariants, productData]);

  useEffect(() => {
    setFilteredVariants(productData?.variants?.filter(variant => !customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(variant?.uniqueId)))
  }, [productData, customerPortalSettingEntity])

  const handleVariantChange = value => {
    let selectedVarObj = productData?.variants.find(variant => variant.id == value);
    setSelectedVariant({ ...selectedVarObj });
    setSelectedItemsToAdd([
      {
        id: selectedVarObj?.id,
        uniqueId: selectedVarObj?.uniqueId,
        title: selectedVarObj?.title,
        imageSrc: productData?.imageSrc,
        prdHandleName: productData?.prdHandleName
      }
    ]);
  };

  const validateNumber = value => {
    const type = typeof value;
    value = String(value);
    if (type === 'undefined') {
      return undefined;
    } else if (!value?.trim()) {
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
    if (validateNumber(value) || Number(value) < Number(customerPortalSettingEntity?.minQtyToAllowDuringAddProduct)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const quantityInputBlurHandler = value => {
    setQuantity(value);
    setQuantityInputBlurred(true);
    if (validateNumber(value) || Number(value) < Number(customerPortalSettingEntity?.minQtyToAllowDuringAddProduct)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  return (
    <>
      <div className="as-grid as-grid-cols-12 as-gap-4 as-py-2 as-mb-3">
        <div className="as-col-span-4">
          <img src={productData?.imgSrc} alt="" className="as-w-full as-h-70" />
        </div>
        <div className="as-col-span-8">
        </div>
      </div>
      <div className="">
        <p className={`as-text-2xl as-text-gray-900 as-mb-1 as-text-left`}>{productData?.title}</p>

        <div className="as-flex">
          <p className="as-text-md as-text-gray-600 as-text-left">
            {formatPrice(quantity * selectedVariant?.appstleSellingPrice)} 
            {(selectedVariant?.compareAtPrice !== selectedVariant?.appstleSellingPrice) && <span className="as-text-xs as-text-gray-500 as-line-through as-product-strikeout-price as-ml-2 as-mr-2">{formatPrice(quantity * selectedVariant?.compareAtPrice)}</span>}
            <span className="as-text-xs as-text-gray-500">
              ({formatPrice(selectedVariant?.appstleSellingPrice)}/{customerPortalSettingEntity?.quantityLbl})
            </span>
          </p>
        </div>
        <div className="as-my-4">
          <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">
            {customerPortalSettingEntity?.editQuantityLabelText}
          </label>
          <Input
            value={quantity}
            type="number"
            readOnly={isSwapProduct}
            onInput={event => quantityInputChangeHandler(event.target.value)}
            onBlur={event => quantityInputBlurHandler(event.target.value)}
            onChange={event => quantityInputChangeHandler(event.target.value)}
            className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full sm:as-text-sm as-border-gray-300 as-rounded-md disabled:as-bg-gray-100 disabled:as-border disabled:as-border-gray-300 disabled:as-text-gray-900 disabled:as-cursor-not-allowed"
          />
          {isQuantityInputInValid && (
            <p class="as-mt-2 as-text-sm as-text-red-600">
              {customerPortalSettingEntity?.validNumberRequiredMessage || 'Please enter valid number'}
            </p>
          )}
        </div>
        {(filterredVariants?.length > 1 || productData?.variantLength > 1) && (
          <div className="as-my-3">
            <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">
              {customerPortalSettingEntity?.changeVariantLabelText ? customerPortalSettingEntity?.changeVariantLabelText : 'Change Variant'}
            </label>
            <div className="d-flex" style={{ alignItems: 'center' }}>
              <Input
                value={selectedVariant?.id}
                type="select"
                onChange={event => {
                  handleVariantChange(event.target.value);
                }}
                className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
              >
                {filterredVariants?.map((variant, index) => {
                  return (
                    <>
                      {
                        (purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag ? !variant?.requires_selling_plan : true)
                        &&
                          <option key={variant?.id} value={variant?.id}>
                            {variant?.title}
                          </option>
                      }
                    </>
                  );
                })}
              </Input>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

import React, {useEffect, useState} from 'react';
import {Input} from 'reactstrap';
import axios from 'axios';
import {getCustomerPortalEntity} from 'app/entities/subscriptions/subscription.reducer';
import {connect, useSelector} from 'react-redux';

// import './loader.scss';
import {toast} from 'react-toastify';
// import './appstle-subscription.scss';
import TailwindModal from './TailwindModal';
import Loader from './Loader';
import PopupMessaging from './PopupMessaging';

const EditCustomerContractDetail = ({
                                      productQuantity,
                                      getCustomerPortalEntity,
                                      contractId,
                                      lineId,
                                      shopName,
                                      productId,
                                      variantId,
                                      subscriptionEntities,
                                      totalProductPriceObj,
                                      productPrice,
                                      index,
                                      onComplete,
                                      shopInfo,
                                      currencyCode,
                                      attributeEdit,
                                      subscriptionContractFreezeStatus,
                                      isEditModalOpen,
                                      setIsEditModalOpen,
                                      productImage,
                                      productTitle,
                                      subUpcomingOrderEntities,
                                      prd,
                                      oldVariantId,
                                      productData
                                    }) => {
  let [quantity, setQuantity] = useState('');
  let [editMode, setEditMode] = useState(false);
  let [variants, setVariants] = useState([]);
  let [selectedVariant, setSelectedVariant] = useState(prd?.node?.variantId);
  let [updateInProgress, setUpdateInProgress] = useState(false);
  let [deleteInProgress, setDeleteInProgress] = useState(false);
  let [price, setPrice] = useState('');
  let [priceInputBlurred, setPriceInputBlurred] = useState(false);
  let [isPriceInputInValid, setIsPriceInputInValid] = useState(false);
  let [quantityInputBlurred, setQuantityInputBlurred] = useState(false);
  let [isQuantityInputInValid, setIsQuantityInputInValid] = useState(false);
  let [totalAmount, setTotalAmount] = useState(parseFloat(totalProductPriceObj?.amount).toFixed(2));
  let [handleAlert, setHandleAlert] = useState({show: false, data: null});
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  let [editVariantMode, setEditVariantMode] = useState(false);
  const [isVariantChanged, setIsVariantChanged] = useState(false);
  const [isContextualPriceUpdateInProgress, setIsContextualPriceUpdateInProgress] = useState(false);
  const [isSwapProductModalOpen, setIsSwapProductModalOpen] = useState(false);

  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const subscriptionEntitiesFull = useSelector(state => state.subscription.entity);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const resetEditProductModal = () => {
    setIsEditModalOpen(false);
    setShowMessaging(false);
    setSuccess(false);
    setError(false);

    setSelectedVariant(prd?.node?.variantId);
    setQuantityInputBlurred(false);
    setIsQuantityInputInValid(false);
    setTotalAmount(parseFloat(totalProductPriceObj?.amount).toFixed(2));
    setIsVariantChanged(false);
    setQuantity(productQuantity);
    setPrice((productPrice / productQuantity).toFixed(2));
  };

  useEffect(() => {
    setSelectedVariant(variantId);
    // Check if productData and variants exist
    if (!productData || !productData?.variants) {
      const prdId = productId?.split('/').pop();
      if (prdId) {
        axios.get(`api/data/product?productId=${prdId}`).then((res) => {
          const variantsFromApi = res?.data?.variants || [];
          setVariants(variantsFromApi);
        });
      }
      return; // Exit early if there's no productData or variants
    }
  
    const { productSelectionOption } = customerPortalSettingEntity;
  
    if (productSelectionOption === 'PRODUCTS_FROM_ALL_PLANS') {
      setVariants(productData.variants.filter((v) => v.selling_plan_allocations.length > 0));
    } else if (productSelectionOption === 'PRODUCTS_FROM_CURRENT_PLAN') {
      setVariants(
        productData.variants.filter((v) =>
          v.selling_plan_allocations.some(
            (a) => 'gid://shopify/SellingPlan/' + a.selling_plan_id === prd?.node?.sellingPlanId
          )
        )
      );
    } else {
      setVariants(productData.variants);
    }
  }, [variantId, productData]);
  

  useEffect(() => {
    if (validateNumber(quantity) || (Number(quantity) < Number(customerPortalSettingEntity?.minQtyToAllowDuringAddProduct))) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  }, [quantity])

  useEffect(() => {
    if (prd?.node?.variantId === selectedVariant) {
      setIsVariantChanged(false);
    } else {
      setIsVariantChanged(true);
    }
  }, [selectedVariant]);

  useEffect(() => {
    setQuantity(productQuantity);
    setPrice((productPrice / productQuantity).toFixed(2));
  }, [productPrice, productQuantity]);

  useEffect(() => {
    setEditMode(false);
  }, [lineId]);

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

  const priceInputBlurHandler = value => {
    setPrice(value);
    setPriceInputBlurred(true);
    if (validateNumber(value)) {
      setIsPriceInputInValid(true);
    } else {
      setIsPriceInputInValid(false);
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

  const priceInputChangeHandler = value => {
    setPrice(value);
    if (!priceInputBlurred) return;
    if (validateNumber(value)) {
      setIsPriceInputInValid(true);
    } else {
      setIsPriceInputInValid(false);
    }
  };

  const quantityInputChangeHandler = value => {
    setQuantity(value);
    setTotalAmount((parseFloat(price) * parseInt(value)).toFixed(2));
    if (!quantityInputBlurred) return;
    if (validateNumber(value) || Number(value) < Number(customerPortalSettingEntity?.minQtyToAllowDuringAddProduct)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const handleVariantChange = event => {
    setIsContextualPriceUpdateInProgress(true);
    if (shopInfo?.currency !== currencyCode) {
      axios
        .get(
          '/api/data/variant-contextual-pricing?currencyCode=' +
          totalProductPriceObj?.currencyCode +
          '&variantId=' +
          event?.target?.value?.split('/')?.pop()
        )
        .then(data => {
          setPrice(parseFloat(data?.data?.contextualPricing?.price?.amount));
          setTotalAmount((parseFloat(data?.data?.contextualPricing?.price?.amount) * parseInt(quantity)).toFixed(2));
          setIsContextualPriceUpdateInProgress(false);
        });
    } else {
      variants.forEach(variant => {
        if (variant?.admin_graphql_api_id === event?.target?.value) {
          setPrice(variant?.price);
          setTotalAmount((parseFloat(variant?.price) * parseInt(quantity)).toFixed(2));
          setIsContextualPriceUpdateInProgress(false);
        }
      });
    }
    setSelectedVariant(event?.target?.value);
  };

  const updateProduct = async () => {
    if (!isPriceInputInValid && !isQuantityInputInValid) {
      setUpdateInProgress(true);
      const requestUrl = `api/subscription-contracts-update-line-item-quantity?contractId=${contractId}&lineId=${lineId}&quantity=${quantity}&variantId=${selectedVariant}&isExternal=true`;
      return await axios
        .put(requestUrl)
        .then(res => {
          getCustomerPortalEntity(contractId);
          setUpdateInProgress(false);
          toast.success(customerPortalSettingEntity?.contractUpdateMessageTextV2 || 'Contract Updated', options);
          setEditMode(!editMode);
          return res;
        })
        .catch(err => {
          console.log('Error in updating product', err);
          setUpdateInProgress(false);
          toast.error(err.response.data.message, options);
          return err;
        });
    }
  };

  const updateVariant = async () => {
    console.log('editcustomerdetail -> 225');
    setUpdateInProgress(true);
    if (selectedVariant !== variantId)
      {
        let newVariant = parseInt(selectedVariant?.split('/').pop());
        const requestBody = {
          shop: shopName,
          contractId: contractId,
          oldVariants: [parseInt(oldVariantId)],
          newVariants: {
            [newVariant]: productQuantity
          },
          oldLineId: lineId,
        };
        console.log("Selected variant", selectedVariant);

        const requestUrl = 'api/subscription-contract-details/replace-variants-v3';

        return axios.post(requestUrl, requestBody)
        .then(async res => {
          getCustomerPortalEntity(contractId);
          setUpdateInProgress(false);
          toast.success(customerPortalSettingEntity?.contractUpdateMessageTextV2 || 'Contract Updated', options);
          setEditMode(false);
          setEditVariantMode(false);
          return res;
        })
        .catch(err => {
          setUpdateInProgress(false);
          setEditMode(false);
          setEditVariantMode(false);
          toast.error(customerPortalSettingEntity?.contractErrorMessageTextV2 || 'Contract Updated Failed', options);
          return err;
        });
          
        } 
    else {
        setUpdateInProgress(false);
        }
  };

  const deleteProduct = () => {
    setDeleteInProgress(true);
    setHandleAlert({show: false, data: null});
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true&removeDiscount=${true}`;
    axios
      .put(requestUrl)
      .then(res => {
        getCustomerPortalEntity(contractId);
        setDeleteInProgress(false);

        toast.success('Product deleted from contract', options);
      })
      .catch(err => {
        console.log('Error in deleting product');
        setDeleteInProgress(false);
        toast.error(err.response.data.message, options);
      });
  };

  const resetVariant = () => {
    setEditVariantMode(!editVariantMode);
    handleVariantChange({target: {value: variantId}});
  };

  const handleProductUpdateAction = async () => {
    let results = null;
    if (isVariantChanged) {
      results = await updateVariant();
    } else {
      results = await updateProduct();
    }
    if (results) {
      setShowMessaging(true);
      if (results?.status === 200) {
        setSuccess(true);
        setError(false);
      } else {
        setSuccess(false);
        setError(true);
      }
    }
  };


  return (
    <>
      <TailwindModal
        open={isEditModalOpen}
        setOpen={resetEditProductModal}
        actionMethod={handleProductUpdateAction}
        actionButtonText={!showMessaging ? customerPortalSettingEntity?.updateButtonText || 'Update' : ''}
        updatingFlag={updateInProgress || isContextualPriceUpdateInProgress}
        modalTitle={customerPortalSettingEntity?.editCommonText || 'Edit Product'}
        class="as-model-edit-product"
        success={success}
      >
        {showMessaging && <PopupMessaging showSuccess={success} showError={error}/>}
        {!showMessaging && (
          <div className={isSwapProductModalOpen ? `as-hidden` : ``}>
            <div className="as-grid as-grid-cols-12 as-gap-4 as-py-2 as-mb-3">
              <div className="as-col-span-4">
                <img
                  src={
                    prd?.node?.productId == null
                      ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                      : prd?.node?.variantImage?.transformedSrc
                  }
                  alt=""
                  className="as-w-full as-h-70"
                />
              </div>
              <div className="as-col-span-8">
                {/* {prd?.node?.title && <p className={`as-text-2xl as-text-gray-900 as-mb-1 as-text-left ${prd?.node?.productId == null ? "as-line-through" : ""}`}>{prd?.node?.title}{prd?.node?.productId == null ? "*" : ""}</p>}

              <div className="as-flex as-items-center">
                <p className="as-text-sm as-text-gray-500"><span className="as-font-medium as-text-gray-600">Amount: </span>{price} {currencyCode}<span className="as-text-xs as-text-400">/quantity</span></p>
                {isContextualPriceUpdateInProgress && <div className="as-ml-2">
                  <Loader size="4" />
                </div>}
              </div>
              <div className="as-flex as-items-center">
                <p className="as-text-sm as-text-gray-500"><span className="as-font-medium as-text-gray-600">Total: </span>{totalAmount} {currencyCode}</p>
                {isContextualPriceUpdateInProgress && <div className="as-ml-2">
                  <Loader size="4" />
                </div>}
              </div> */}
              </div>
            </div>
            <div className="">
              {prd?.node?.title && (
                <p
                  className={`as-text-2xl as-text-gray-900 as-mb-1 as-text-left ${prd?.node?.productId == null ? 'as-line-through' : ''}`}>
                  {prd?.node?.title}
                  {prd?.node?.productId == null ? '*' : ''}
                </p>
              )}

              <div className="as-flex">
                <p className="as-text-md as-text-gray-600 as-text-left">
                  {totalAmount} {currencyCode}{' '}
                  <span className="as-text-xs as-text-gray-500">
                    ({price} {currencyCode}/{customerPortalSettingEntity?.quantityLbl})
                  </span>
                </p>
                {isContextualPriceUpdateInProgress && (
                  <div className="as-ml-2">
                    <Loader size="4"/>
                  </div>
                )}
              </div>

              <div className="as-my-4">
                <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">
                  {customerPortalSettingEntity?.editQuantityLabelText}
                </label>
                <Input
                  value={quantity}
                  type="number"
                  // onInput={event => quantityInputChangeHandler(event.target.value)}
                  onBlur={event => quantityInputBlurHandler(event.target.value)}
                  onChange={event => quantityInputChangeHandler(event.target.value)}
                  disabled={!customerPortalSettingEntity?.editProductFlag}
                  className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full sm:as-text-sm as-border-gray-300 as-rounded-md disabled:as-cursor-not-allowed"
                />
                {isQuantityInputInValid && (
                  <p class="as-mt-2 as-text-sm as-text-red-600">
                    {customerPortalSettingEntity?.validNumberRequiredMessage || 'Please enter valid number'}
                  </p>
                )}
              </div>
              {variants?.length > 1 && (
                <div className="as-my-3">
                  <label className="as-block as-text-sm as-font-medium as-text-gray-900 as-text-left">
                    {customerPortalSettingEntity?.changeVariantLabelText
                      ? customerPortalSettingEntity?.changeVariantLabelText
                      : 'Change Variant'}
                  </label>
                  <div className="d-flex" style={{alignItems: 'center'}}>
                    <Input
                      value={selectedVariant}
                      type="select"
                      disabled={!customerPortalSettingEntity?.enableSwapProductVariant} 
                      onChange={event => {
                        handleVariantChange(event);
                      }}
                      className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                    >
                      {variants.map(variant => {
                        return (
                          <option
                            key={variant?.admin_graphql_api_id}
                            value={variant?.admin_graphql_api_id}
                            selected={variant?.admin_graphql_api_id === variantId ? true : false}
                          >
                            {variant?.title}
                          </option>
                        );
                      })}
                    </Input>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}
      </TailwindModal>
    </>
  );
};

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(EditCustomerContractDetail);

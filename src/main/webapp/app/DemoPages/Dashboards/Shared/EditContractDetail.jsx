import React, { useEffect, useState } from 'react';
import { Button, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import axios from 'axios';
import { deleteEntity, getEntity } from 'app/entities/subscriptions/subscription.reducer';
import { connect } from 'react-redux';
import './loader.scss';
import { toast } from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { isOneTimeProduct, isFreeProduct } from 'app/shared/util/subscription-utils';

const EditContractDetail = ({
  productQuantity,
  getEntity,
  contractId,
  lineId,
  shop,
  productId,
  variantId,
  subscriptionEntities,
  productPrice,
  sellingPlanName,
  manualCopounDiscountEnabled,
  isPrepaid,
  currencyCode,
  shopInfo,
  customerPortalSettingEntity,
  recurringProductsInSubscription,
  freeProductsInSubscription,
  oneTimeProductsInSubscription,
  prd
}) => {
  let [quantity, setQuantity] = useState('');
  let [editMode, setEditMode] = useState(false);
  let [variants, setVariants] = useState([]);
  let [selectedVariant, setSelectedVariant] = useState('');
  let [updateInProgress, setUpdateInProgress] = useState(false);
  let [deleteInProgress, setDeleteInProgress] = useState(false);
  let [price, setPrice] = useState('');
  let [priceInputBlurred, setPriceInputBlurred] = useState(false);
  let [isPriceInputInValid, setIsPriceInputInValid] = useState(false);
  let [quantityInputBlurred, setQuantityInputBlurred] = useState(false);
  let [isQuantityInputInValid, setIsQuantityInputInValid] = useState(false);
  let [editVariantMode, setEditVariantMode] = useState(false);
  let [sellingPlanNameValue, setSellingPlanNameValue] = useState('');
  let [sellingPlanInputBlurred, setSellingPlanInputBlurred] = useState(false);
  let [isSellingPlanInputInValid, setIsSellingPlanInputInValid] = useState(false);
  let [deleteDiscountFirst, setDeleteDiscountFirst] = useState(false);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  useEffect(() => {
    setSelectedVariant(variantId);
    const prdId = productId?.split('/').pop();
    prdId &&
      axios.get(`/api/data/product?productId=${prdId}`).then(res => {
        res?.data?.variants && setVariants(res?.data?.variants);
      });
  }, [variantId]);

  useEffect(() => {
    setQuantity(productQuantity);
    setPrice(parseFloat(productPrice).toFixed(2));
    setSellingPlanNameValue(sellingPlanName);
  }, [productPrice, productQuantity, sellingPlanName]);

  useEffect(() => {
    setEditMode(false);
  }, [lineId]);

  const validateNumber = value => {
    const type = typeof value;
    if (type === 'undefined') {
      return undefined;
    } else if (!value.trim()) {
      return `field value is required`;
    } else if (isNaN(value)) {
      return `field value should be a number`;
    } else if (parseInt(value) < 0) {
      return `field value should be greater or equal to 0.`;
    } else {
      return undefined;
    }
  };

  const validateText = value => {
    const type = typeof value;
    if (type === 'undefined') {
      return undefined;
    } else if (!value) {
      return `field value is required`;
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
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const sellingPlanInputBlurHandle = value => {
    setSellingPlanNameValue(value);
    setSellingPlanInputBlurred(true);
    if (validateText(value)) {
      setSellingPlanInputBlurred(true);
    } else {
      setSellingPlanInputBlurred(false);
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
    if (!quantityInputBlurred) return;
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const sellingPlanInputChangeHandler = value => {
    setSellingPlanNameValue(value);
    // if (!sellingPlanInputBlurred) return;
    if (validateText(value)) {
      setIsSellingPlanInputInValid(true);
    } else {
      setIsSellingPlanInputInValid(false);
    }
  };

  const handleVariantChange = event => {
    if (shopInfo?.currency !== currencyCode) {
      axios
        .get('/api/data/variant-contextual-pricing?currencyCode=' + currencyCode + '&variantId=' + event?.target?.value?.split('/')?.pop())
        .then(data => {
          setPrice(parseFloat(data?.data?.contextualPricing?.price?.amount));
        });
    } else {
      variants.forEach(variant => {
        if (variant?.admin_graphql_api_id === event?.target?.value) {
          setPrice(variant?.price);
        }
      });
    }
    setSelectedVariant(event?.target?.value);
  };

  const updateProduct = () => {
    if (!isPriceInputInValid && !isQuantityInputInValid) {
      setUpdateInProgress(true);
      const requestUrl = `/api/subscription-contracts-update-line-item?contractId=${contractId}&price=${price}&lineId=${lineId}&quantity=${quantity}&variantId=${selectedVariant}&sellingPlanName=${encodeURIComponent(
        sellingPlanNameValue
      )}`;
      axios
        .put(requestUrl)
        .then(res => {
          getEntity(contractId);
          setUpdateInProgress(false);
          toast.success('Contract Updated', options);
        })
        .catch(err => {
          console.log('Error in updating product');
          setUpdateInProgress(false);
          toast.error('Contract Update Failed', options);
        });
    }
  };

  const updateVariant = () => {
    setUpdateInProgress(true);
    if (selectedVariant !== variantId) {
      axios
        .put(
          `/api/v2/subscription-contracts-add-line-item?contractId=${contractId}&price=${price}&quantity=${quantity}&variantId=${selectedVariant}`
        )
        .then(data => {
          const requestUrl = `/api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&removeDiscount=${true}`;
          axios
            .put(requestUrl)
            .then(res => {
              getEntity(contractId);
              setUpdateInProgress(false);
              toast.success('Contract Updated', options);
              setEditMode(false);
              setEditVariantMode(false);
            })
            .catch(err => {
              setUpdateInProgress(false);
              toast.error('Contract Update Failed', options);
            });
        })
        .catch(err => {
          setUpdateInProgress(false);
          toast.error(err?.response?.data?.message || 'Contract Update Failed', options);
        });
    } else {
      setUpdateInProgress(false);
    }
  };

  const deleteProduct = removeDiscount => {
    setDeleteInProgress(true);
    const requestUrl = `/api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&removeDiscount=${
      removeDiscount != null ? removeDiscount : false
    }`;
    axios
      .put(requestUrl)
      .then(res => {
        getEntity(contractId);
        setDeleteInProgress(false);
        toast.success('Product deleted from contract', options);
        setDeleteDiscountFirst(false);
      })
      .catch(err => {
        if (err.response && err.response.data.errorKey === '10001') {
          setDeleteDiscountFirst(true);
        }
        setDeleteInProgress(false);
        // toast.error('Delete Product Failed', options);
      });
  };

  const resetVariant = () => {
    setEditVariantMode(!editVariantMode);
    handleVariantChange({ target: { value: variantId } });
  };

  return (
    <>
      {editMode && (
        <div className="mt-3">
          {variants?.length > 1 && (
            <FormGroup>
              <Label>Change Variant</Label>
              <div className="d-flex">
                <Input
                  value={selectedVariant}
                  type="select"
                  disabled={!editVariantMode}
                  onChange={event => {
                    handleVariantChange(event);
                  }}
                >
                  {variants.map(variant => {
                    return (
                      <option value={variant?.admin_graphql_api_id} selected={variant?.admin_graphql_api_id === variantId ? true : false}>
                        {variant?.title}
                      </option>
                    );
                  })}
                </Input>
                {!editVariantMode && (
                  <Button
                    color="primary ml-1"
                    className="primary btn btn-primary d-flex align-items-center"
                    onClick={() => setEditVariantMode(!editVariantMode)}
                  >
                    <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                  </Button>
                )}
              </div>
              <div className="d-flex mt-2">
                {editVariantMode && (
                  <Button color="success" className="primary btn btn-success d-flex align-items-center" onClick={() => updateVariant()}>
                    {!updateInProgress && <i className="lnr pe-7s-diskette btn-icon-wrapper"></i>}
                    {updateInProgress && <div className="appstle_loadersmall" />}
                  </Button>
                )}
                {editVariantMode && (
                  <Button color="warning" className="ml-2 primary btn btn-primary d-flex align-items-center" onClick={() => resetVariant()}>
                    <i className="lnr lnr-cross btn-icon-wrapper"></i>
                  </Button>
                )}
              </div>
            </FormGroup>
          )}
          {/* {!editVariantMode && <FormGroup>
            <Label>Edit Selling Plan</Label>
            <Input
              value={sellingPlanNameValue}
              invalid={isSellingPlanInputInValid}
              type="text"
              onInput={event => sellingPlanInputChangeHandler(event.target.value)}
              onBlur={event => sellingPlanInputBlurHandle(event.target.value)}
            />
            <FormFeedback>Please enter valid text</FormFeedback>
          </FormGroup>} */}
          {/* {!editVariantMode && <FormGroup>
            <Label>Edit Quantities</Label>
            <Input
              value={quantity}
              invalid={isQuantityInputInValid}
              type="number"
              onInput={event => quantityInputChangeHandler(event.target.value)}
              onBlur={event => quantityInputBlurHandler(event.target.value)}
            />
            <FormFeedback>Please enter valid number</FormFeedback>
          </FormGroup>} */}
          {/* {!editVariantMode && <FormGroup>
            <Label>{isPrepaid ? "Total Prepaid Price (including all deliveries)" : "Current price per Quantity"}</Label>
            <Input
              value={price}
              invalid={isPriceInputInValid}
              type="number"
              onInput={event => priceInputChangeHandler(event.target.value)}
              onBlur={event => priceInputBlurHandler(event.target.value)}
            />
            <FormFeedback>Please enter valid number</FormFeedback>
            <span style={{color: '#545cd8', fontSize: "11px", marginTop: "-10px"}}>Note: Use discounted price if the contract is imported.</span>
          </FormGroup>} */}
        </div>
      )}
      <div className="mt-3 d-flex">
        {!editMode && variants?.length > 1 && (
          <Button color="primary" className="primary btn btn-primary d-flex align-items-center" onClick={() => setEditMode(!editMode)}>
            <i className="lnr lnr-pencil btn-icon-wrapper"></i>
          </Button>
        )}
        {/* {editMode && !editVariantMode && (
          <Button color="success" className="primary btn btn-success d-flex align-items-center" onClick={() => updateProduct()}>
            {!updateInProgress && <i className="lnr pe-7s-diskette btn-icon-wrapper"></i>}
            {updateInProgress && <div className="appstle_loadersmall" />}
          </Button>
        )} */}
        {(isOneTimeProduct(prd) || isFreeProduct(prd) || (recurringProductsInSubscription?.length > 1)) && !editVariantMode && (
          <Button
            color="danger"
            className="ml-2 primary btn btn-primary d-flex align-items-center"
            onClick={() => deleteProduct(customerPortalSettingEntity?.removeDiscountCodeAutomatically)}
          >
            {!deleteInProgress && <i className="lnr lnr-trash btn-icon-wrapper"></i>}
            {deleteInProgress && <div className="appstle_loadersmall" />}
          </Button>
        )}
        {editMode && !editVariantMode && (
          <Button color="warning" className="ml-2 primary btn btn-primary d-flex align-items-center" onClick={() => setEditMode(!editMode)}>
            <i className="lnr lnr-cross btn-icon-wrapper"></i>
          </Button>
        )}
      </div>

      <Modal isOpen={deleteDiscountFirst} toggle={() => setDeleteDiscountFirst(!deleteDiscountFirst)} size="lg">
        <ModalHeader>Confirmation</ModalHeader>
        <ModalBody>
          {customerPortalSettingEntity?.removeDiscountCodeLabel ||
            'This product is tied to a discount, please remove the discount before trying to remove the product.'}
        </ModalBody>
        <ModalFooter>
          <Button color="link" onClick={() => setDeleteDiscountFirst(!deleteDiscountFirst)}>
            Cancel
          </Button>
          <MySaveButton onClick={() => deleteProduct(true)} updating={deleteInProgress} text={'Confirm'} updatingText={'Deleting'} />
        </ModalFooter>
      </Modal>
    </>
  );
};

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  customerPortalSettingEntity: state.customerPortalSettings.entity
});

const mapDispatchToProps = {
  getEntity,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(EditContractDetail);

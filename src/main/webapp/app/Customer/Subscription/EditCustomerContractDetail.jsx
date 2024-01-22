import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { Button, Input, Label, FormGroup, FormFeedback, Row, Col, Popover, PopoverHeader, PopoverBody, Tooltip  } from 'reactstrap';
import axios from 'axios';
import { getCustomerPortalEntity, deleteCustomerEntity } from 'app/entities/subscriptions/subscription.reducer';
import { connect, useSelector } from 'react-redux';
// import './loader.scss';
import { toast } from 'react-toastify';
import _ from 'lodash';
import SweetAlert from 'sweetalert-react';
import './appstle-subscription.scss';
import { faPencilAlt, faTrash, faCheck, faTimes, faLock } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { motion, AnimatePresence } from "framer-motion";
import { formatPrice } from 'app/shared/util/customer-utils';

const EditCustomerContractDetail = ({
  productQuantity,
  getCustomerPortalEntity,
  contractId,
  lineId,
  shop,
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
  subscriptionContractFreezeStatus
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
  let [totalAmount, setTotalAmount] = useState(totalProductPriceObj?.amount);
  let [handleAlert, setHandleAlert] = useState({ show: false, data: null });
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  let [editVariantMode, setEditVariantMode] = useState(false);
  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  useEffect(() => {
    setSelectedVariant(variantId);
    const prdId = productId?.split('/').pop();
    prdId &&
      axios.get(`api/data/product?productId=${prdId}`).then(res => {
        res?.data?.variants && setVariants(res?.data?.variants);
      });
  }, [variantId]);

  useEffect(() => {
    setQuantity(productQuantity);
    setPrice((productPrice / productQuantity).toFixed(2));
  }, [productPrice, productQuantity]);

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
    if (validateNumber(value)) {
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
    setTotalAmount(parseFloat(price) * parseInt(value));
    if (!quantityInputBlurred) return;
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
    }
  };

  const handleVariantChange = event => {
    setUpdateInProgress(true)
    if (shopInfo?.currency !== currencyCode) {
      axios.get("/api/data/variant-contextual-pricing?currencyCode=" + totalProductPriceObj?.currencyCode + "&variantId=" + event?.target?.value?.split("/")?.pop())
      .then(data => {
        setPrice(parseFloat(data?.data?.contextualPricing?.price?.amount));
        setTotalAmount(parseFloat(data?.data?.contextualPricing?.price?.amount) * parseInt(quantity));
        setUpdateInProgress(false)
      })
    } else {
      variants.forEach(variant => {
        if (variant?.admin_graphql_api_id === event?.target?.value) {
          setPrice(variant?.price);
          setTotalAmount(parseFloat(variant?.price) * parseInt(quantity));
          setUpdateInProgress(false)
        }
      });
    }
    setSelectedVariant(event?.target?.value);
  };

  const updateProduct = () => {
    if (!isPriceInputInValid && !isQuantityInputInValid) {
      setUpdateInProgress(true);
      const requestUrl = `api/subscription-contracts-update-line-item-quantity?contractId=${contractId}&lineId=${lineId}&quantity=${quantity}&variantId=${selectedVariant}&isExternal=true`;
      axios
        .put(requestUrl)
        .then(res => {
          getCustomerPortalEntity(contractId);
          setUpdateInProgress(false);
          toast.success('Contract Updated', options);
          setEditMode(!editMode);
        })
        .catch(err => {
          console.log('Error in updating product', err);
          setUpdateInProgress(false);
          toast.error(err.response.data.message, options);
        });
    }
  };

  const updateVariant = () => {
    setUpdateInProgress(true);
    if (selectedVariant !== variantId) {
      axios.put(
        `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&price=${price}&quantity=${quantity}&variantId=${selectedVariant}&isExternal=true`
      )
      .then(data => {
        const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true`;
        axios.put(requestUrl).then(res => {
            getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            toast.success('Contract Updated', options);
            setEditMode(false);
            setEditVariantMode(false);
        })
        .catch(err => {
          setUpdateInProgress(false);
          setEditMode(false);
          setEditVariantMode(false);
          toast.error('Contract Update Failed', options);
        })
      })
    } else {
      setUpdateInProgress(false);
    }
  }

  const deleteProduct = () => {
    setDeleteInProgress(true);
    setHandleAlert({ show: false, data: null });
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${lineId}&isExternal=true`;
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
  }

  return (
    <>
      {/* <SweetAlert
        show={handleAlert.show}
        title="Are you sure?"
        text="You are about to delete this product."
        showCancelButton
        cancelButtonTextV2="No"
        confirmButtonText="Yes"
        onConfirm={() => {
          deleteProduct();
        }}
        onCancel={() => {
          setHandleAlert({ show: false, data: null });
        }}
      /> */}
      <Col md={2} className="appstle_edit_delete_button_wrapper" id={`popover-product-col${index}`}>
        { customerPortalSettingEntity?.editProductFlag && (
          <>
            {
              (!editMode && !attributeEdit) && (
              <>
                <Button
                  style={{ padding: '10px 12px' }}
                  className="appstle_order-detail_update-button d-flex align-items-center"
                  disabled ={subscriptionContractFreezeStatus}
                  onClick={() => setEditMode(!editMode)}
                >
                {
                  subscriptionContractFreezeStatus ?
                  <FontAwesomeIcon
                      icon={faLock}/>
                  :
                    <FontAwesomeIcon
                      icon={faPencilAlt}
                      onClick={() => {
                        setEditMode(true);
                      }}
                    />
                }

                </Button>
              </>
            )}
          </>
        )}
        {!subscriptionContractFreezeStatus && subscriptionEntities?.length > 1 && customerPortalSettingEntity?.deleteProductFlag && !editMode && (
          <>
          <Popover container={document.getElementById(`popover-product-col${index}`)} placement="bottom" isOpen={handleAlert.show} target={`popover-index${index}`} toggle={() => setHandleAlert({ show: false, data: null })}>
              <PopoverHeader style={{fontWeight: 'bold', color: '#eb3023'}}>{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || "Are you sure?"}</PopoverHeader>
              <PopoverBody className="d-flex justify-content-center align-items-center">
                  <Button
                      style={{ padding: '8px 10px',backgroundColor: 'white', color: '#eb3023', border: '1px solid #eb3023' }}
                      className="d-flex align-items-center mr-1"
                      onClick={deleteProduct}
                    >
                    <FontAwesomeIcon  icon={faCheck} />
                    </Button>
                    <Button
                      style={{ padding: '8px 10px' }}
                      className="appstle_order-detail_update-button d-flex align-items-center"
                      onClick={()=>setHandleAlert({ show: false, data: null })}
                    >
                    <FontAwesomeIcon  icon={faTimes} />
                    </Button>
              </PopoverBody>
            </Popover>
          {<Button
            style={{ padding: '10px 12px' }}
            id={`popover-index${index}`}
            className="ml-2 appstle_deleteButton d-flex align-items-center"
            onClick={() => {
              setHandleAlert({ show: true, data: null });
            }}
          >
            {!deleteInProgress && (
              <FontAwesomeIcon
                icon={faTrash}
              />
            )}
            {deleteInProgress && <div className="appstle_loadersmall" />}
          </Button>}
          </>
        )}
      </Col>
      <Col md={2}></Col>
      <Col md={10}>
      <AnimatePresence initial={false}>
      {editMode && (
        <motion.div
          key="content"
          initial="collapsed"
          animate="open"
          exit="collapsed"
          variants={{
            open: { opacity: 1, height: "auto" },
            collapsed: { opacity: 0, height: 0 }
          }}
          transition={{ duration: 0.8, ease: [0.04, 0.62, 0.23, 0.98] }}
        >
          {variants?.length > 1 && (
            <FormGroup style={{ marginBottom: '1rem' }}>
              <Label>{customerPortalSettingEntity?.changeVariantLabelText ? customerPortalSettingEntity?.changeVariantLabelText: "Change Variant" }</Label>
              <div className="d-flex" style={{alignItems: "center"}}>
              <Input
                value={selectedVariant}
                disabled={!editVariantMode}
                type="select"
                onChange={event => {
                  handleVariantChange(event);
                }}
                className="mtl-1"
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
              {!editVariantMode && <Button
                  style={{ padding: '10px 12px' }}
                  className="m-0 ml-2 appstle_order-detail_update-button d-flex align-items-center"
                  onClick={() => setEditVariantMode(!editVariantMode)}
                >
                  <FontAwesomeIcon
                    icon={faPencilAlt}
                    onClick={() => {
                      setEditVariantMode(!editVariantMode);
                    }}
                  />
                </Button>}
              </div>
            </FormGroup>
          )}
          {!editVariantMode && <FormGroup style={{ marginBottom: '1rem' }}>
            <Label>{customerPortalSettingEntity?.editQuantityLabelText}</Label>
            <Input
              value={quantity}
              invalid={isQuantityInputInValid}
              type="number"
              onInput={event => quantityInputChangeHandler(event.target.value)}
              onBlur={event => quantityInputBlurHandler(event.target.value)}
              onChange={event => quantityInputChangeHandler(event.target.value)}
            />
            <FormFeedback>Please enter valid number</FormFeedback>
          </FormGroup>}

          {/* <FormGroup>
            <Label>Price per Quantity</Label>
            <Input
              value={price}
              invalid={isPriceInputInValid}
              type="number"
              onInput={event => priceInputChangeHandler(event.target.value)}
              onBlur={event => priceInputBlurHandler(event.target.value)}
            />
            <FormFeedback>Please enter valid number</FormFeedback>
          </FormGroup> */}
         </motion.div>
        )}
      </AnimatePresence>
      <div className="d-flex appstle_edit_product_btn_grp align-items-center">
        {customerPortalSettingEntity?.editProductFlag && (
          <>
            {editVariantMode && (
              <>
                {/*<h5 class="appstle_subtotal_font_size">
                  {customerPortalSettingEntity?.subTotalLabelTextV2}: {totalAmount || '0'}
                  {' '}
                  {totalProductPriceObj?.currencyCode}
                </h5>*/}
                <Button disabled={updateInProgress} className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => updateVariant()}>
                  {!updateInProgress && customerPortalSettingEntity?.updateButtonText}
                  {updateInProgress && <div className="appstle_loadersmall" />}
                </Button>
              </>
            )}

            {editVariantMode && (
              <Button disabled={updateInProgress} className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center" onClick={() => resetVariant()}>
                {customerPortalSettingEntity?.cancelButtonTextV2}
              </Button>
            )}
          </>
        )}
      </div>
      <div className="d-flex appstle_edit_product_btn_grp align-items-center">
        {customerPortalSettingEntity?.editProductFlag && (
          <>
            {editMode && !editVariantMode && (
              <>
                {/*<h5 class="appstle_subtotal_font_size">
                  {customerPortalSettingEntity?.subTotalLabelTextV2}: {totalAmount || '0'}
                  {' '}
                  {totalProductPriceObj?.currencyCode}
                </h5>*/}
                <Button className="appstle_order-detail_update-button d-flex align-items-center" onClick={() => updateProduct()}>
                  {!updateInProgress && customerPortalSettingEntity?.updateButtonText}
                  {updateInProgress && <div className="appstle_loadersmall" />}
                </Button>
              </>
            )}

            {editMode && !editVariantMode && (
              <Button className="ml-2 appstle_order-detail_cancel-button d-flex align-items-center" onClick={() => setEditMode(!editMode)}>
                {customerPortalSettingEntity?.cancelButtonTextV2}
              </Button>
            )}
          </>
        )}
      </div>
      </Col>
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

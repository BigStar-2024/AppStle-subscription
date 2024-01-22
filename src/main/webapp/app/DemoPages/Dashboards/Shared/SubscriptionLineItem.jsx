import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import axios from 'axios';
import {
  Button,
  Col,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  ListGroupItem,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row,
  Table
} from 'reactstrap';

import {
  applyDiscount,
  getEntity,
  removeDiscount,
  updateBillingDate,
  updateBillingInterval,
  updateDeliveryInterval,
  updateDeliveryMethodName,
  updateDeliveryPrice,
  updateMaxCycles,
  updateMinCycles,
  updatePaymentDetail,
  updateShippingAddress
} from 'app/entities/subscriptions/subscription.reducer';
import {
  getFailedOrderEntity,
  getUpcomingOrderEntity,
  skipBillingOrder,
  updateAttemptBillingEntity
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import {toast} from 'react-toastify';
import EditContractDetail from './EditContractDetail';
import {
  getSubscriptionContractDetailsByContractId,
  getUpcomingOrderEntityList,
  updateEntity
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import {Delete, Info} from '@mui/icons-material';

import {getEntity as getPortalSettings} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {getSellingPlans} from 'app/entities/subscription-group/subscription-group.reducer'

import AddCustomAttributes from './AddCustomAttributes';
import CustomHtmlToolTip from '../SubscriptionGroups/CustomHtmlToolTip';
// import SendEmailPopup from 'app/DemoPages/Dashboards/CommonComponents/Popup/SendEmailPopup';
import LoyaltyDetails from "./LoyaltyDetails";
import './MySubscriptionDetail.scss'
import MySaveButton from "../Utilities/MySaveButton";
import {Field, Form, FormSpy} from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import {FieldArray} from 'react-final-form-arrays';
import {OnChange} from 'react-final-form-listeners';
import {parseInt} from 'lodash';
import {getEntity as getSubscriptionBundling} from "app/entities/subscription-bundling/subscription-bundling.reducer";
import { isOneTimeProduct, isFreeProduct } from 'app/shared/util/subscription-utils';

const SubscriptionLineItem = ({
                                subUpcomingOrderEntities,
                                updateShippingAddress,
                                updatingShippingAddress,
                                subscriptionEntities,
                                subscriptionContractDetailsEntity,
                                cancelSubscription,
                                updateBillingInterval,
                                updateDeliveryInterval,
                                updateBillingDate,
                                updatingDeliveryPrice,
                                skipBillingOrder,
                                updatePaymentDetail,
                                removeBuffer,
                                updating,
                                getUpcomingOrderEntity,
                                updateAttemptBillingEntity,
                                updateDeliveryPrice,
                                shopName,
                                customerId,
                                contractId,
                                getEntity,
                                getSubscriptionContractDetailsByContractId,
                                getFailedOrderEntity,
                                subFailedOrderEntities,
                                shopInfo,
                                billingAttempted,
                                updateEntity,
                                updatingSubscriptionContractDetails,
                                updateMaxCycles,
                                skippedBillingAttemptFlag,
                                updateMinCycles,
                                updatingMinCycles,
                                updatingMaxCycles,
                                removeDiscount,
                                removeDiscountInProgress,
                                applyDiscount,
                                getPortalSettings,
                                customerPortalSettingEntity,
                                getUpcomingOrderEntityList,
                                upcomingOrderDetailsEntity,
                                updateDeliveryMethodName,
                                updatingDeliveryMethodName,
                                failedOrderLoading,
                                totalFailedBillingAttemptItems,
                                getSellingPlans,
                                sellingPlanData,
                                prd,
                                index,
                                currentCycle,
                                currentCycleLoaded,
                                getSubscriptionBundling,
                                subscriptionBundling,
                                recurringProductsInSubscription,
                                freeProductsInSubscription,
                                oneTimeProductsInSubscription,
                                ...props
                              }) => {


  const [isPrepaid, setIsPrepaid] = useState(
    subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true
  );

  const [globalDiscounts, setGlobalDiscounts] = useState([]);

  let [quantity, setQuantity] = useState('');
  let [quantityInputBlurred, setQuantityInputBlurred] = useState(false);
  let [isQuantityInputInValid, setIsQuantityInputInValid] = useState(false);
  let [isEditQuantity, setIsEditQuantity] = useState(false);
  let [isEditQuantityInProgress, setIsEditQuantityInProgress] = useState(false);

  let [sellingPlanNameValue, setSellingPlanNameValue] = useState('');
  let [isEditSellingPlan, setIsEditSellingPlan] = useState(false);
  let [isEditSellingPlanInProgress, setIsEditSellingPlanInProgress] = useState(false);

  let [isEditPricingPolicy, setIsEditPricingPolicy] = useState(false);
  let [isEditPricingPolicyInProgress, setIsEditPricingPolicyInProgress] = useState(false);

  let [editMode, setEditMode] = useState(false);
  let [variants, setVariants] = useState([]);
  let [selectedVariant, setSelectedVariant] = useState('');
  let [updateInProgress, setUpdateInProgress] = useState(false);
  let [deleteInProgress, setDeleteInProgress] = useState(false);
  let [price, setPrice] = useState('');
  let [priceInputBlurred, setPriceInputBlurred] = useState(false);
  let [isPriceInputInValid, setIsPriceInputInValid] = useState(false);

  let [editVariantMode, setEditVariantMode] = useState(false);

  let [pricingPolicy, setPricingPolicy] = useState({});
  let [fulfilments, setFulfilments] = useState(1);
  let [currentPrice, setCurrentPrice] = useState(0);
  let [basePricePerUnit, setBasePricePerUnit] = useState(0);

  useEffect(() => {
    setQuantity(prd?.node?.quantity);
    setPrice(parseFloat(prd?.node?.currentPrice?.amount).toFixed(2));
    setSellingPlanNameValue(prd?.node?.sellingPlanId);
  }, [prd])

  useEffect(() => {
    getSellingPlans()
  }, [])

  useEffect(() => {
    extractGlobalDiscounts();
    let checkPrepaid = subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true;
    setIsPrepaid(checkPrepaid);
  }, [subscriptionEntities]);

  const saveQuantity = () => {
    if (!isQuantityInputInValid) {
      setIsEditQuantityInProgress(true)
      axios.put(`/api/subscription-contracts-update-line-item-quantity?contractId=${contractId}&lineId=${prd?.node?.id}&quantity=${quantity}`)
        .then(res => {
          return getEntity(contractId);
        }).then(res => {
        setIsEditQuantityInProgress(false);
        setIsEditQuantity(false);
        toast.success("Quantity Updated", options)
      }).catch(err => {
        setIsEditQuantityInProgress(false)
        toast.error("Quantity Updated failed", options)
      })
    }
  }

  useEffect(() => {
    if (prd && quantity && subscriptionEntities && currentCycleLoaded) {
      let fulfilments = (subscriptionEntities?.billingPolicy?.intervalCount / subscriptionEntities?.deliveryPolicy?.intervalCount) || 1
      let currentPrice = parseFloat(prd?.node?.currentPrice?.amount / fulfilments).toFixed(2);
      let basePricePerUnit = parseFloat((prd?.node?.pricingPolicy?.basePrice?.amount) || currentPrice).toFixed(2);

      let currentBillingCycle = currentCycle;

      let formData = {
        basePricePerUnit: (basePricePerUnit),
        currentPrice: currentPrice,
        currentBillingCycle: currentBillingCycle,
        totalBasePrice: parseFloat(basePricePerUnit * prd?.node?.quantity * fulfilments).toFixed(2),
        totalCurrentPrice: parseFloat(currentPrice * prd?.node?.quantity * fulfilments).toFixed(2),
        fulfilments: fulfilments,
        quantity: quantity,
        appstleCycles: []
      };

      prd?.node?.pricingPolicy?.cycleDiscounts?.forEach(item => {
        formData.appstleCycles.push(
          {
            afterCycle: item?.afterCycle,
            discountType: item?.adjustmentType === "FIXED_AMOUNT" ? "FIXED" : item?.adjustmentType,
            value: item?.adjustmentType === "PERCENTAGE" ? item?.adjustmentValue?.percentage : parseFloat(item?.adjustmentValue?.amount).toFixed(2)
          }
        )
      })
      setFulfilments(fulfilments);
      setCurrentPrice(currentPrice);
      setBasePricePerUnit(basePricePerUnit);
      setPricingPolicy(formData)
    }

  }, [prd, currentCycle, subscriptionEntities, isPrepaid, quantity, currentCycleLoaded])

  useEffect(() => {

  }, [isPrepaid])

  const saveSellingPlan = () => {
    setIsEditSellingPlanInProgress(true)
    let sellingPlanid = sellingPlanNameValue.split("/").pop();
    let sellingPlanName = (sellingPlanData.find(item => item?.id === sellingPlanNameValue))?.frequencyName;
    console.log(sellingPlanName);
    axios.put(`/api/subscription-contracts-update-line-item-selling-plan?contractId=${contractId}&lineId=${prd?.node?.id}&sellingPlanId=${sellingPlanid}&sellingPlanName=${sellingPlanName}`)
      .then(res => {
        return getEntity(contractId);
      }).then(res => {
      setIsEditSellingPlanInProgress(false);
      setIsEditSellingPlan(false);
      toast.success("Selling Plan updated", options)
    }).catch(err => {
      setIsEditSellingPlanInProgress(false)
      toast.error("Selling Plan updated failed", options)
    })
  }

  const quantityInputBlurHandler = value => {
    setQuantity(value);
    setQuantityInputBlurred(true);
    if (validateNumber(value)) {
      setIsQuantityInputInValid(true);
    } else {
      setIsQuantityInputInValid(false);
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

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const extractGlobalDiscounts = () => {
    if (subscriptionEntities?.discounts?.edges?.length > 0) {
      let globalDiscountArray = []
      subscriptionEntities?.discounts?.edges?.forEach(discountNode => {
        let isCurrentDiscountNodeGlobal = true;
        subscriptionEntities?.lines?.edges?.forEach(productNode => {
          let productNodeDiscountArray = [];
          productNode?.node?.discountAllocations?.forEach(productDiscount => {
            productNodeDiscountArray?.push(productDiscount?.discount?.id)
          });
          if ((productNodeDiscountArray?.length) && (productNodeDiscountArray?.indexOf(discountNode?.node?.id) === -1)) {
            isCurrentDiscountNodeGlobal = false;
          }
        });
        if (isCurrentDiscountNodeGlobal) {
          globalDiscountArray?.push(JSON.parse(JSON.stringify(discountNode)))
        }
      })
      setGlobalDiscounts([...globalDiscountArray]);
    } else {
      setGlobalDiscounts([]);
      return;
    }
  }

  const getDiscountCodeFromID = (subscriptionEntities, prdDiscountNode) => {
    let discountData = subscriptionEntities?.discounts?.edges?.find(discountNode => {
      return discountNode?.node?.id === prdDiscountNode?.discount?.id;
    })
    return "Code:" + discountData?.node?.title
  }

  const saveEntity = values => {
    console.log(values)
    setIsEditPricingPolicyInProgress(true);
    axios.put(`/api/subscription-contracts-update-line-item-pricing-policy?basePrice=${values?.basePricePerUnit}&contractId=${contractId}&lineId=${prd?.node?.id}`, values?.appstleCycles).then(res => {
      return getEntity(contractId);
    })
      .then(res => {
        toast.success('Pricing Policy updated', options);
        setIsEditPricingPolicyInProgress(false);
        setIsEditPricingPolicy(false);
      })
      .catch(err => {
        setIsEditPricingPolicyInProgress(false);
        toast.error(err?.response?.data?.message, options);
      })
  };

  const getCurrentPrice = (values) => {
    if (values?.appstleCycles && values?.appstleCycles?.length) {
      let currentEligiblePricingPolicy = null;
      let reversedAppstleCycle = JSON.parse(JSON.stringify([...values.appstleCycles])).reverse();
      let currentPrice = 0;
      for (var i = 0; i < reversedAppstleCycle.length; i++) {
        if (values?.currentBillingCycle >= reversedAppstleCycle[i]?.afterCycle) {
          currentEligiblePricingPolicy = reversedAppstleCycle[i];
          break;
        }
      }
      if (currentEligiblePricingPolicy) {
        if (currentEligiblePricingPolicy?.discountType === "PERCENTAGE") {
          currentPrice = parseFloat(parseFloat(values?.basePricePerUnit) * (1 - (parseFloat(currentEligiblePricingPolicy?.value) / 100))).toFixed(2);
        } else if (currentEligiblePricingPolicy?.discountType === "FIXED") {
          currentPrice = parseFloat(parseFloat(values?.basePricePerUnit) - (parseFloat(isPrepaid ? parseFloat(currentEligiblePricingPolicy?.value / fulfilments).toFixed(2) : currentEligiblePricingPolicy?.value))).toFixed(2);
        } else if (currentEligiblePricingPolicy?.discountType === "PRICE") {
          currentPrice = parseFloat(parseFloat(isPrepaid ? parseFloat(currentEligiblePricingPolicy?.value / fulfilments).toFixed(2) : currentEligiblePricingPolicy?.value)).toFixed(2);
        }
      } else {
        currentPrice = values?.basePricePerUnit;
      }
      return currentPrice;
    }
  }

  const getTenseBasedOnCurrentCycle = (currentCycle, afterCycle) => {
    if (currentCycle > afterCycle) {
      return "was";
    } else if (currentCycle <= afterCycle) {
      return "will be"
    }
  }

  let submit;
  let addLine;
  return (
    <>
      <ListGroupItem
        className="mb-3"
        key={`1${index}`}
        style={{
          backgroundColor: prd?.node?.productId == null ? 'beige' : '',
          padding: '0.75rem'
        }}
      >
        <div className="widget-content p-0">
          <div className="widget-content-wrapper" style={{alignItems: 'stretch'}}>
            {prd?.node?.productId != null ? (
              <Fragment>
                <div className="widget-content-left">
                  <img
                    src={prd?.node?.variantImage?.transformedSrc}
                    width={100}
                    height={100}
                    style={{borderRadius: '2px', objectFit: 'contain'}}
                  />
                </div>
                <div
                  className="widget-content-left ml-3 d-flex align-items-start"
                  style={{flexDirection: 'column'}}
                >
                  <div className="widget-heading">
                    <h6 className="mb-2 product-title">
                      <a
                        href={`https://${shopName}/admin/products/${prd?.node?.productId?.split('/')[4]}`}
                        target="_blank"
                      >
                        {prd.node.title} {(prd?.node?.variantTitle && prd?.node?.variantTitle !== "-" && prd?.node?.variantTitle !== "Default Title") && ('-' + prd?.node.variantTitle)}
                      </a>
                    </h6>
                  </div>
                  {/*<span>
                <b>Selling Plan:</b>
                <div style={{display: "inline-flex"}}>
                  <span className="ml-2">{prd?.node?.sellingPlanId ? '('+prd?.node?.sellingPlanId?.replace('gid://shopify/SellingPlan/','')+') ' : ''}{prd?.node?.sellingPlanName || 'NA'}&nbsp;</span>
                  <div className="ml-2">
                    {!isEditSellingPlan && <a style={{color: "#545cd8", cursor: "pointer"}} onClick={() => setIsEditSellingPlan(true)}> <i class="lnr lnr-pencil btn-icon-wrapper"></i></a>}
                  </div>
                </div>
              </span>*/}
                  <span>
                <b className={"product-quantity"}>Quantity:</b>
                <div style={{display: "inline-flex"}}>
                  <span className="ml-2">{prd?.node?.quantity}&nbsp;</span>
                  <div className="ml-2">
                    {!isEditQuantity &&
                      <a style={{color: "#545cd8", cursor: "pointer"}} onClick={() => setIsEditQuantity(true)}> <i
                        class="lnr lnr-pencil btn-icon-wrapper"/></a>}
                  </div>
                </div>
              </span>
              <span>
                <b className='product-sku'>SKU: </b>
                <span>{prd?.node?.sku || ''}</span>
              </span>
                  {isPrepaid && <span>
                <b className={"fullfillments-delivery"}>Fulfilments (Deliveries):</b>
                <div style={{display: "inline-flex"}}>
                  <span
                    className="ml-2">{(subscriptionEntities?.billingPolicy?.intervalCount / subscriptionEntities?.deliveryPolicy?.intervalCount) || 1}</span>
                </div>
              </span>}
                  {isPrepaid && <span>
                <b className={"price-unit"}>Price / unit / delivery:</b>
                <div style={{display: "inline-flex"}}>
                  <span className="ml-2">{(currentPrice)} &nbsp;{prd.node?.currentPrice?.currencyCode}</span>
                  {prd.node?.pricingPolicy?.basePrice?.amount > currentPrice && <span className="ml-2"
                                                                                      style={{textDecoration: "line-through"}}>{prd.node?.pricingPolicy?.basePrice.amount} &nbsp;{prd.node?.pricingPolicy?.basePrice?.currencyCode}</span>}
                </div>
              </span>}
                  <div className="mt-2 d-flex align-items-center w-100">
                    <b>{isPrepaid ? "Total Amount*: " : "Total: "}</b>
                    <div className="badge  badge-alternate ml-2"
                         style={{padding: '5px 6px', fontSize: '12px'}}>
                      {parseFloat(prd.node.lineDiscountedPrice.amount).toFixed(2)} {prd.node.lineDiscountedPrice.currencyCode}
                    </div>
                    {(prd.node?.pricingPolicy && (parseInt(prd.node.lineDiscountedPrice.amount) !== parseInt(prd.node?.pricingPolicy?.basePrice?.amount))) ?
                      <div className="ml-2" style={{textDecoration: "line-through"}}>
                        {parseFloat(basePricePerUnit * parseInt(prd?.node?.quantity) * (fulfilments)).toFixed(2)}&nbsp;{prd.node?.currentPrice?.currencyCode}
                      </div> : ""}
                    <div className="ml-2">
                      {!isEditPricingPolicy &&
                        <a style={{color: "#545cd8", cursor: "pointer"}} onClick={() => setIsEditPricingPolicy(true)}>
                          <i class="lnr lnr-pencil btn-icon-wrapper"></i></a>}
                    </div>
                    {
                      prd?.node?.discountAllocations?.length > 0 && <CustomHtmlToolTip
                        interactive
                        placement="right"
                        arrow
                        enterDelay={0}
                        title={
                          <div style={{padding: '8px'}}>
                            <Table
                              borderless
                              responsive
                              size="sm"
                              style={{color: '#fff'}}
                            >
                              <tbody>
                              <tr>
                                <td>
                                  (Actual Price)
                                </td>
                                <td style={{textAlign: 'right', width: '200px'}}>
                                  {parseFloat(prd?.node?.currentPrice?.amount * quantity).toFixed(2)}
                                </td>
                                <td>
                                  {prd?.node?.currentPrice?.currencyCode}
                                </td>
                              </tr>
                              {
                                prd?.node?.discountAllocations?.map(data =>

                                  <tr>
                                    <td>
                                      (Discount)
                                      <div className='d-flex'
                                           style={{fontSize: '10px'}}>{getDiscountCodeFromID(subscriptionEntities, data)}</div>
                                    </td>
                                    <td style={{textAlign: 'right', color: 'red', width: '200px'}}>
                                      <div> - {data?.amount?.amount}</div>
                                    </td>
                                    <td>
                                      {data?.amount?.currencyCode}
                                    </td>
                                  </tr>
                                )
                              }
                              <tr style={{border: '1px solid #fff'}}>
                                <td>
                                  Total Price
                                </td>
                                <td style={{textAlign: 'right'}}>
                                  {parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)}
                                </td>
                                <td>
                                  {prd?.node?.lineDiscountedPrice?.currencyCode}
                                </td>
                              </tr>
                              </tbody>
                            </Table>
                            <div className='d-flex'>
                              {/* <div className="text-center">
                          &nbsp;&nbsp;&nbsp;&nbsp;
                          </div>
                          <div className="text-right">
                          {prd?.node?.currentPrice?.amount} {prd?.node?.currentPrice?.currencyCode} (Actual Price)
                          </div> */}
                            </div>
                            {/* {
                          prd?.node?.discountAllocations?.map(data =>
                            <div className='d-flex'>
                            <div className="text-center">
                            &nbsp; - &nbsp;
                            </div>
                            <div className="text-right">
                            {data?.amount?.amount} {data?.amount?.currencyCode} (Discount)
                            </div>
                          </div>
                            )
                        } */}
                            {/* <div style={{borderTop: '1px solid black', width: '100%'}}>
                        &nbsp;&nbsp;&nbsp;&nbsp;{prd?.node?.lineDiscountedPrice?.amount} {prd?.node?.lineDiscountedPrice?.currencyCode} ( Total Price)
                          </div> */}
                          </div>
                        }
                      >
                        <Info style={{fontSize: '1rem'}}/>
                      </CustomHtmlToolTip>
                    }
                  </div>
                  {isPrepaid && <small>*price includes all deliveries.</small>}
                  {prd.node?.pricingPolicy && prd.node.pricingPolicy?.cycleDiscounts?.length && (
                    <div>
                      <b>About Discount: </b>
                      <p style={{padding: '5px 0px', fontSize: '13px'}}>
                        {(prd.node.pricingPolicy.cycleDiscounts[0]?.afterCycle > 0) ? `After ${prd.node.pricingPolicy.cycleDiscounts[0]?.afterCycle} billing cycle(s) the price` : "Initial price"} of
                        the
                        product {getTenseBasedOnCurrentCycle(currentCycle, prd.node.pricingPolicy.cycleDiscounts[0]?.afterCycle)} &nbsp;
                        {parseFloat(prd.node.pricingPolicy?.cycleDiscounts[0]?.computedPrice.amount) +
                          ' ' +
                          prd.node.pricingPolicy.cycleDiscounts[0]?.computedPrice.currencyCode}
                        &nbsp;
                        <span
                          className="badge"
                          style={{backgroundColor: '#9f5858', color: '#FFF', fontSize: '10px'}}
                        >
                          {prd?.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.amount
                            ? parseFloat(prd.node.pricingPolicy.cycleDiscounts[0]?.adjustmentValue.amount) +
                            ' ' +
                            prd.node.pricingPolicy.cycleDiscounts[0]?.adjustmentValue?.currencyCode
                            : prd.node.pricingPolicy.cycleDiscounts[0]?.adjustmentValue.percentage + '%'}{' '}
                          {prd?.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentType == 'PRICE' ? '' : 'off'}
                        </span>
                        {prd.node.pricingPolicy.cycleDiscounts[1] &&
                          <>
                            <br/>
                            <span>
                            After {prd.node.pricingPolicy.cycleDiscounts[1]?.afterCycle} cycle(s) the
                            product price {getTenseBasedOnCurrentCycle(currentCycle, prd.node.pricingPolicy.cycleDiscounts[1]?.afterCycle)}
                              &nbsp;
                              {parseFloat(prd.node.pricingPolicy.cycleDiscounts[1]?.computedPrice.amount) +
                                ' ' +
                                prd.node.pricingPolicy.cycleDiscounts[1]?.computedPrice.currencyCode}
                              &nbsp;
                              <span
                                className="badge"
                                style={{backgroundColor: '#9f5858', color: '#FFF', fontSize: '10px'}}
                              >
                              {prd?.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.amount
                                ? parseFloat(prd.node.pricingPolicy.cycleDiscounts[1]?.adjustmentValue.amount) +
                                ' ' +
                                prd.node.pricingPolicy.cycleDiscounts[1]?.adjustmentValue?.currencyCode
                                : prd.node.pricingPolicy.cycleDiscounts[1]?.adjustmentValue.percentage + '%'}{' '}
                              {prd?.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentType == 'PRICE' ? '' : 'off'}
                            </span>
                          </span>
                          </>
                        }
                        {
                          (prd?.node?.sellingPlanId) &&
                          <LoyaltyDetails sellingPlan={prd?.node?.sellingPlanId} sellingPlanData={sellingPlanData}
                                          currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}
                                          text="view more."/>
                        }
                      </p>
                    </div>
                  )}
                  {isOneTimeProduct(prd) && <span className='badge badge-pill badge-info mt-3'>One Time Product</span>}
                  {isFreeProduct(prd) && <span className='badge badge-pill badge-info mt-3'>Free Product</span>}
                </div>
              </Fragment>
            ) : (
              <Fragment>
                <div className="widget-heading" style={{backgroundColor: 'beige'}}>
                  <h6>
                    <b>
                      {prd.node.title} {prd?.node.variantTitle && '-' + prd?.node.variantTitle}
                    </b>{' '}
                    isn't present in Shopify anymore.
                  </h6>
                </div>
              </Fragment>
            )}
          </div>
          {
            prd?.node?.discountAllocations?.map(prdDiscountNode => {
              let finalnode = null;
              let isDiscountGlobal = globalDiscounts?.some((discount => {
                return prdDiscountNode?.discount?.id === discount?.node?.id;
              }))

              if (!isDiscountGlobal) {
                finalnode = subscriptionEntities?.discounts?.edges?.find(discountNode => {
                  return discountNode?.node?.id === prdDiscountNode?.discount?.id;
                })
              }

              if (finalnode) {
                return (<p className="mt-2" style={{wordBreak: "break-all"}}>
                  <b>Discount Coupon
                    Applied: </b>{finalnode?.node?.title} @ {finalnode?.node?.value?.percentage || finalnode?.node?.value?.amount?.amount}{finalnode?.node?.value?.percentage ? '% ' : finalnode?.node?.value?.amount?.currencyCode}
                  &nbsp; {!removeDiscountInProgress &&
                  <span style={{color: "#13b5ea", textDecoration: "underline", cursor: "pointer"}}
                        onClick={() => removeDiscount(contractId, finalnode?.node?.id, shopName)}>delete</span>} {removeDiscountInProgress &&
                  <span style={{display: "inline-block"}} className="appstle_loadersmall"/>}
                </p>)
              }

            })
          }
          <EditContractDetail
            contractId={contractId}
            lineId={prd?.node?.id}
            shop={shopName}
            productQuantity={prd?.node?.quantity}
            productId={prd?.node?.productId}
            variantId={prd?.node?.variantId}
            sellingPlanName={prd?.node?.sellingPlanName}
            subscriptionEntities={subscriptionEntities?.lines?.edges}
            productPrice={prd?.node?.currentPrice?.amount}
            manualCopounDiscountEnabled={subscriptionEntities?.discounts?.edges?.length > 0}
            isPrepaid={isPrepaid}
            currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}
            recurringProductsInSubscription={recurringProductsInSubscription}
            freeProductsInSubscription={freeProductsInSubscription}
            oneTimeProductsInSubscription={oneTimeProductsInSubscription}
            prd={prd}
          />
          <div className="d-flex">
            {
              (!prd.node?.pricingPolicy && prd?.node?.sellingPlanId) &&
              <LoyaltyDetails sellingPlan={prd?.node?.sellingPlanId} sellingPlanData={sellingPlanData}
                              currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}/>
            }
            <AddCustomAttributes
              contractId={contractId}
              lineId={prd?.node?.id}
              shop={shopName}
              currentVariant={prd?.node}
            />
          </div>
        </div>

      </ListGroupItem>
      <Modal isOpen={isEditQuantity} toggle={() => setIsEditQuantity(old => !old)}>
        <ModalHeader>
          Edit Quantity
        </ModalHeader>
        <ModalBody>
          <FormGroup>
            <Label>Change Quantity</Label>
            <Input
              value={quantity}
              invalid={isQuantityInputInValid}
              type="number"
              onInput={event => quantityInputChangeHandler(event.target.value)}
              onBlur={event => quantityInputBlurHandler(event.target.value)}
            />
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          <Button color="link" onClick={() => setIsEditQuantity(old => !old)}>Cancel</Button>
          <MySaveButton onClick={() => saveQuantity()} updating={isEditQuantityInProgress}/>
        </ModalFooter>
      </Modal>

      <Modal isOpen={isEditSellingPlan} toggle={() => setIsEditSellingPlan(old => !old)}>
        <ModalHeader>
          Edit Selling Plan
        </ModalHeader>
        <ModalBody>
          <FormGroup>
            <Label>Change Selling Plan</Label>
            <Input
              value={sellingPlanNameValue}
              invalid={isQuantityInputInValid}
              type="select"
              onChange={event => setSellingPlanNameValue(event.target.value)}
            >
              <option value="">Please Select</option>
              {sellingPlanData?.map(item => {
                return <option value={item?.id}>{item?.frequencyName}&nbsp;({item?.id.split("/").pop()})</option>
              })}
            </Input>
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          <Button color="link" onClick={() => setIsEditSellingPlan(old => !old)}>Cancel</Button>
          <MySaveButton onClick={() => saveSellingPlan()} disabled={!sellingPlanNameValue}
                        updating={isEditSellingPlanInProgress}/>
        </ModalFooter>
      </Modal>

      <Modal isOpen={isEditPricingPolicy} toggle={() => setIsEditPricingPolicy(old => !old)} size="lg">
        <ModalHeader>
          Edit Pricing Policy
        </ModalHeader>
        <ModalBody>
          <Form
            mutators={{
              ...arrayMutators,
              setValue: ([field, value], state, {changeValue}) => {
                changeValue(state, field, () => value)
              }
            }}
            initialValues={pricingPolicy}
            onSubmit={saveEntity}
            render={({
                       handleSubmit,
                       form: {
                         mutators: {push, pop, update, remove}
                       },
                       form,
                       submitting,
                       pristine,
                       values,
                       errors,
                       valid
                     }) => {
              submit = () => {
                if (Object.keys(errors).length === 0 && errors.constructor === Object) {
                  handleSubmit();
                } else {
                  if (Object.keys(errors).length) handleSubmit();
                  setFormErrors(errors);
                  setErrorsVisibilityToggle(!errorsVisibilityToggle);
                }
              }
              return (
                <>
                  <form onSubmit={handleSubmit}>
                    <Row>
                      <Col md={isPrepaid ? 4 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                {values?.appstleCycles?.length ? "Discounted Price per Quantity" : "Current Price per Quantity"}
                              </Label>
                              <InputGroup className="currentPrice">
                                <InputGroupAddon addonType='prepend'>{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                <Input {...input} disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>

                            </FormGroup>
                          )}
                          id={`currentPrice`}
                          className="form-control"
                          type="number"
                          name={`currentPrice`}
                        />
                      </Col>
                      <Col md={isPrepaid ? 2 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Quantity
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`quantity`}
                          className="form-control"
                          type="number"
                          name={`quantity`}
                        />
                      </Col>
                      {isPrepaid && <Col md={isPrepaid ? 2 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Fulfilments
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`fulfilments`}
                          className="form-control"
                          type="number"
                          name={`fulfilments`}
                        />
                      </Col>}
                      <Col md={isPrepaid ? 4 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                {values?.appstleCycles?.length ? "Discounted Line Price" : "Current Line Price"}
                              </Label>
                              <InputGroup className="totalCurrentPrice">
                                <InputGroupAddon addonType='prepend'>{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`totalCurrentPrice`}
                          className="form-control"
                          type="number"
                          name={`totalCurrentPrice`}
                        />
                      </Col>
                      <Col md={12}>
                        <FormText>Note: Current price will be automatically be calculated based upon the <b>base
                          price</b>, <b>current billing cycle</b> and the <b>values defined in the pricing policy</b>.
                          This subscription has completed <span
                            style={{color: "green"}}>{values?.currentBillingCycle} billing cycle(s).</span>
                        </FormText>
                        <br/>
                      </Col>
                    </Row>
                    <Row>
                      <Col md={isPrepaid ? 4 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Base Price per Quantity
                              </Label>
                              <InputGroup className="basePricePerUnit">
                                <InputGroupAddon type="prepend">{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                <Input {...input}
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`basePricePerUnit`}
                          className="form-control"
                          type="number"
                          name={`basePricePerUnit`}
                        />

                      </Col>
                      <Col md={isPrepaid ? 2 : 4} style={{display: "none"}}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Current Billing Cycle
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <InputGroupAddon addonType='prepend'>#</InputGroupAddon>
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`currentBillingCycle`}
                          className="form-control"
                          type="number"
                          name={`currentBillingCycle`}
                        />
                      </Col>
                      <Col md={isPrepaid ? 2 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Quantity
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`quantity`}
                          className="form-control"
                          type="number"
                          name={`quantity`}
                        />
                      </Col>
                      {isPrepaid && <Col md={isPrepaid ? 2 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Fulfilments
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`fulfilments`}
                          className="form-control"
                          type="number"
                          name={`fulfilments`}
                        />
                      </Col>}
                      <Col md={isPrepaid ? 4 : 4}>
                        <Field
                          render={({input, meta}) => (
                            <FormGroup>
                              <Label>
                                Total Base Price
                              </Label>
                              <InputGroup className="currentBillingCycle">
                                <InputGroupAddon addonType='prepend'>{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                <Input {...input}
                                       disabled
                                       invalid={meta.error && meta.touched ? true : null}/>
                                {meta.error && (
                                  <div
                                    class="invalid-feedback"
                                    style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                  >
                                    {meta.error}
                                  </div>
                                )}
                              </InputGroup>
                            </FormGroup>
                          )}
                          id={`totalBasePrice`}
                          className="form-control"
                          type="number"
                          name={`totalBasePrice`}
                        />
                      </Col>
                      <OnChange name={`basePricePerUnit`}>
                        {(value, previous) => {
                          form.mutators.setValue("totalBasePrice", parseFloat(values?.basePricePerUnit * quantity * fulfilments).toFixed(2));
                        }}
                      </OnChange>
                      <OnChange name={`currentPrice`}>
                        {(value, previous) => {
                          form.mutators.setValue("totalCurrentPrice", parseFloat(values?.currentPrice * quantity * fulfilments).toFixed(2));
                        }}
                      </OnChange>
                    </Row>
                    <FieldArray name={`appstleCycles`}>
                      {({fields}) => {
                        addLine = fields.push;
                        return fields.map((name, index) => (
                            <>
                              <div style={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                marginTop: "20px"
                              }}>
                                <h6 style={{margin: "0"}}>
                                  Pricing Policy {index + 1}
                                </h6>
                              </div>
                              <hr/>
                              <Row>
                                <Col md={3} sm={3} xs={8}>
                                  <Fragment>
                                    <Label for={`${name}.afterCycle`}>After Billing Cycle(s)</Label>
                                    <FormGroup style={{display: 'flex'}}>
                                      <Field
                                        render={({input, meta}) => (
                                          <InputGroup>
                                            <Input {...input}

                                                   invalid={meta.error && meta.touched ? true : null}/>
                                            {meta.error && (
                                              <div
                                                class="invalid-feedback"
                                                style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                              >
                                                {meta.error}
                                              </div>
                                            )}
                                            {/* <InputGroupAddon addonType="append">%</InputGroupAddon> */}
                                          </InputGroup>
                                        )}
                                        validate={(value, allValues, meta) => {
                                          let indx = parseInt((meta.name.split(".")[0]).match('\\d+')[0]);
                                          var reg = /^\d+$/


                                          if (value == null) {
                                            return 'After Cycle can not be less than 0.';
                                          } else if (value < 0) {
                                            return 'After Cycle can not be less than 0.';
                                          } else if (!reg.test(value)) {
                                            return "After Cycle can not be in point figures."
                                          } else {
                                            if ((indx > 0) && parseInt(allValues["appstleCycles"][indx - 1]?.afterCycle) >= parseInt(value)) {
                                              return 'After Cycle should be greater than ' + allValues["appstleCycles"][indx - 1]?.afterCycle
                                            } else if ((indx === 0) && values?.['discountEnabled2'] && parseInt(values?.['afterCycle2']) >= parseInt(value)) {
                                              return 'After Cycle should be greater than ' + parseInt(values?.['afterCycle2']);
                                            } else {
                                              return undefined
                                            }
                                          }

                                        }}
                                        id={`${name}.afterCycle`}
                                        className="form-control"
                                        type="number"
                                        name={`${name}.afterCycle`}

                                      />
                                    </FormGroup>
                                  </Fragment>
                                </Col>
                                <Col md={4}>
                                  <Field
                                    render={({input, meta}) => (
                                      <FormGroup>
                                        <Label>Offer</Label>
                                        <Input {...input}>
                                          <option value="PERCENTAGE">Percent off(%)</option>
                                          <option value="FIXED">Amount Off</option>
                                          <option value="PRICE">Fixed Price</option>
                                        </Input>
                                      </FormGroup>

                                    )}
                                    width="20px"
                                    id="discountType"
                                    className="form-control"
                                    type="select"
                                    name={`${name}.discountType`}
                                  />
                                </Col>
                                {(values?.appstleCycles[index]?.discountType === "PERCENTAGE") &&
                                  <Col md={4} sm={5} xs={16}>
                                    <Fragment>
                                      <Label for={`${name}.value`}>Discount Percentage</Label>
                                      <FormGroup style={{display: 'flex'}}>
                                        <Field
                                          render={({input, meta}) => (
                                            <InputGroup className="discountAmount">
                                              <Input {...input}
                                                     invalid={meta.error && meta.touched ? true : null}/>
                                              {meta.error && (
                                                <div
                                                  class="invalid-feedback"
                                                  style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                                >
                                                  {meta.error}
                                                </div>
                                              )}
                                              <InputGroupAddon addonType='append'>%</InputGroupAddon>
                                            </InputGroup>
                                          )}
                                          validate={value => {
                                            if (value == null) {
                                              return 'Please provide a valid value for After cycle discount percentage.';
                                            } else {
                                              if (value < 0) {
                                                return 'Please provide a valid value for After cycle discount percentage.';
                                              } else {
                                                return undefined;
                                              }
                                            }
                                          }}
                                          id={`${name}.value`}
                                          className="form-control"
                                          type="number"
                                          name={`${name}.value`}
                                        />

                                      </FormGroup>
                                    </Fragment>
                                  </Col>}
                                {(values?.appstleCycles[index]?.discountType === "FIXED") &&
                                  <Col md={4} sm={5} xs={16}>
                                    <Fragment>
                                      <Label for={`${name}.value`}>Discount Amount / quantity</Label>
                                      <FormGroup style={{display: 'flex'}}>
                                        <Field
                                          render={({input, meta}) => (
                                            <InputGroup className="discountAmount">
                                              <InputGroupAddon addonType='prepend'>{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                              <Input {...input}
                                                     invalid={meta.error && meta.touched ? true : null}/>
                                              {meta.error && (
                                                <div
                                                  class="invalid-feedback"
                                                  style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                                >
                                                  {meta.error}
                                                </div>
                                              )}
                                            </InputGroup>
                                          )}
                                          validate={value => {
                                            if (value == null) {
                                              return 'Please provide a valid value for After cycle discount amount.';
                                            } else {
                                              if (value < 0) {
                                                return 'Please provide a valid value for After cycle discount amount.';
                                              } else {
                                                return undefined;
                                              }
                                            }
                                          }}
                                          id={`${name}.value`}
                                          className="form-control"
                                          type="number"
                                          name={`${name}.value`}
                                        />

                                      </FormGroup>
                                    </Fragment>
                                  </Col>}
                                {(values?.appstleCycles[index]?.discountType === "PRICE") &&
                                  <Col md={4} sm={5} xs={16}>
                                    <Fragment>
                                      <Label for={`${name}.value`}>Discount Amount / quantity</Label>
                                      <FormGroup style={{display: 'flex'}}>
                                        <Field
                                          render={({input, meta}) => (
                                            <InputGroup className="discountAmount">
                                              <InputGroupAddon addonType='prepend'>{prd?.node?.pricingPolicy?.basePrice?.currencyCode}</InputGroupAddon>
                                              <Input {...input}
                                                     invalid={meta.error && meta.touched ? true : null}/>
                                              {meta.error && (
                                                <div
                                                  class="invalid-feedback"
                                                  style={{display: meta.error && meta.touched ? 'block' : 'none'}}
                                                >
                                                  {meta.error}
                                                </div>
                                              )}
                                            </InputGroup>
                                          )}
                                          validate={value => {
                                            if (value == null) {
                                              return 'Please provide a valid value for After cycle discount amount.';
                                            } else {
                                              if (value < 0) {
                                                return 'Please provide a valid value for After cycle discount amount.';
                                              } else {
                                                return undefined;
                                              }
                                            }
                                          }}
                                          id={`${name}.value`}
                                          className="form-control"
                                          type="number"
                                          name={`${name}.value`}
                                        />

                                      </FormGroup>
                                    </Fragment>
                                  </Col>}
                                <Col md={1} sm={2}>
                                  <Label>&nbsp;</Label>
                                  <FormGroup style={{display: 'flex', justifyContent: "space-between"}}>
                                    <Button
                                      size="sm"
                                      className="btn-shadow-primary"
                                      color="danger"
                                      style={{height: "38px", flexGrow: "1"}}
                                      onClick={() => {
                                        fields.remove(index);
                                      }}
                                      // disabled={!values['subscriptionPlans'][index]?.discountEnabled2Masked && (idx === 0)}
                                    >
                                      <Delete/>
                                    </Button>
                                  </FormGroup>
                                </Col>

                              </Row>
                            </>
                          )
                        )
                      }}
                    </FieldArray>
                    <FormSpy subscription={{values: true}}>
                      {({values}) => {
                        if (values?.appstleCycles && values?.appstleCycles?.length) {
                          let currentPrice = getCurrentPrice(values);
                          if (parseFloat(values?.currentPrice).toFixed(2) !== parseFloat(currentPrice).toFixed(2))
                            form.mutators.setValue("currentPrice", currentPrice);
                        } else {
                          if (parseFloat(values?.currentPrice).toFixed(2) !== parseFloat(values?.basePricePerUnit).toFixed(2))
                            form.mutators.setValue("currentPrice", values?.basePricePerUnit);
                        }

                        return <></>;
                      }}
                    </FormSpy>
                  </form>
                  {((!values?.appstleCycles) || (values?.appstleCycles?.length <= 1)) && <MySaveButton
                    text={"Add Pricing Policy"}
                    onClick={() => addLine({
                      afterCycle: 1,
                      discountType: "PERCENTAGE",
                      value: 0,
                    })}/>}
                </>
              );
            }}
          />

        </ModalBody>
        <ModalFooter>
          <Button color="link" onClick={() => setIsEditPricingPolicy(old => !old)}>Cancel</Button>
          <MySaveButton onClick={() => submit()} updating={isEditSellingPlanInProgress}
                        updating={isEditPricingPolicyInProgress}/>
        </ModalFooter>
      </Modal>
    </>
  );
};

const mapStateToProps = state => ({
  subUpcomingOrderEntities: state.subscriptionBillingAttempt.entity,
  subFailedOrderEntities: state.subscriptionBillingAttempt.failedBillingAttempt,
  loading: state.subscriptionBillingAttempt.loading,
  updatingSkipOrder: state.subscriptionBillingAttempt.updating,
  updatingBillingAttempt: state.subscriptionBillingAttempt.updatingBillingAttempt,
  billingAttempted: state.subscriptionBillingAttempt.billingAttempted,
  skippedBillingAttemptFlag: state.subscriptionBillingAttempt.skippedBillingAttemptFlag,
  isUpcomingOrderUpdatedFlag: state.subscriptionBillingAttempt.isUpcomingOrderUpdated,
  updatingBilling: state.subscription.updatingBilling,
  updatingDeliveryInterval: state.subscription.updatingDeliveryInterval,
  updateSuccess: state.subscription.updateSuccess,
  updatingNextOrderFlag: state.subscription.updatingBillingDate,
  updatingShippingAddress: state.subscription.updatingShippingAddress,
  updatingDeliveryPrice: state.subscription.updatingDeliveryPrice,
  updatingMinCycles: state.subscription.updatingMinCycles,
  updatingMaxCycles: state.subscription.updatingMaxCycles,
  updatingDeliveryMethodName: state.subscription.updatingDeliveryMethodName,
  shopInfo: state.shopInfo.entity,
  subscriptionContractDetailsEntity: state.subscriptionContractDetails.entity,
  updatingSubscriptionContractDetails: state.subscriptionContractDetails.updating,
  removeDiscountInProgress: state.subscription.removeDiscountInProgress,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  upcomingOrderDetailsEntity: state.subscriptionContractDetails.upcomingOrders,
  failedOrderLoading: state.subscriptionBillingAttempt.failedOrderLoading,
  totalFailedBillingAttemptItems: state.subscriptionBillingAttempt.totalFailedBillingAttemptItems,
  sellingPlanData: state.subscriptionGroup.sellingPlanData,
  subscriptionBundling: state.subscriptionBundling.entity,
});

const mapDispatchToProps = {
  getUpcomingOrderEntity,
  updateAttemptBillingEntity,
  skipBillingOrder,
  updateBillingInterval,
  updateDeliveryInterval,
  updatePaymentDetail,
  updateBillingDate,
  updateShippingAddress,
  updateDeliveryPrice,
  getEntity,
  getFailedOrderEntity,
  getSubscriptionContractDetailsByContractId,
  updateEntity,
  updateMinCycles,
  updateMaxCycles,
  removeDiscount,
  applyDiscount,
  getPortalSettings,
  getUpcomingOrderEntityList,
  updateDeliveryMethodName,
  getSellingPlans,
  getSubscriptionBundling
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionLineItem);

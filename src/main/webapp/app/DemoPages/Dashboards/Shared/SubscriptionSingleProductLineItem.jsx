import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import Axios from 'axios';
import {Button, FormGroup, Input, ListGroupItem, Modal, ModalBody, ModalFooter, ModalHeader, Row, Table} from 'reactstrap';

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
import {Info} from '@mui/icons-material';

import {getEntity as getPortalSettings} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {getSellingPlans} from 'app/entities/subscription-group/subscription-group.reducer'
import CustomHtmlToolTip from '../SubscriptionGroups/CustomHtmlToolTip';
import LoyaltyDetails from "./LoyaltyDetails";
import './MySubscriptionDetail.scss'
import {parseInt} from 'lodash';
import {
  getBundlingByToken as getSubscriptionBundling,
  getBundlingListsBySingleProduct
} from "app/entities/subscription-bundling/subscription-bundling.reducer";
import EditSingleBundleProductContent from "./EditSingleBundleProductContent";

const SubscriptionSingleProductLineItem = ({
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
                                             getBundlingListsBySingleProduct,
                                             subscriptionBundlingList,
                                             ...props
                                           }) => {


  const [isPrepaid, setIsPrepaid] = useState(
    subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true
  );

  const [globalDiscounts, setGlobalDiscounts] = useState([]);

  let [quantity, setQuantity] = useState('');
  let [isEditQuantity, setIsEditQuantity] = useState(false);
  let [sellingPlanNameValue, setSellingPlanNameValue] = useState('');
  let [price, setPrice] = useState('');
  let [pricingPolicy, setPricingPolicy] = useState({});
  let [fulfilments, setFulfilments] = useState(1);
  let [currentPrice, setCurrentPrice] = useState(0);
  let [basePricePerUnit, setBasePricePerUnit] = useState(0);
  let [buildABoxSingleProductProducts, setBuildABoxSingleProductProducts] = useState([]);
  let [aplicableProducts, setApplicableProducts] = useState([]);
  const [updatingLineAttribute, setUpdatingLineAttribute] = useState(false);
  const [applicableSettings, setApplicableSettings] = useState(null);
  const [singleBuildABoxError, setSingleBuildABoxError] = useState(null);

  const [totalProductsAdded, setTotalProductsAdded] = useState(0)
  const [isSwapSingleProducBuildABoxtModalOpen, setSwapSingleProducBuildABoxtModalOpen] = useState(false);
  const [isModelOpen, setModelOpen] = useState(false);
  const [searchValue, setSearchValue]=useState('')
  const [prodcutToSwap, setProductToSwap]= useState('')

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
  const getTenseBasedOnCurrentCycle = (currentCycle, afterCycle) => {
    if (currentCycle > afterCycle) {
      return "was";
    } else if (currentCycle <= afterCycle) {
      return "will be"
    }
  }

  useEffect(() => {
    if (subscriptionContractDetailsEntity.subscriptionType != null && subscriptionContractDetailsEntity?.subscriptionType === "BUILD_A_BOX_SINGLE_PRODUCT") {
      getBundlingListsBySingleProduct();
    }
  }, [subscriptionContractDetailsEntity])

  useEffect(() => {
    buildABoxSingleProductProducts.length ? setTotalProductsAdded(buildABoxSingleProductProducts.map(item => item.quantity).reduce((prev, next) => prev + next, 0)) : null
  }, [buildABoxSingleProductProducts])

  useEffect(() => {
    if (subscriptionBundlingList && subscriptionBundlingList.length > 0) {

      let buildABoxToken = null;
      if (subscriptionContractDetailsEntity.subscriptionTypeIdentifier != null) {
        buildABoxToken = subscriptionContractDetailsEntity.subscriptionTypeIdentifier;
      } else {
        buildABoxToken = subscriptionEntities?.lines?.edges[index]?.node?.customAttributes?.find((item) => item.key === "_appstle-bb-id")?.value || null
      }
      const appliedSubscriptionBundling = subscriptionBundlingList?.find((item) => item.uniqueRef === buildABoxToken);
      const singleProductSetting = appliedSubscriptionBundling?.singleProductSettings ? JSON.parse(appliedSubscriptionBundling?.singleProductSettings) : []
      const commaSeparatedProducts = subscriptionEntities?.lines?.edges[index]?.node?.customAttributes?.find((item) => item.key === "products")?.value || "";
      const productId = subscriptionEntities?.lines?.edges[index]?.node?.productId?.split('/').pop()
      const applicableSettings = singleProductSetting?.find((item) => item?.sourceProduct?.id === parseInt(productId));
      const planProducts = applicableSettings?.products;
      const productTitleList = commaSeparatedProducts.split(",");

      let buildABoxSingleProductProducts = []
      productTitleList.forEach((item) => {
        const selectedProduct = planProducts?.find((product) => item.includes(product.title))
        if (selectedProduct) {
          const productData = {
            product: selectedProduct,
            quantity: parseInt(item.split("x")[0])
          }
          buildABoxSingleProductProducts.push(productData);
        }
      })
      setBuildABoxSingleProductProducts(buildABoxSingleProductProducts);
      if (applicableSettings) {
        setApplicableProducts(applicableSettings?.products || []);
      }
      setApplicableSettings(applicableSettings);
    }
  }, [subscriptionBundlingList])

  const renderSelectedProduct = (product) => {
    let found = buildABoxSingleProductProducts.find((item) => item.product.id === product.id);
    if (found) {
      return true
    } else {
      return false
    }
  }

  const handleAddRemoveProduct = (checked, product) => {
    if (checked) {
      const totalQuantity = buildABoxSingleProductProducts.length ? buildABoxSingleProductProducts.map(item => item.quantity).reduce((prev, next) => prev + next, 0) : 0;
      if (applicableSettings.maxQuantity && applicableSettings.maxQuantity < totalQuantity + 1) {
        setSingleBuildABoxError("Maximum limit " + applicableSettings.maxQuantity + " Items.");
        return;
      } else {
        const productData = {
          product: product,
          quantity: 1
        }
        setBuildABoxSingleProductProducts([...buildABoxSingleProductProducts, productData]);
        setSingleBuildABoxError(null);
      }
    } else {
      const newProducts = [...buildABoxSingleProductProducts];
      const toRemoveIndex = newProducts.findIndex((item) => item.product.id === product.id);
      if (toRemoveIndex > -1) {
        newProducts.splice(toRemoveIndex, 1)
        setBuildABoxSingleProductProducts(newProducts);
      }
    }
  }

  const handleChangeEditProductQuantity = (value, product) => {
    const newProducts = [...buildABoxSingleProductProducts];
    const editIndex = newProducts.findIndex((item) => item.product.id === product.id);
    const totalQuantity = buildABoxSingleProductProducts.length ? buildABoxSingleProductProducts.filter((item) => item.product.id !== product.id).map(item => item.quantity).reduce((prev, next) => prev + next, 0) : 0;
    if (editIndex > -1 && applicableSettings.maxQuantity && totalQuantity + parseInt(value) > applicableSettings.maxQuantity) {
      setSingleBuildABoxError("Maximum limit " + applicableSettings.maxQuantity + " Items.");
      return
    } else if (editIndex > -1 && (!applicableSettings.maxQuantity || totalQuantity + parseInt(value) <= applicableSettings.maxQuantity)) {
      newProducts[editIndex].quantity = parseInt(value);
      setSingleBuildABoxError(null);
    }
    setBuildABoxSingleProductProducts(newProducts);
  }

  const removeSwapProduct = (product, addProduct) => {
    const newProducts = [...buildABoxSingleProductProducts];
    const toRemoveIndex = newProducts.findIndex((item) => item.product.id === product.id);
    if (toRemoveIndex > -1) {
      newProducts.splice(toRemoveIndex, 1)
      newProducts.push({
        product: addProduct,
        quantity: 1
      })
      setBuildABoxSingleProductProducts(newProducts);
      updateSubscriptionContract(newProducts)
    }
  }

  const handleSearch = event => {
    if (event.target.value !== searchValue) {
      setSearchValue(event.target.value.toLowerCase());
    }
  };


  const handleEdit = () => {
    setModelOpen(true);
    setIsEditQuantity(true);
    setSwapSingleProducBuildABoxtModalOpen(false);
    setSearchValue('');
  };

  const handleSwap = (product) => {
    setProductToSwap(product)
    setModelOpen(true);
    setSwapSingleProducBuildABoxtModalOpen(true);
    setIsEditQuantity(false);
    setSearchValue('');
  };

  const updateSubscriptionContract = (updatedProductList) => {
    setUpdatingLineAttribute(true);
    let productAttribute = []
    let productList  = updatedProductList ? updatedProductList : buildABoxSingleProductProducts;
    productList.forEach((item) => {
      const text = item.quantity + "x " + item.product.title
      productAttribute.push(text)
    })
    const attributes = [...subscriptionEntities?.lines?.edges[index]?.node?.customAttributes, {
      key: "products",
      value: productAttribute.join(",")
    }]
    const requestUrl = `/api/subscription-contracts-update-line-item-attributes?contractId=${contractId}&lineId=${prd?.node?.id}`;
    Axios.put(requestUrl, attributes)
      .then(data => {
        setIsEditQuantity(false);
        setUpdatingLineAttribute(false);
        getEntity(contractId);
        setSwapSingleProducBuildABoxtModalOpen(false);
        setModelOpen(false);
        toast.success('Contract product updated', options);
      })
      .catch(err => {
        setIsEditQuantity(false);
        setUpdatingLineAttribute(false);
        setSwapSingleProducBuildABoxtModalOpen(false);
        setModelOpen(false);
        toast.error('Contract update failed', options);
      });
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
                    style={{borderRadius: '2px'}}
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
                  <span>
                <b className={"product-quantity"}>Quantity:</b>
                <div style={{display: "inline-flex"}}>
                  <span className="ml-2">{prd?.node?.quantity}&nbsp;</span>
                </div>
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
                            </div>
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
                          off
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
                                off
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
          />
          <div className="d-flex">
            {
              (!prd.node?.pricingPolicy && prd?.node?.sellingPlanId) &&
              <LoyaltyDetails sellingPlan={prd?.node?.sellingPlanId} sellingPlanData={sellingPlanData}
                              currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}/>
            }
          </div>
        </div>

        {/*Build a box single product*/}
        {buildABoxSingleProductProducts.length > 0 && buildABoxSingleProductProducts.map((item, index) => {
          return (
            <div className="d-flex align-items-center mt-2" key={index}>
              <img src={item?.product?.imageSrc} width={50}
                   style={{borderRadius: '29%', padding: '10px'}} alt={"I"}/>

              <div>
                <a href={`https://${shopName}/products/${item?.product?.productHandle}`} target="_blank">
      <span>{item?.product?.title}{' '}
      </span>
                </a>
                <div>
                  <span>Quantity:{' '}</span>
                  <span>{item?.quantity}</span>
                  <a href='javascript:void(0)' onClick={() => handleSwap(item)} class="pl-3">Swap</a>
                </div>
              </div>
            </div>)
        })}
        <div className={"d-flex w-100 justify-content-end"}>
          {buildABoxSingleProductProducts && buildABoxSingleProductProducts.length > 0 ? (
            <Button color="primary mb-2" onClick={() => handleEdit()} style={{minWidth: '120px'}}>
              <i className="lnr lnr-pencil btn-icon-wrapper"/>&nbsp; Edit
            </Button>) : ""}
        </div>
        {/*Build a box single product*/}

      </ListGroupItem>
      <Modal isOpen={isModelOpen} toggle={() => setModelOpen(old => !old)} size={"md"}>
        <ModalHeader toggle={() => setModelOpen(old => !old)}>
        {isSwapSingleProducBuildABoxtModalOpen ? "Swap Product" : "Edit Quantity" }
        </ModalHeader>
        <ModalBody>
          <FormGroup className={"ml-3 build-a-box-item-list"}>
            <p className={"text-danger"}>{singleBuildABoxError != null ? singleBuildABoxError : ""}</p>
          </FormGroup>
          <Row>
          <FormGroup  style={{padding:"5px 5px"}} className="w-100">
            <Input type="text"  placeholder= { isSwapSingleProducBuildABoxtModalOpen ? "Search a product to swap" : "Search a product"}
          onChange={handleSearch}
          value={searchValue} />
          </FormGroup>
          </Row>
          <Row>
            {aplicableProducts.length && aplicableProducts.map((product, index) => {
              const entity = buildABoxSingleProductProducts.find((item) => item.product.id === product.id)
              return (
                product.title.toLowerCase().includes(searchValue) ?
                <EditSingleBundleProductContent
                  entity={entity}
                  customerPortalSettingEntity={customerPortalSettingEntity}
                  renderSelectedProduct={renderSelectedProduct}
                  handleAddRemoveProduct={handleAddRemoveProduct}
                  product={product}
                  handleChangeEditProductQuantity={handleChangeEditProductQuantity}
                  totalProductsAdded={totalProductsAdded}
                  applicableSettings={applicableSettings}
                  isProductSwap = {isSwapSingleProducBuildABoxtModalOpen}
                  prodcutToSwap={prodcutToSwap}
                  removeSwapProduct={removeSwapProduct}
                /> : ""
              )
            })}
          </Row>
        </ModalBody>
        { !isSwapSingleProducBuildABoxtModalOpen ?
          <ModalFooter>
            <Button className={"build-a-box-cancel"} color="link"
                    onClick={() => setModelOpen(old => !old)}>Cancel</Button>
            <Button className={"build-a-box-edit"} type="button" color="success"
                    disabled={updatingLineAttribute || totalProductsAdded < applicableSettings?.minQuantity}
                    onClick={() => updateSubscriptionContract()}>
              {!updatingLineAttribute && (
                <>
                  <i className="lnr pe-7s-diskette btn-icon-wrapper"/> Save
                </>
              )}
              {updatingLineAttribute && <div className="appstle_loadersmall"/>}
            </Button>
          </ModalFooter> : ""
        }
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
  subscriptionBundlingList: state.subscriptionBundling.entities,
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
  getSubscriptionBundling,
  getBundlingListsBySingleProduct
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionSingleProductLineItem);

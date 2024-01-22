import React, {useEffect, useState} from 'react';
import {connect, useSelector} from 'react-redux';
import axios from 'axios';
import {formatPrice, getCustomerIdentifier, redirectToAccountPage} from 'app/shared/util/customer-utils';
import {Card, CardBody} from 'reactstrap';
import { toast } from 'react-toastify';
import {Link} from 'react-router-dom';
import 'react-datepicker/dist/react-datepicker.css';
import {getCustomerPortalEntity, getvalidCustomerEntity} from 'app/entities/customers/customer.reducer';
import {getEntity as getPortalSettings} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import { getEntity as getCancellationManagementEntity } from 'app/entities/cancellation-management/cancellation-management.reducer';
import {splitSubscriptionContract} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import {updateAttemptBillingEntity,getCustomerUpcomingOrderEntity} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { getCustomerSubscriptionContractDetailsByContractId as getSubscriptionContractDetailsByContractId } from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
import {DateTime} from "luxon";
import {ChevronRightIcon,} from '@heroicons/react/20/solid'
import MagicLinkForm from "./MagicLinkForm";
import getSymbolFromCurrency from "currency-symbol-map";
import CancelSubscription from './CancelSubscriptionV2';
import TailwindModal from './TailwindModal';
import SplitContact from './SplitContract';
import {CallSplit, KeyboardArrowLeft} from '@mui/icons-material';
import Loader from './Loader';

const SubscriptionsList = props => {
  const {
    getCustomerPortalEntity,
    customerEntity,
    getPortalSettings,
    customerPortalSettingEntity,
    showRedirectMessage,
    getCancellationManagementEntity,
    cancellationManagementEntity,
    updateAttemptBillingEntity
  } = props;

  const loadingCustomer = useSelector(state => state.customer.loading);
  const customerId = getCustomerIdentifier();
  const [validContractId, setValidContractId] = useState([]);
  const [updatingContractStatus, setUpdatingContractStatus] = useState(false);
  const [subscriptionEntities,setSubscriptionEntities] = useState({});
  const [isSplitContractModalOpen, setIsSplitContractModalOpen] = useState(false);
  const [showSplitContractMessaging, setShowSplitContractMessaging] = useState(false);
  const [splitSuccess, setSplitSuccess] = useState(false);
  const [splitError, setSplitError] = useState(false);
  const [splitContractProducts, setSplitContractProducts] = useState([]);
  const [subscriptionContracts, setSubscriptionContracts] = useState(null);
  const [isUpdateAttemptBillingEntitySuccess, setIsUpdateAttemptBillingEntitySuccess] = useState(false);
  const [isSplitButtonProcess, setIsSplitButtonProcess] = useState(false);
  const [isCancelButtonProcess, setIsCancelButtonProcess] = useState(false);
  const [splitWithOrder, setSplitWithOrder] = useState(false);
  const [globalDiscounts, setGlobalDiscounts] = useState([]);
  const [productSelectValid, setProductSelectValid] = useState(true);

  if (!customerId) {
    redirectToAccountPage();
    // setShowRedirectMessage(true);
  }

  const getCustomerPortalEnity = async () => {
    let data = await  getCustomerPortalEntity(customerId);
    setSubscriptionContracts(data?.value?.data?.subscriptionContracts?.edges)
  };

  useEffect(() => {
    getCustomerPortalEnity();
    // getvalidCustomerEntity(customerId, Shopify.shop);
    getPortalSettings(0);
    getCancellationManagementEntity(0);
    if (window?.parent?.postMessage) {
      window?.parent?.postMessage("appstle_scroll_top");
    }
  }, []);

  const subscriptionCustomersDetail = () => {
    axios
      .get(`api/subscription-customers-detail/valid/${customerId}`)
      .then(res => {
        setValidContractId(res.data);
        if (window?.showBandBoxCP) {
          let firstContract = res?.data?.sort(function (a, b) {
            if (a.status < b.status) {
              return -1;
            }
            if (a.status > b.status) {
              return 1;
            }
            // names must be equal
            return 0;
          })[0]
          props?.history?.push(`/subscriptions/${firstContract?.subscriptionContractId}/detail`)
        }
      })
      .catch(err => {
        console.log(err)
        // setShowRedirectMessage(true);
      });
  }

  useEffect(() => {
    subscriptionCustomersDetail()
  }, []);

  let frequencyIntervalTranslate = {
    "week": customerPortalSettingEntity?.weekText,
    "day": customerPortalSettingEntity?.dayText,
    "month": customerPortalSettingEntity?.monthText,
    "year": customerPortalSettingEntity?.yearText,
    "weeks": customerPortalSettingEntity?.weeksTextV2,
    "days": customerPortalSettingEntity?.daysTextV2,
    "months": customerPortalSettingEntity?.monthsTextV2,
    "years": customerPortalSettingEntity?.yearsTextV2,
  };
  const getFrequencyTitle = (interval, intervalCount) => {
    let intervalText = parseInt(intervalCount) > 1 ? `${interval}s` : interval;
    return frequencyIntervalTranslate[intervalText?.toLowerCase(intervalText)] ? frequencyIntervalTranslate[intervalText?.toLowerCase()] : intervalText;
  }

  const options = {
    autoClose: 500,
    position: toast.POSITION.TOP_CENTER
  };

  const updateContractStatus = async (contractId, status, pauseDurationCycle) => {
    setUpdatingContractStatus(true);
    return await axios
    .put(
      `api/subscription-contracts-update-status?contractId=${contractId}&status=${status}&isExternal=true${
        pauseDurationCycle ? '&pauseDurationCycle=' + pauseDurationCycle : ''
      }`
    )
    .then(res => {
        if (res.status == 200) {
          setUpdatingContractStatus(false);
          subscriptionCustomersDetail();
          if (status == 'PAUSED') {
            toast.success(customerPortalSettingEntity.subscriptionPausedMessageText, options);
          } else {
            toast.success(customerPortalSettingEntity.subscriptionActivatedMessageText, options);
          }
        } else {
          toast.error(customerPortalSettingEntity.unableToUpdateSubscriptionStatusMessageText);
          setUpdatingContractStatus(false);
        }
        return res;
      })
      .catch(err => {
        setUpdatingContractStatus(false);
        toast.error(err.response.data.message, options);
        return err;
      });
  };

  const checkIfPreventCancellationBeforeDays = (days, nextBillingDate) => {
    if (days && nextBillingDate) {
      let nextOrderDate = moment(nextBillingDate).format();
      let currentDateWithExtraDays = moment()
        .add(parseInt(days), 'd')
        .format();
      if (moment(nextOrderDate).isBefore(currentDateWithExtraDays)) {
        return true;
      }
    } else {
      return false;
    }
  };


  const extractGlobalDiscounts = (subscriptions) => {
    if (subscriptions?.discounts?.edges?.length > 0) {
      let globalDiscountArray = [];
      subscriptions?.discounts?.edges?.forEach(discountNode => {
        let isCurrentDiscountNodeGlobal = true;
        subscriptions?.lines?.edges?.forEach(productNode => {
          let productNodeDiscountArray = [];
          productNode?.node?.discountAllocations?.forEach(productDiscount => {
            productNodeDiscountArray?.push(productDiscount?.discount?.id);
          });
          if (productNodeDiscountArray?.length && productNodeDiscountArray?.indexOf(discountNode?.node?.id) === -1) {
            isCurrentDiscountNodeGlobal = false;
          }
        });
        if (isCurrentDiscountNodeGlobal) {
          globalDiscountArray?.push(JSON.parse(JSON.stringify(discountNode)));
        }
      });
      setGlobalDiscounts([...globalDiscountArray]);
    } else {
      setGlobalDiscounts([]);
      return;
    }
  };

  const getUpcomingOrderId = async (contractId, customerId) => {
    if (subscriptionEntities?.billingPolicy?.intervalCount != subscriptionEntities?.deliveryPolicy?.intervalCount) {
        return subscriptionEntities?.originOrder?.fulfillmentOrders?.edges.sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))[0]?.node?.id.toString();
    } else {
      const requestUrl = `api/subscription-billing-attempts/top-orders?contractId=${contractId}&${isNaN(customerId) ? 'customerUid' : 'customerId'}=${customerId}`;
      let subUpcomingOrderEntities = await axios.get(requestUrl);
      return subUpcomingOrderEntities?.data?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))[0]?.id.toString();
    }
  }

  const attemptBillingHandler = async () => {
    if (splitContractProducts.length <= 0) {
      setProductSelectValid(false);
      return;
    }
    setIsUpdateAttemptBillingEntitySuccess(true);
    const contractId = subscriptionEntities?.id.split("/")?.pop();
    if (splitContractProducts.length === props.subscriptionEntities?.lines?.edges?.length) {
      const upcomingOrderId = await getUpcomingOrderId(contractId, customerId);
      let results = await updateAttemptBillingEntity(upcomingOrderId, customerPortalSettingEntity?.shop, contractId);
      if (results) {
        setShowSplitContractMessaging(true);
        if (results?.value?.status === 200) {
          setSplitSuccess(true);
          setSplitError(false);
        } else {
          setSplitSuccess(false);
          setSplitError(true);
        }
      }
      setIsUpdateAttemptBillingEntitySuccess(false);
    } else {
      axios.post(`api/subscription-contract-details/split-existing-contract?contractId=${contractId}&isSplitContract=${true}&attemptBilling=${splitWithOrder}`,splitContractProducts).then(results => {
        setShowSplitContractMessaging(true);
        if (results?.status === 200) {
          setSplitSuccess(true);
          setSplitError(false);
          subscriptionCustomersDetail();
        } else {
          setSplitSuccess(false);
          setSplitError(true);
        }
        setIsUpdateAttemptBillingEntitySuccess(false);
      }).catch(err => {
          setShowSplitContractMessaging(true);
          setSplitSuccess(false);
          setSplitError(true);
          setIsUpdateAttemptBillingEntitySuccess(false);
      });
    }
  };


  const resetSplitContractModal = () => {
    setIsSplitContractModalOpen(false);
    setShowSplitContractMessaging(false);
    setSubscriptionContracts(null);
    setSplitContractProducts([]);
    setSubscriptionEntities({});
    setSplitSuccess(false);
    setSplitError(false);
    setProductSelectValid(true);
  };

  const handleSplitContractProducts = lineId => {
    let newProductsIds = [...splitContractProducts];
    let index = newProductsIds.findIndex(item => item === lineId);
    if (index > -1) {
      newProductsIds.splice(index, 1);
    } else {
      newProductsIds.push(lineId);
    }
    setSplitContractProducts(newProductsIds);
  };

  const openSplitPopUp = async (node) => {
    setIsSplitButtonProcess(true);
    let response = await getSubscriptionContractDataById(node.subscriptionContractId);
    setSubscriptionEntities(response?.data);
    setIsSplitContractModalOpen(true);
    setIsSplitButtonProcess(false);
  };

  const getSubscriptionContractDataById = async (subscriptionContractId) => {
    const requestUrl = `api/subscription-contracts/contract-external/${subscriptionContractId}?isExternal=true`;
    return await axios.get(requestUrl);
  };

  const openCancelPopUp = async (subscriptionContractId) => {
    setIsCancelButtonProcess(true);
    let response = await getSubscriptionContractDataById(subscriptionContractId);
    setSubscriptionEntities(response?.data);
    extractGlobalDiscounts(response?.data);
    setIsCancelButtonProcess(false);
  };

  const checkProducts = (node) => {
    const products = JSON.parse(node?.contractDetailsJSON);
    if(products?.length > 1) {
      return true;
    } else {
      return false;
    }
  };

  const getFromDate = (lastOrderDate, daysToAdd) => {
    let orderDate = new Date(lastOrderDate);
    let currentDayNumber = orderDate.getDay();
    let additionalDaysToAdd = daysToAdd;
    if (currentDayNumber >= 5) {
      additionalDaysToAdd = additionalDaysToAdd + (7 - currentDayNumber);
    }
    return new Date(orderDate.setDate(orderDate.getDate() + additionalDaysToAdd));
  };

  const customShopName = 'co2-you.myshopify.com';

  const [contractProductIds, setContractProductIds] = useState([]);
  const [productHandles, setProductHandles] = useState(null);

  useEffect(() => {
    if (customerPortalSettingEntity?.enableRedirectToProductPage) {
      let ids = [];
      if (validContractId && validContractId?.length > 0) {
        validContractId?.sort((a, b) => ((a.status < b.status) ? 1 : (a.status > b.status ) ? 1 : 0))?.map((node) => {
          JSON.parse(node.contractDetailsJSON)?.map(contractItem => {
            ids.push(contractItem?.productId);
          });
        });
      }
      setContractProductIds([...new Set(ids)]);
    }
  }, [validContractId]);

  useEffect(() => {
    if (contractProductIds && contractProductIds.length > 0) {
      axios.get(`/api/data/product-handles?productIds=${contractProductIds?.filter(value => value)?.join()}`).then(res => {
        if (res?.data) {
          setProductHandles(res?.data);
        }
      })
    }
  }, [contractProductIds])

  useEffect(() => {
    if (customerEntity?.id) {
      window.top['appstleCustomerId'] = customerEntity?.id?.split('/').pop();
    }
  }, [customerEntity])

  const productURLHandles = (productId) => {
    if (productHandles && productId) {
      let product = productHandles[productId?.split("/")?.pop()];
      console.log("Ajay Product :- " , product);
      return product ? product : undefined;
    }
    return undefined;
  }

  return (
    <>
      {loadingCustomer ? (
        <div className="as-m-8 as-flex as-h-screen as-justify-center as-items-center">
          <svg role="status"
               class="as-inline as-w-8 as-h-8 as-mr-2 as-text-gray-200 as-animate-spin as-fill-blue-600"
               viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
              fill="currentColor"/>
            <path
              d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
              fill="currentFill"/>
          </svg>
          <p
            className='as-text-sm as-text-gray-500'>{customerPortalSettingEntity?.pleaseWaitLoaderText || "Please wait."}</p>
        </div>
      ) : (
        <>
        <div>
          <div class="as-flex as-items-center as-justify-between as-customer-info">
            <div className="as-flex as-items-center">
              {(window?.appstleCustomerData && appstleCustomerData?.customerId) && <>
                <img className="as-w-14"
                    src={`https://ui-avatars.com/api/?rounded=true&name=${appstleCustomerData?.firstName}%20${appstleCustomerData?.lastName}`}/>
                <div className="as-ml-3">
                  <h2
                    className="as-customer-info-name as-text-lg">{customerPortalSettingEntity?.helloNameText || `Hello`} {(appstleCustomerData?.firstName || appstleCustomerData?.lastName) ? <>
                      <span className="appstle_customer_firstName">{appstleCustomerData?.firstName}</span> <span
                      className="appstle_customer_lastName">{appstleCustomerData?.lastName}</span> </> :
                    <span
                      className="appstle_greeting_text">{customerPortalSettingEntity?.greetingText || "There"}</span>}</h2>
                  {appstleCustomerData?.customerId && <div
                    className="as-text-sm">{customerPortalSettingEntity?.customerIdText || `Customer ID`}: {appstleCustomerData?.customerId}</div>}
                </div>
              </>}
            </div>
            <div>
              <a
                href={`https://${customerPortalSettingEntity.shop}/account`}
                target={customerPortalSettingEntity?.enableRedirectMyAccountButton ? "" : "_blank"}
                className="appstle_contract_see_more"
                style={{ marginTop: '15px' }}
              >
                <button className="appstle_order-detail_update-button" style={{ paddingLeft: '0' }}>
                  <KeyboardArrowLeft style={{ height: '18px' }} /> {customerPortalSettingEntity?.goBackAccountPageTextV2 || "My Account"}
                </button>
              </a>
            </div>
          </div>
          {(!loadingCustomer && showRedirectMessage) ? (
            (!customerPortalSettingEntity?.magicLinkEmailFlag) ? <Card
              style={{margin: '28px 0px', borderRadius: '8px 8px 0 0', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'}}>
              <CardBody>
                <div dangerouslySetInnerHTML={{__html: customerPortalSettingEntity?.expiredTokenText}}
                     className="as-expiredTokenText as-subscriptionListError"></div>
              </CardBody>
            </Card> : <MagicLinkForm customerPortalSettingEntity={customerPortalSettingEntity}/>
          ) : ''}
          {(!loadingCustomer && validContractId?.length === 0 && !showRedirectMessage) ?
            <Card
              style={{margin: '28px 0px', borderRadius: '8px 8px 0 0', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'}}>
              <CardBody>
                <div dangerouslySetInnerHTML={{__html: customerPortalSettingEntity?.noSubscriptionMessageV2}}
                     className="as-noSubscriptionMessageV2"></div>
              </CardBody>
            </Card> : ''
          }
          {(!loadingCustomer && validContractId && validContractId?.length) ?
            validContractId.sort(function (a, b) {
              if (a.status < b.status) {
                return -1;
              }
              if (a.status > b.status) {
                return 1;
              }
              // names must be equal
              return 0;
            })
              .map((node) => {
                var options = {year: 'numeric', month: 'long', day: 'numeric'};
                let price = 0;
                let currencyCode = "";
                let hasDeletedProduct = false;
                return (
                  <>
                    {(
                      <>
                        <div
                          className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-my-8 as-card as-subscription">
                          <div
                            className="as-px-4 as-py-5 sm:as-px-6 as-flex as-justify-between as-flex-col lg:as-flex-row as-subscription-header">
                            <h3
                              className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-flex as-items-center">
                              <span
                                className="as-contract-id">#{node?.subscriptionContractId}</span> {node?.status == 'active' ? (
                              <span
                                className="as-bg-green-100 as-text-green-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-active-badge as-status">{customerPortalSettingEntity?.activeBadgeText}</span>
                            ) : node?.status == 'cancelled' ? (
                              <span
                                className="as-bg-red-100 as-text-red-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-close-badge as-status">{customerPortalSettingEntity?.closeBadgeText}</span>
                            ) : node?.status == 'paused' ? (
                              <span
                                className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-pause-badge as-status">{customerPortalSettingEntity?.pauseBadgeText}</span>
                            ) : (
                              ''
                            )}</h3>
                            {<p className="as-mt-1 as-max-w-2xl as-text-sm as-text-gray-900 as-next-order-date"><span
                              className="as-font-medium as-label">{customerPortalSettingEntity?.nextOrderText}<span className='colon-symbol'>:</span></span>&nbsp;
                              <span className={`as-value ${node?.status == 'cancelled' ? 'as-text-red-800 as-line-through' : '' }`}>
                                {!(Shopify.shop === customShopName) && (
                                  DateTime.fromISO(node?.nextBillingDate).toFormat(customerPortalSettingEntity?.dateFormat)
                                )}
                                {Shopify.shop === customShopName && (
                                  DateTime.fromISO(getFromDate(node?.nextBillingDate, 1).toISOString()).toFormat(
                                    customerPortalSettingEntity?.dateFormat
                                  )
                                )}
                              </span>
                            </p>}
                            <p className="as-mt-1 as-max-w-2xl as-text-sm as-text-gray-900 as-order-frequency"><span
                              className="as-font-medium as-label">{customerPortalSettingEntity?.orderFrequencyTextV2}<span className='colon-symbol'>:</span></span>&nbsp;
                              <span
                                className="as-value">{customerPortalSettingEntity?.everyLabelTextV2} {node?.billingPolicyIntervalCount} {getFrequencyTitle(node?.billingPolicyInterval, node?.billingPolicyIntervalCount)}</span>
                            </p>
                          </div>
                          <div
                            className="as-border-t as-border-b as-border-gray-200 as-px-4 as-py-5 sm:as-px-6 as-subscription-detail">
                            <div className="as-grid as-gap-4 lg:as-grid-cols-2">
                              {JSON.parse(node.contractDetailsJSON)?.map(contractItem => {
                                if (contractItem?.discountedPrice) {
                                  price = price + parseFloat(contractItem?.discountedPrice)
                                } else if (contractItem?.currentPrice && contractItem?.quantity) {
                                  price = price + (parseFloat(contractItem?.quantit) * parseFloat(contractItem?.currentPrice))
                                }

                                // if (contractItem?.node?.currentPrice?.currencyCode) {
                                //   currencyCode = contractItem?.node?.currentPrice?.currencyCode
                                // }

                                if (contractItem?.productId == null) {
                                  hasDeletedProduct = true;
                                }
                                return (
                                  <div
                                    className='as-grid as-grid-cols-4 as-gap-4 as-items-center as-subscription-product'>
                                    <div className='as-col-span-1 as-subscription-product-image-wrapper'>
                                      {(customerPortalSettingEntity?.enableRedirectToProductPage && productURLHandles(contractItem?.productId)) ?
                                        <a className='as-product-title-tag-a as-w-full' href={`https://${Shopify?.shop}/products/${productURLHandles(contractItem?.productId)}`} target='_blank'>
                                          <img
                                            src={contractItem?.productId == null ? "https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660" : contractItem?.variantImage}
                                            alt=""
                                            className="as-w-full as-subscription-product-image"
                                          />
                                        </a> :
                                        <img
                                          src={contractItem?.productId == null ? "https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660" : contractItem?.variantImage}
                                          alt=""
                                          className="as-w-full as-subscription-product-image"
                                        />
                                      }
                                    </div>
                                    <div className='as-col-span-3 as-subscription-product-details'>
                                      {contractItem?.title &&
                                        <p className={`as-text-sm as-text-gray-800 as-mb-2 as-product-title ${contractItem?.productId == null ? "as-line-through" : ""}`}>
                                          {(customerPortalSettingEntity?.enableRedirectToProductPage && productURLHandles(contractItem?.productId)) ?
                                            <a className='as-product-title-tag-a' href={`https://${Shopify?.shop}/products/${productURLHandles(contractItem?.productId)}`} target='_blank'>{contractItem?.title} {contractItem?.productId == null ? "*" : ""}</a> :
                                            <>{contractItem?.title} {contractItem?.productId == null ? "*" : ""}</>
                                          }
                                        </p>
                                      }

                                      {contractItem?.variantTitle && contractItem?.variantTitle !== "-" && contractItem?.variantTitle !== "Default Title" &&
                                        <p className="as-text-xs as-text-gray-500 as-variant"><span
                                          className="as-font-medium as-text-gray-600 as-label">{customerPortalSettingEntity?.variantLblText || "Variant"}<span className='colon-symbol'>:</span></span>&nbsp;
                                          <span className="as-value">{contractItem?.variantTitle}</span>
                                        </p>}
                                      {contractItem?.quantity &&
                                        <p className="as-text-xs as-text-gray-500 as-quantity"><span
                                          className='as-font-medium as-text-gray-600 as-label'>{customerPortalSettingEntity?.quantityLbl || "Quantity"}<span className='colon-symbol'>:</span></span>&nbsp;
                                          <span className="as-value">{contractItem?.quantity}</span>
                                        </p>}
                                      {contractItem?.currentPrice &&
                                        <p className="as-text-xs as-text-gray-500 as-price"><span
                                          className='as-font-medium as-text-gray-600 as-label'>{customerPortalSettingEntity?.priceLbl || "Price"}<span className='colon-symbol'>:</span></span>&nbsp;
                                          <span className="as-value">
                                           {formatPrice(parseFloat(contractItem?.currentPrice).toFixed(2)*100)}
                                          </span>
                                        </p>}
                                    </div>
                                  </div>
                                );
                              })}
                            </div>
                            {hasDeletedProduct &&
                              <p className="as-text-right as-mt-2 as-text-xs as-text-gray-500 as-removed-tooltip">
                                *{customerPortalSettingEntity?.productRemovedTooltip}
                              </p>
                            }
                          </div>
                          {node?.orderNote && <div className='as-border-b as-px-4 as-py-5 sm:as-px-6 as-flex as-justify-between as-flex-col md:as-flex-row as-items-center as-text-sm as-subscription-list-item-detail'>
                            {customerPortalSettingEntity?.orderNoteText || 'Order Note'}: {node?.orderNote}
                          </div>}
                          <div
                            className="as-px-4 as-py-5 sm:as-px-6 as-flex as-justify-between as-flex-col md:as-flex-row as-items-center  as-subscription-footer">
                               <p
                                  className="as-text-md as-leading-6 as-font-medium as-text-gray-700 as-flex as-items-center as-total">
                                  <span
                                    className="as-font-medium as-label">{customerPortalSettingEntity?.totalLblText || "Total"}<span className='colon-symbol'>:</span></span>&nbsp;
                                  <span
                                    className="as-value">{formatPrice(parseFloat(price).toFixed(2)*100)}</span>
                                </p>

                              {(customerPortalSettingEntity?.enableSplitContract) && checkProducts(node) && (node?.status != 'cancelled') ?
                               <div>
                                <button
                                    type="button"
                                    onClick={() => openSplitPopUp(node)}
                                    disabled={isSplitButtonProcess}
                                    class="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary as-edit-billing_primary-button disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-900 as-order-now-btn as-mt-2 as-mx-2" style={{width:"max-content",backgroundColor: "#f7b924",borderColor: "#f7b924"}}
                                  >
                                    {!isSplitButtonProcess ? <><CallSplit/> {customerPortalSettingEntity?.splitContractText || 'Split Contract'}</>  : <Loader /> }
                                  </button>
                                </div> : ''
                              }

                              <div className='as-flex as-items-center'>
                              { (customerPortalSettingEntity?.cancelSub) && (node?.status == 'active' || node?.status == 'paused') ?
                              <div>
                                <CancelSubscription
                                  contractId={node?.subscriptionContractId}
                                  subscriptionContractDetails={node}
                                  onUpdate={subscriptionCustomersDetail}
                                /></div> : ''
                                }
                                <Link to={`/subscriptions/${node?.subscriptionContractId}/detail`}
                                  className="appstle_contract_see_more as-see-more-link as-pl-3 as-mt-2">
                                  <button type="button"
                                          className="as-inline-flex as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary as-see-more-button" style={{width:"max-content"}}>
                                    {customerPortalSettingEntity?.seeMoreDetailsText}
                                    <ChevronRightIcon className="as-h-5 as-w-5 as-see-more-icon" aria-hidden="true"/>
                                  </button>
                                </Link>
                            </div>
                          </div>
                        </div>
                      </>
                    )}
                  </>
                );
              }) : ''}
        </div>

          <TailwindModal
            open={isSplitContractModalOpen}
            setOpen={resetSplitContractModal}
            actionMethod={attemptBillingHandler}
            actionButtonText={!showSplitContractMessaging ? 'Confirm' : ''}
            updatingFlag={isUpdateAttemptBillingEntitySuccess}
            modalTitle={customerPortalSettingEntity?.splitContractText || 'Split Contract'}
            className="as-model-order-now"
            success={splitSuccess}
          >
            <SplitContact showSplitContractMessaging={showSplitContractMessaging}
              splitSuccess={splitSuccess}
              splitError={splitError}
              customerPortalSettingEntity={customerPortalSettingEntity}
              subscriptionEntities={subscriptionEntities}
              handleSplitContractProducts={handleSplitContractProducts}
              splitContractProducts={splitContractProducts}
              shopName={customerPortalSettingEntity.shop}
              splitWithOrder={splitWithOrder}
              setSplitWithOrder={setSplitWithOrder}
              productSelectValid= {productSelectValid}
            />
        </TailwindModal>
        </>
      )}
    </>
  );
};

const mapStateToProps = state => ({
  customerEntity: state.customer.entity,
  showRedirectMessage: state.customer.showRedirectMessage,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  loading: state.customerPortalSettings.loading,
  cancellationManagementEntity: state.cancellationManagement.entity,
  subUpcomingOrderEntities: state.subscriptionBillingAttempt.entity,
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  getPortalSettings,
  getvalidCustomerEntity,
  getCancellationManagementEntity,
  splitSubscriptionContract,
  updateAttemptBillingEntity,
  getCustomerUpcomingOrderEntity,
  getSubscriptionContractDetailsByContractId
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionsList);

import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import axios from 'axios';
import moment from 'moment';
import { toast } from 'react-toastify';
import { getCustomerPortalEntity as getCustomerInfoEntity, getShipingCountryCodes, getShopifyCustomerPaymentDetails } from 'app/entities/customers/customer.reducer';
import { Link } from 'react-router-dom';
import { formatPrice, getCustomerIdentifier, redirectToAccountPage, urlParamsToObject } from 'app/shared/util/customer-utils';
import { KeyboardArrowLeft } from '@mui/icons-material';
import CustomerPortalUpcoming from './CustomerPortalUpcoming';
import EditBillingDate from './EditBillingDateV2';
import EditOrderNote from './EditOrderNote';
import EditFrequency from './EditFrequencyV2';
import Pagination from 'react-js-pagination';
import { getUpcomingOrderEntityList } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { getEntity as getCancellationManagementEntity } from 'app/entities/cancellation-management/cancellation-management.reducer';
import { getCustomerSubscriptionContractDetailsByContractId as getSubscriptionContractDetailsByContractId } from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
import {
  getCustomerUpcomingOrderEntity,
  getFailedOrderEntity,
  skipCustomerBillingOrder
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { getCustomerPortalSettingEntity as getPortalSettings } from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {
  addDiscountCode,
  applyDiscountOnCustomerPortal,
  deleteCustomerEntity,
  deleteOneOffProducts,
  getCustomerPortalEntity,
  getSubscriptionFreezeStatus,
  removeDiscountOnCustomerPortal,
  updateCustomerBillingDate,
  updateCustomerBillingInterval,
  updateCustomerDeliveryPrice,
  updateCustomerPaymentDetail,
  updateCustomerExistingPaymentDetail,
  updateCustomerShippingAddress
} from 'app/entities/subscriptions/subscription.reducer';
import QuickActionDetail from './QuickActionDetail';
import SwapProductCustomer from './AddProductModelStep/SwapProductCustomer';
import AddUpSellProduct from './AddProductModelStep/AddUpSellProduct';
import { Tab } from '@headlessui/react';
import SubscriptionDetailProduct from './SubscriptionDetailProduct';
import { DateTime } from 'luxon';
import ShippingDetails from './ShippingDetailsV2';
import UpdatePayment from './UpdatePayment';
import SubscriptionAction from './SubscriptionAction';
import CancelSubscription from './CancelSubscriptionV2';
import SubscriptionDetailProductOneTime from './SubscriptionDetailProductOneTime';
import SubscriptionDiscount from './SubscriptionDiscount';
import Loader from './Loader';
import { getCurrentCycle, getSellingPlans } from 'app/entities/subscription-group/subscription-group.reducer';
import AdditionalOrderDetails from './AdditionalOrderDetails';
import MagicLinkForm from './MagicLinkForm';
import { Card, CardBody } from 'reactstrap';
import OrderAttributes from './OrderAttributes';
import getSymbolFromCurrency from 'currency-symbol-map';
import SubscriptionDetailSingleProduct from 'app/CustomerV3/Subscription/SubscriptionDetailSingleProduct';
import BandBox from './BandBox';
import { isOneTimeProduct, isFreeProduct } from 'app/shared/util/subscription-utils';
import CustomerPortalFulfilment from './CustomerPortalFulfilment';
import DeliveryScheduledDate from './DeliveryScheduledDate';

// import productMockData from "./AddProductModelStep/productMockData";
// IMPORT END

const SubscriptionDetails = ({ ...props }) => {
  const {
    loading,
    updating,
    deleteCustomerEntity,
    subUpcomingOrderEntities,
    updateCustomerShippingAddress,
    updatingShippingAddress,
    subscriptionEntities,
    updateCustomerBillingInterval,
    updateCustomerBillingDate,
    updatingDeliveryPrice,
    skipCustomerBillingOrder,
    updateCustomerPaymentDetail,
    getCustomerInfoEntity,
    removeBuffer,
    getCustomerUpcomingOrderEntity,
    updateCustomerDeliveryPrice,
    getCustomerPortalEntity,
    updatingPayment,
    customerInfoEntity,
    updatePaymentSuccess,
    getPortalSettings,
    getSubscriptionContractDetailsByContractId,
    customerPortalSettingEntity,
    getCancellationManagementEntity,
    cancellationManagementEntity,
    subscriptionContractDetails,
    removeDiscountOnCustomerPortal,
    removeDiscountInProgress,
    applyDiscountOnCustomerPortal,
    skippedBillingAttempt,
    discountAppliedData,
    deleteSubscriptionMessage,
    getSubscriptionFreezeStatus,
    subscriptionContractFreezeStatus,
    subscriptionContractFreezeStatusMessage,
    deleteOneOffProducts,
    getFailedOrderEntity,
    subFailedOrderEntities,
    failedOrderLoading,
    totalFailedBillingAttemptItems,
    getSellingPlans,
    sellingPlanData,
    upcomingOrderDetailsEntity,
    getUpcomingOrderEntityList,
    getCurrentCycle,
    customerEntity,
    getShipingCountryCodes,
    countriesCodes,
    currentCycle,
    loadingCustomer,
    showRedirectMessage,
    shopInfo,
    subscriptionLoading,
    getShopifyCustomerPaymentDetails,
    customerPaymentDetails,
    updateCustomerExistingPaymentDetail
  } = props;

  const options = {
    autoClose: 500,
    position: toast.POSITION.TOP_CENTER
  };

  const contractId = props.match.params.id;
  const customerId = getCustomerIdentifier();
  if (!customerId) {
    redirectToAccountPage();
  }
  const shopName = Shopify.shop;

  const [accordion, setAccordion] = useState([false, false, false, false, false]);
  const [paymentData, setPaymentData] = useState(subscriptionEntities?.customerPaymentMethod?.instrument);
  const [editBilling, setEditBilling] = useState(false);
  const [updatedNextOrderDate, setUpdatedNextOrderDate] = useState();
  const [isEditNextOrder, setIsEditNextOrder] = useState(false);
  const [isUpdateShippingOpenFlag, setIsUpdateShippingOpenFlag] = useState(false);
  const [isPrepaid, setIsPrepaid] = useState(
    subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true
  );
  const [isEditDeliveryPrice, setIsEditDeliveryPrice] = useState(false);
  const [updatingContractStatus, setUpdatingContractStatus] = useState(false);
  const [shippingLabelData, setShippingLabelData] = useState('');

  const [updatePaymentButtonText, setUpdatePaymentButtonText] = useState(customerPortalSettingEntity?.updatePaymentBtnText);
  const [variantQuantityList, setVariantQuantityList] = useState([]);
  const [upcomingSwapProduct, setUpcomingSwapProduct] = useState([]);
  const [upcomingOneTimeVariantList, setUpcomingOneTimeVariantList] = useState([]);
  const [globalDiscounts, setGlobalDiscounts] = useState([]);

  const [nextOrderDateChangedData, setNextOrderDateChangedData] = useState({});
  const [attributeEdit, setAttributeEdit] = useState(false);
  const [productIds, setProductIds] = useState([]);
  const [invalidDiscountCode, setInvalidDiscountCode] = useState(false);
  const [activeOneTimeProducts, setActiveOneTimeProducts] = useState([]);
  const [errorMessages, setErrorMessages] = useState({
    pauseBtnMessage: '',
    pauseBtnMessageColor: '',
    removeSubscriptionBtnMessage: '',
    removeSubscriptionBtnMessageColor: '',
    discountBtnMsg: ''
  });

  const [isQuickActionAvailable, setIsQuickActionAvailable] = useState(false);
  const [currentQueryParams, setCurrentQueryParams] = useState({});
  const [isSwapProductModalOpen, setIsSwapProductModalOpen] = useState(false);
  const [addProductModalOpen, setAddProductModalOpen] = useState(false);

  const [pastOrderActivePage, setPastOrderActivePage] = useState(1);
  const [pastOrderTotalRowData, setPastOrderTotalRowData] = useState(0);
  const [pastOrderItemsPerPage, setPastOrderItemsPerPage] = useState(10);
  const [oneTimeProductsInSubscription, setOneTimeProductsInSubscription] = useState([]);
  const [recurringProductsInSubscription, setRecurringProductsInSubscription] = useState([]);
  const [freeProductsInSubscription, setFreeProductsInSubscription] = useState([]);

  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [pastOrderIds, setPastOrderIds] = useState([]);
  const [fulfillmentLoading, setFulfillmentLoading] = useState(false);
  const [fulfillmentOrderEntity, setFulfillmentOrderEntity] = useState([]);

  const [babDetails, setBabDetails] = useState(null);
  const loadDetails = () => {
    getCustomerPortalEntity(props.match.params.id);
  };

  let queryStringObj = urlParamsToObject(location.search);
  useEffect(() => {
    loadDetails();
    getPortalSettings(props.match.params.id);
    getCustomerInfoEntity(customerId);
    getCancellationManagementEntity(0);
    getSubscriptionContractDetailsByContractId(props.match.params.id);
    if (window?.parent?.postMessage) {
      window?.parent?.postMessage('appstle_scroll_top');
    }
    getSubscriptionFreezeStatus(props.match.params.id);
    if (queryStringObj['quickAction']) {
      setIsQuickActionAvailable(true);
      setCurrentQueryParams(queryStringObj);
    }
  }, [contractId]);

  useEffect(() => {
    getSellingPlans();
    getShipingCountryCodes();
  }, []);

  useEffect(() => {
    if (customerEntity && customerEntity?.id) {
      getShopifyCustomerPaymentDetails(`/${customerEntity?.id?.split('/').pop()}/payment-methods`)
      window.top['appstleCustomerId'] = customerEntity?.id?.split('/').pop();
    }
  }, [customerEntity]);

  const [sellingPlanName, setSellingPlanName] = useState('');
  useEffect(() => {
     if (subscriptionEntities?.lines?.edges && subscriptionEntities?.lines?.edges?.length > 0) {
      setSellingPlanName(subscriptionEntities.lines.edges[0]?.node?.sellingPlanName);
     }
     let productAttributes = [];
     let babAttributes = [];
     subscriptionEntities?.lines?.edges.forEach(prd => {
      productAttributes = [...productAttributes, ...prd?.node?.customAttributes]
     });
     productAttributes.forEach(prdattr => {
        if (prdattr?.key == "_appstle-bb-id") {
          babAttributes = [...babAttributes, prdattr?.value]
        }
     })

     babAttributes = [...new Set(babAttributes)];

     if (babAttributes?.length === 1) {
      axios(`/api/v3/subscription-bundlings/external/get-bundle/${babAttributes[0]}`).then(res => {
        if (res.data) {
          setBabDetails(res.data);
        }
      })
     }
  }, [subscriptionEntities]);

  useEffect(() => {
    let variantArray = [];
    subscriptionEntities?.lines?.edges?.map((prd) => {
      if (!isOneTimeProduct(prd) && !isFreeProduct(prd))
      variantArray = [...variantArray, { variantId: prd?.node?.variantId?.split('/').pop(), quantity: prd?.node.quantity }];
    });
    setVariantQuantityList([...variantArray]);
    extractGlobalDiscounts();
    setProductIds(
      subscriptionEntities?.lines?.edges?.map((prd, index) => {
        return prd?.node?.productId?.split('/')?.pop();
      })
    );
    if (subscriptionEntities?.status === 'CANCELLED') {
      if (window?.parent?.postMessage) {
        window?.parent?.postMessage('appstle_scroll_top');
      }
    }
    if (contractId) {
      getCurrentCycle(contractId);
    }
  }, [subscriptionEntities]);

  useEffect(() => {
    if (contractId && subscriptionContractDetails?.customerId && shopName) {
      getFailedOrderEntity(contractId, subscriptionContractDetails?.customerId, shopName, pastOrderActivePage - 1, pastOrderItemsPerPage);
    }
  }, [subscriptionContractDetails, subscriptionEntities, shopName, pastOrderActivePage]);

  useEffect(() => {
    setPastOrderTotalRowData(totalFailedBillingAttemptItems || 0);
  }, [totalFailedBillingAttemptItems]);

  const handlePaginationPastOrder = activePage => {
    setPastOrderActivePage(activePage || 1);
  };

  useEffect(() => {
    if (subscriptionEntities?.lines?.edges.length > 0 && variantQuantityList.length > 0) {
      var data = {
        variantQuantityList: variantQuantityList
      };

      axios.post(`api/product-swaps-by-variant-groups/${contractId}?isExternal=true`, data).then(res => {
        setUpcomingSwapProduct(res.data);
      });
    }
  }, [variantQuantityList]);

  useEffect(() => {
    let upcomingOneTimeVariants = [];
    let subscriptionContractOneoff = `api/subscription-contract-one-offs-by-contractId?contractId=${contractId}`;
    axios
      .get(subscriptionContractOneoff)
      .then(async resp => {
        if (resp?.data?.length > 0) {
          let globalIndex = 0;
          for await (let item of resp?.data) {
            try {
              globalIndex = globalIndex + 1;
              const productvariantUrl = `${location.origin}/products/${item.variantHandle}.js`;
              await axios
                .get(productvariantUrl)
                .then(res => {
                  // res.data = productMockData[globalIndex - 1]
                  let varData = res.data?.variants.find(variant => variant.id == item.variantId);
                  let upcomingOneTimeVariantsData = {
                    billingID: item?.billingAttemptId,
                    prdImage: `https:${res.data?.featured_image}`,
                    title: res.data?.title,
                    variantTitle: res.data?.variants?.length > 1 ? ' - ' + varData?.title : '',
                    id: varData?.id,
                    price: varData?.price,
                    quantity: item?.quantity,
                    description: res.data?.description || res.data?.body_html
                  };
                  upcomingOneTimeVariants.push(upcomingOneTimeVariantsData);
                  if (globalIndex === resp?.data?.length) {
                    setUpcomingOneTimeVariantList([...upcomingOneTimeVariants]);
                  }
                })
                .catch(err => {
                  console.log(err);
                });
            } catch (error) {
              console.log(error);
              continue;
            }
          }
        } else {
          setUpcomingOneTimeVariantList([]);
        }
      })
      .catch(err => {
        console.log(err);
      });
  }, [subscriptionEntities]);

  useEffect(() => {
    if (!isPrepaid && subUpcomingOrderEntities?.length > 0 && upcomingOneTimeVariantList?.length > 0) {
      let activeOneTimeVariants = [];
      subUpcomingOrderEntities?.map((ordData, index) => {
        let activeOneTimeVariantObj = upcomingOneTimeVariantList?.filter(function(item) {
          return item?.billingID == ordData?.id;
        });
        activeOneTimeVariants = [...activeOneTimeVariants, ...activeOneTimeVariantObj];
      });
      setActiveOneTimeProducts(activeOneTimeVariants);
    } else {
      setActiveOneTimeProducts([]);
    }
  }, [subUpcomingOrderEntities, upcomingOneTimeVariantList]);

  useEffect(() => {
    if (activeOneTimeProducts?.length > 0) {
    let subscriptionTotal = 0;
    subscriptionEntities?.lines?.edges?.map((prd, index) => {
      subscriptionTotal = subscriptionTotal + parseFloat(prd?.node?.lineDiscountedPrice?.amount);
    });

    let otpTotal = 0;
    activeOneTimeProducts.map((prd, index) => {
      otpTotal = otpTotal + parseFloat(prd.price) / 100;
    });

    let subTotal = parseFloat(subscriptionTotal) + parseFloat(otpTotal);
      setContractSubTotal(formatPrice(subTotal?.toFixed(2)*100));
    }
  }, [activeOneTimeProducts]);

  useEffect(() => {
    let checkPrepaid =
      subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true;
    if (contractId && checkPrepaid) {
      getUpcomingOrderEntityList(contractId);
    }
  }, [subscriptionEntities, isPrepaid]);

  useEffect(() => {
    if (customerPortalSettingEntity?.customerPortalSettingJson && customerPortalSettingEntity?.customerPortalSettingJson?.length) {
      let settingData = customerPortalSettingEntity?.customerPortalSettingJson;

      settingData = settingData.split('{');
      settingData[0] = '';
      settingData = settingData.join('{');

      settingData = settingData.split('}');
      settingData[settingData?.length - 1] = '';
      settingData = settingData.join('}');

      settingData = settingData.trim();

      setShippingLabelData(JSON.parse(settingData));
    }
  }, [customerPortalSettingEntity]);

  // custom variable. need to integrate at customer portal

  const extractGlobalDiscounts = () => {
    if (subscriptionEntities?.discounts?.edges?.length > 0) {
      let globalDiscountArray = [];
      subscriptionEntities?.discounts?.edges?.forEach(discountNode => {
        let isCurrentDiscountNodeGlobal = true;
        subscriptionEntities?.lines?.edges?.forEach(productNode => {
          let productNodeDiscountArray = [];
          productNode?.node?.discountAllocations?.forEach(productDiscount => {
            productNodeDiscountArray?.push(productDiscount?.discount?.id);
          });
          if (productNodeDiscountArray?.length && productNodeDiscountArray?.indexOf(discountNode?.node?.id) === -1) {
            isCurrentDiscountNodeGlobal = false;
          }
        });
        if (isCurrentDiscountNodeGlobal && discountNode) {
          globalDiscountArray?.push(JSON.parse(JSON.stringify(discountNode)));
        }
      });
      setGlobalDiscounts([...globalDiscountArray]);
    } else {
      setGlobalDiscounts([]);
      return;
    }
  };

  useEffect(() => {
    if (discountAppliedData) {
      if (Object.keys(discountAppliedData).length > 0) {
        if (discountAppliedData?.discounts?.edges?.length > 0) {
          setInvalidDiscountCode(false);
          toast.success(customerPortalSettingEntity?.discountCouponAppliedText || 'Discount coupon applied', options);
        } else {
          setInvalidDiscountCode(true);
          toast.error(customerPortalSettingEntity?.discountCouponNotAppliedText || 'Invalid discount coupon', options);
        }
      }
    }
  }, [discountAppliedData]);

  useEffect(() => {
    if (deleteSubscriptionMessage) {
      if (Object.keys(deleteSubscriptionMessage).length > 0) {
        setErrorMessages({
          ...errorMessages,
          removeSubscriptionBtnMessage: deleteSubscriptionMessage?.response?.data?.message,
          removeSubscriptionBtnMessageColor: 'danger'
        });
        console.log(deleteSubscriptionMessage?.response?.data?.message);
      }
    }
  }, [deleteSubscriptionMessage]);

  useEffect(() => {
    if (contractId && customerId) getCustomerUpcomingOrderEntity(contractId, customerId);
    let total = 0;
    subscriptionEntities?.lines?.edges?.map((prd, index) => {
      total = total + parseFloat(prd?.node?.lineDiscountedPrice?.amount);
    });
    setContractSubTotal(formatPrice(parseFloat(total).toFixed(2)*100));
  }, [subscriptionEntities]);

  useEffect(() => {
    setPaymentData(subscriptionEntities?.customerPaymentMethod?.instrument);
    setIsPrepaid(subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true);
  }, [subscriptionEntities]);

  useEffect(() => {
    setEditBilling(false);
    setIsEditNextOrder(false);
    setIsEditDeliveryPrice(false);
  }, [props.updateSuccess]);
  useEffect(() => {
    if (!updatingPayment && updatePaymentSuccess) {
      setUpdatePaymentButtonText(customerPortalSettingEntity?.successText);
      let timer = setTimeout(() => {
        setUpdatePaymentButtonText(customerPortalSettingEntity?.updatePaymentBtnText);
        clearTimeout(timer);
      }, 1000);
    }
  }, [updatingPayment, updatePaymentSuccess]);

  const toggleAccordion = tab => {
    const state = accordion.map((x, index) => (tab === index ? !x : false));
    setUpdatePaymentButtonText(customerPortalSettingEntity?.updatePaymentBtnText);
    setAccordion(state);
  };

  const skipShipment = async (ordDataId, isPrepaid) => {
    return await skipCustomerBillingOrder(isPrepaid ? ordDataId?.split('/').pop() : ordDataId, contractId, customerId, isPrepaid);
  };
  const editPaymentInfo = async (isAddNewPaymentMethod, paymentMethodId) => {
    if (isAddNewPaymentMethod) {
      return await updateCustomerPaymentDetail(contractId);
    } else {
      return await updateCustomerExistingPaymentDetail(contractId, paymentMethodId);
    }
  };

  useEffect(() => {
    if (Object.keys(nextOrderDateChangedData)?.length > 0 && subUpcomingOrderEntities) {
      // for pay as you go plan
      var currentUpcomingOrderDate = moment(nextOrderDateChangedData?.nextBillingDate).format('MMMM DD, YYYY');
      var updatedUpcomingOrderDate;
      if (subUpcomingOrderEntities?.length > 0) {
        updatedUpcomingOrderDate = moment(
          subUpcomingOrderEntities?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))[0]?.billingDate
        ).format('MMMM DD, YYYY');
      }
      // popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
    }
  }, [subUpcomingOrderEntities]);

  useEffect(() => {
    if (skippedBillingAttempt && subscriptionEntities && subUpcomingOrderEntities) {
      var currentUpcomingOrderDate = moment(subscriptionEntities?.nextBillingDate).format('MMMM DD, YYYY');
      var updatedUpcomingOrderDate;
      if (subUpcomingOrderEntities?.length > 0) {
        updatedUpcomingOrderDate = moment(
          subUpcomingOrderEntities?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))[0]?.billingDate
        ).format('MMMM DD, YYYY');
      }
      // popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
    }
  }, [skippedBillingAttempt]);

  const [contractSubTotal, setContractSubTotal] = useState(0);

  const popUpWhenUpcomingOrderChange = (currentUpcomingOrderDate, updatedUpcomingOrderDate) => {
    if (currentUpcomingOrderDate && updatedUpcomingOrderDate) {
      if (currentUpcomingOrderDate !== updatedUpcomingOrderDate) {
        console.log(moment(updatedNextOrderDate).format('MMMM DD, YYYY'));
        swal({
          title: customerPortalSettingEntity?.upcomingOrderChangePopupFailureTitleText || 'Try Again!',
          text:
            customerPortalSettingEntity?.upcomingOrderChangePopupFailureDescriptionText ||
            'Something wend wrong. Please update the subscription contract again!',
          icon: 'error',
          button: customerPortalSettingEntity?.upcomingOrderChangePopupFailureClosebtnText || 'close'
        });
      } else if (currentUpcomingOrderDate === updatedUpcomingOrderDate) {
        swal({
          title: customerPortalSettingEntity?.upcomingOrderChangePopupSuccessTitleText || 'Successfully Updated!',
          text:
            customerPortalSettingEntity?.upcomingOrderChangePopupSuccessDescriptionText ||
            'Your subscription was updated successfully. Please still confirm your update by reviewing upcoming orders section.',
          icon: 'success',
          button: customerPortalSettingEntity?.upcomingOrderChangePopupSuccessClosebtnText || 'okay'
        });
      }
      setNextOrderDateChangedData([]);
    }
  };

  const [tooltipOpen, setTooltipOpen] = useState(false);

  const toggle = () => setTooltipOpen(!tooltipOpen);

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  let detailborderstyle = {
    borderRadius: '15px',
    border: '1px solid darkblue',
    textAlign: 'center',
    boxShadow: '7px 8px #84818b'
  };
  let frequencyIntervalTranslate = {
    week: customerPortalSettingEntity?.weekText,
    day: customerPortalSettingEntity?.dayText,
    month: customerPortalSettingEntity?.monthText,
    year: customerPortalSettingEntity?.yearText
  };
  const getFrequencyTitle = interval => {
    return frequencyIntervalTranslate[interval?.toLowerCase(interval)] ? frequencyIntervalTranslate[interval?.toLowerCase()] : interval;
  };

  const updateShippingAddressMethod = async updateShipping => {
    return await updateCustomerShippingAddress(updateShipping, contractId);
    toggleUpdateShippingModal();
  };

  const toggleUpdateShippingModal = () => setIsUpdateShippingOpenFlag(!isUpdateShippingOpenFlag);

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
          getCustomerPortalEntity(props.match.params.id);
          if (status == 'PAUSED') {
            setErrorMessages({
              ...errorMessages,
              pauseBtnMessage: customerPortalSettingEntity.subscriptionPausedMessageText,
              pauseBtnMessageColor: 'success'
            });
            toast.success(customerPortalSettingEntity.subscriptionPausedMessageText, options);
          } else {
            setErrorMessages({
              ...errorMessages,
              pauseBtnMessage: customerPortalSettingEntity.subscriptionActivatedMessageText,
              pauseBtnMessageColor: 'success'
            });
            toast.success(customerPortalSettingEntity.subscriptionActivatedMessageText, options);
          }
        } else {
          setErrorMessages({
            ...errorMessages,
            pauseBtnMessage: customerPortalSettingEntity.unableToUpdateSubscriptionStatusMessageText,
            pauseBtnMessageColor: 'danger'
          });
          toast.error(customerPortalSettingEntity.unableToUpdateSubscriptionStatusMessageText);
          setUpdatingContractStatus(false);
        }
        return res;
      })
      .catch(err => {
        setUpdatingContractStatus(false);
        window.scroll({
          top: 0,
          left: 0,
          behavior: 'smooth'
        });
        setErrorMessages({
          ...errorMessages,
          pauseBtnMessage: err.response.data.message,
          pauseBtnMessageColor: 'danger'
        });
        if (window?.parent?.postMessage) {
          window?.parent?.postMessage('appstle_scroll_top');
        }
        toast.error(err.response.data.message, options);
        return err;
      });
  };

  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleClick = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
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

  function classNames(...classes) {
    return classes.filter(Boolean).join(' ');
  }

  const open = Boolean(anchorEl);
  const id = open ? 'simple-popover' : undefined;

  let [categories] = useState({
    Upcoming: [],
    Scheduled: [],
    History: []
  });

  const tabsDataPrepaid = [
    {
      key: 'Upcoming',
      value: customerPortalSettingEntity?.upcomingTabTitleV2 ?? 'Upcoming'
    },
    {
      key: 'Fulfilments',
      value: customerPortalSettingEntity?.fulfilmentTabTitleV2 ?? 'Fulfilments'
    },
    {
      key: 'Scheduled',
      value: customerPortalSettingEntity?.scheduledTabTitleV2 ?? 'Scheduled'
    },
    {
      key: 'History',
      value: customerPortalSettingEntity?.historyTabTitleV2 ?? 'History'
    }
  ];

  const tabsData = [
    {
      key: 'Upcoming',
      value: customerPortalSettingEntity?.upcomingTabTitleV2 ?? 'Upcoming'
    },
    {
      key: 'Scheduled',
      value: customerPortalSettingEntity?.scheduledTabTitleV2 ?? 'Scheduled'
    },
    {
      key: 'History',
      value: customerPortalSettingEntity?.historyTabTitleV2 ?? 'History'
    }
  ];

  const [tabs, setTabs] = useState(tabsData);

  useEffect(() => {
    let tabs = []
    if (isPrepaid) {
      tabs = tabsDataPrepaid
      // setTabs(tabsDataPrepaid)
    } else {
      tabs = tabsData
      // setTabs(tabsData)
    }
    if (!subUpcomingOrderEntities.length) {
      tabs = tabs.filter(item => item.key !== 'Scheduled')
    }
    setTabs(tabs)
  }, [isPrepaid, customerPortalSettingEntity, subUpcomingOrderEntities])

  useEffect(() => {
    let oneTimeProducts = [];
    let reccurringProducts = [];
    let freeProducts = [];
    subscriptionEntities?.lines?.edges?.forEach((prd) => {
      if (isOneTimeProduct(prd) && !isFreeProduct(prd)) {
        oneTimeProducts.push(prd)
      } else if (isFreeProduct(prd)) {
        freeProducts.push(prd)
      } else {
        reccurringProducts.push(prd)
      }
    })

    setRecurringProductsInSubscription([...reccurringProducts]);
    setOneTimeProductsInSubscription([...oneTimeProducts]);
    setFreeProductsInSubscription([...freeProducts]);
  }, [subscriptionEntities])

  const replaceStringWithSpecificWord = (textMsg, replaceWord, newReplaceableWord) => {
    if (textMsg && replaceWord) {
      return textMsg?.replace(replaceWord, newReplaceableWord || '');
    } else {
      return textMsg;
    }
  };

  useEffect(() => {
    let orderId = upcomingOrderDetailsEntity?.id?.split('/')?.pop();
    if (orderId) {
      let pastOrders = [...pastOrderIds];
      setSelectedOrderId(orderId);
      if (pastOrders.indexOf(orderId) === -1) {
        setPastOrderIds([orderId, ...pastOrders]);
      }
    }
  }, [upcomingOrderDetailsEntity]);

  useEffect(() => {
    if (selectedOrderId) {
      setFulfillmentLoading(true);
      axios
        .get(`/api/subscription-contract-details/subscription-fulfillments/order/${selectedOrderId}`)
        .then(res => {
          setFulfillmentOrderEntity(res.data);
          setFulfillmentLoading(false);
        })
        .catch(err => {
          console.log(err);
        });
    }
  }, [selectedOrderId, upcomingOrderDetailsEntity]);

  useEffect(() => {
    if (subFailedOrderEntities && subFailedOrderEntities.length) {
      let currentPastOrderIds = [...pastOrderIds];
      subFailedOrderEntities.map(item => {
        if (item?.graphOrderId) {
          let id = item?.graphOrderId.split('/').pop();
          if (id && currentPastOrderIds?.indexOf(id) === -1) {
            currentPastOrderIds.push(id);
          }
        }
      });
      setPastOrderIds([...currentPastOrderIds]);
    }
  }, [subFailedOrderEntities]);

  let customDisplayTextShop = ["funkyfood.com.au"];
  return (
    <>
    { !window?.showBandBoxCP ?
    <>
      {loading || subscriptionLoading || loadingCustomer || !subscriptionEntities?.lines?.edges?.length ? (
            <div
              style={{ margin: '10% 0 0 43%', flexDirection: 'column' }}
              className="loader-wrapper d-flex justify-content-center align-items-center"
            >
              <div class="appstle_preloader appstle_loader--big"></div>
            </div>
          ) : (
             <>
             {subscriptionEntities?.billingPolicy?.minCycles &&
        props.currentCycle &&
        props.currentCycle < subscriptionEntities?.billingPolicy?.minCycles &&
        customerPortalSettingEntity?.freezeUpdateSubscriptionMessageV2 && (
          <div
            class="as-p-4 as-mb-4 as-text-sm as-text-yellow-700 as-bg-yellow-100 as-rounded-lg  as-alert as-subscription-freeze"
            role="alert"
          >
            {replaceStringWithSpecificWord(
              customerPortalSettingEntity?.freezeUpdateSubscriptionMessageV2,
              '{{minCycles}}',
              subscriptionEntities?.billingPolicy?.minCycles
            )}
            {}
          </div>
        )}
        <div className="as-flex as-justify-between">
          <div class="as-flex as-items-center as-customer-info as-mb-4">
            {(window?.appstleCustomerData && appstleCustomerData?.customerId) && <>
              <img
                className="as-w-14"
                src={`https://ui-avatars.com/api/?rounded=true&name=${appstleCustomerData?.firstName}%20${appstleCustomerData?.lastName}`}
              />
              <div className="as-ml-3">
                <h2 className="as-customer-info-name as-text-lg">
                  {customerPortalSettingEntity?.helloNameText || `Hello`}{' '}
                  {appstleCustomerData?.firstName || appstleCustomerData?.lastName ? (
                    <>
                      <span className="appstle_customer_firstName">{appstleCustomerData?.firstName}</span>{' '}
                      <span className="appstle_customer_lastName">{appstleCustomerData?.lastName}</span>{' '}
                    </>
                  ) : (
                    <span className="appstle_greeting_text">{customerPortalSettingEntity?.greetingText || 'There'}</span>
                  )}
                </h2>
                {appstleCustomerData?.customerId && (
                  <div className="as-text-sm">
                    {customerPortalSettingEntity?.customerIdText || `Customer ID`}: {appstleCustomerData?.customerId}
                  </div>
                )}
              </div>
            </>}
          </div>
          {subscriptionEntities && subscriptionEntities?.lines?.edges?.length > 0 && (
            <>
              <Link to={`/subscriptions/list`} className="appstle_contract_see_more" style={{ marginTop: '15px' }}>
                <button className="appstle_order-detail_update-button" style={{ paddingLeft: '0' }}>
                  <KeyboardArrowLeft style={{ height: '18px' }} /> {customerPortalSettingEntity?.goBackButtonText || `Go Back`}
                </button>
              </Link>
            </>
          )}
        </div>
      {!loadingCustomer && showRedirectMessage ? (
        !customerPortalSettingEntity?.magicLinkEmailFlag ? (
          <Card
            style={{
              margin: '28px 0px',
              borderRadius: '8px 8px 0 0',
              boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'
            }}
          >
            <CardBody>
              <div
                dangerouslySetInnerHTML={{ __html: customerPortalSettingEntity?.expiredTokenText }}
                className="as-expiredTokenText as-subscriptionListError"
              ></div>
            </CardBody>
          </Card>
        ) : (
          <MagicLinkForm customerPortalSettingEntity={customerPortalSettingEntity} />
        )
      ) : (
        ''
      )}
      {!loadingCustomer && !showRedirectMessage && (
        <>
          {customerInfoEntity && Object.keys(customerInfoEntity).length > 0 && (
            <div class="as-flex as-items-center as-my-8 as-subscription-detail-header">
              <p className="as-text-xl as-text-gray-900 as-font-medium as-subscription-detail-header_title">
                <span className="as-label">
                  {replaceStringWithSpecificWord(replaceStringWithSpecificWord(
                    customerPortalSettingEntity?.subscriptionNoText,
                    '{{subscriptionFrequency}}',
                    (subscriptionEntities?.billingPolicy?.intervalCount || '') +
                    ' ' +
                    (getFrequencyTitle(subscriptionEntities?.billingPolicy?.interval) || '')
                  ), '{{sellingPlanName}}', sellingPlanName)} {' '}
                </span>
                <span className="as-label as-subscription-id">#{subscriptionEntities?.id?.split('/')[4]}</span>
              </p>

              {subscriptionEntities.status == 'ACTIVE' ? (
                <span className="as-bg-green-100 as-text-green-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-active-badge as-status">
                  {customerPortalSettingEntity?.activeBadgeText}
                </span>
              ) : subscriptionEntities?.status == 'CANCELLED' ? (
                <span className="as-bg-red-100 as-text-red-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-close-badge as-status">
                  {customerPortalSettingEntity?.closeBadgeText}
                </span>
              ) : subscriptionEntities?.status == 'PAUSED' ? (
                <span className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-pause-badge as-status">
                  {customerPortalSettingEntity?.pauseBadgeText}
                </span>
              ) : (
                ''
              )}
            </div>
          )}
          {shopInfo?.passwordEnabled != null && shopInfo?.passwordEnabled ? (
            <div
              className="as-bg-orange-100 as-border-t-4 as-border-orange-500 as-rounded-b as-text-orange-900 as-px-4 as-py-3 as-shadow-md"
              role="alert"
            >
              <div className="as-flex">
                <div className="as-py-1">
                  <svg
                    className="as-fill-current as-h-6 as-w-6 as-text-orange-500 as-mr-4"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 20 20"
                  >
                    <path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z" />
                  </svg>
                </div>
                <div>
                  <p className="as-font-bold">Store is password protected.</p>
                  <p className="as-text-sm">
                    Please make sure the store is not password protected as some features, such as showing the products list and the product
                    search, won't function properly.
                  </p>
                </div>
              </div>
            </div>
          ) : (
            ''
          )}
          <div className="as-w-full as-px-2 as-my-8 sm:as-px-0 as-subscription-detail-wrapper">
          <Tab.Group>
              <Tab.List className="as-flex as-space-x-0.5 as-rounded-lg as-overflow-hidden as-shadow as-tab-list">
                {tabs?.map(category => (
                  <Tab
                    key={category?.key}
                    className={({ selected }) =>
                      classNames(
                        'as-w-full as-py-4 as-text-sm as-leading-5 as-font-medium as-text-gray-500 as-bg-white as-tab-item',
                        'focus:as-outline-none focus:as-text-gray-600',
                        selected ? 'as-text-gray-900 as-border-b-2 as-border-indigo-500' : 'as-text-gray-500'
                      )
                    }
                  >
                    {category?.value}
                  </Tab>
                ))}
              </Tab.List>
              <Tab.Panels className="as-mt-4 as-tab-content-wrapper">
                <Tab.Panel className={classNames('as-rounded-lg p-3 as-tab-content')}>
                  {customerPortalSettingEntity?.upcomingTabHeaderHTML && <div className='appstle-upcomming-html appstle-cp-tab-header' dangerouslySetInnerHTML={{__html: customerPortalSettingEntity?.upcomingTabHeaderHTML}} />}
                  <div className="as-grid as-gap-4 lg:as-grid-cols-12 as-items-start">
                    <div className="lg:as-col-span-8 as-full-width as-panel-left">
                    {subscriptionContractDetails.subscriptionType != null &&
                      subscriptionContractDetails?.subscriptionType === 'BUILD_A_BOX_SINGLE_PRODUCT' ? (
                        <div>
                          {subscriptionEntities?.lines?.edges?.map((prd, index) => {
                            return (
                              <>
                                <SubscriptionDetailSingleProduct
                                  prd={prd}
                                  customerPortalSettingEntity={customerPortalSettingEntity}
                                  attributeEdit={attributeEdit}
                                  setAttributeEdit={setAttributeEdit}
                                  subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                  contractId={contractId}
                                  shopName={shopName}
                                  getCustomerPortalEntity={getCustomerPortalEntity}
                                  index={index}
                                  subscriptionEntities={subscriptionEntities}
                                  subUpcomingOrderEntities={subUpcomingOrderEntities}
                                  key={`1${index}`}
                                  sellingPlan={prd?.node?.sellingPlanId}
                                  sellingPlanData={sellingPlanData}
                                  subscriptionContractDetails={subscriptionContractDetails}
                                />
                              </>
                            );
                          })}

<div className={'as-float-right'}>
                            <div className="as-table-row as-subtotal-price">
                              <div className="as-px-6 as-pt-4 as-pb-1 as-text-right as-text-lg as-font-medium as-text-gray-900">
                                <span className="as-subtotal as-subtotal-label">
                                  {customerPortalSettingEntity?.subTotalLabelTextV2}
                                  <span className="colon-symbol">:</span>
                                </span>{' '}
                                <span className="as-subtotal as-subtotal-value">
                                  {contractSubTotal}
                                </span>
                              </div>
                            </div>
                            <div className="as-table-row as-delivery-price">
                              <div className="as-px-6 as-text-sm as-text-gray-800 as-text-right as-pb-4">
                                <span className="as-delivery-price as-delivery-price-label">
                                  {customerPortalSettingEntity?.deliveryPriceText}
                                  <span className="colon-symbol">:</span>
                                </span>{' '}
                                <span className="as-delivery-price as-delivery-price-value">
                                  {formatPrice(parseFloat(subscriptionEntities?.deliveryPrice?.amount)?.toFixed(2)*100)}
                                </span>
                              </div>
                            </div>
                          </div>
                        </div>
                        ) : (
                          <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-card as-product-details">
                          <div>
                            <div className="as-flex as-justify-between as-items-center as-mb-1 as-product-details_header">
                              <p className="as-text-sm as-font-medium as-text-gray-500 as-py-2 as-p-4 as-product-details_title">
                                {customerPortalSettingEntity?.productInSubscriptionTextV2}
                              </p>
                              {(subscriptionEntities.status == 'ACTIVE' && !isPrepaid) &&
                                (customerPortalSettingEntity?.addAdditionalProduct || customerPortalSettingEntity?.addOneTimeProduct) && (
                                  <p
                                    className=" as-p-4 as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-add-product-cta"
                                    onClick={() => setAddProductModalOpen(true)}
                                  >
                                    {customerPortalSettingEntity?.addProductButtonTextV2 || 'Add Product'}
                                  </p>
                                )}
                            </div>
                            <div class="as-relative as-overflow-x-auto as-product-details-table-wrapper">
                              <table class="as-w-full as-text-sm as-text-left as-text-gray-500 as-product-details-table">
                                <thead class="as-text-xs as-text-gray-700 as-uppercase as-bg-gray-50 as-table-head">
                                  <tr>
                                    <th scope="col" class="as-px-6 as-py-3">
                                      <span className="as-table-head-label">
                                        {customerPortalSettingEntity?.productLblText || 'Product'}
                                      </span>
                                    </th>
                                    <th scope="col" class="as-px-6 as-py-3 as-hidden md:as-table-cell">
                                      <span className="as-table-head-label">{customerPortalSettingEntity?.amountLblV2}</span>
                                    </th>
                                    <th scope="col" class="as-px-6 as-py-3 as-hidden md:as-table-cell">
                                      <span className="as-table-head-label">{customerPortalSettingEntity?.quantityLbl}</span>
                                    </th>
                                    <th scope="col" class="as-px-6 as-py-3 as-hidden md:as-table-cell">
                                      <span className="as-table-head-label">{customerPortalSettingEntity?.totalLblText || 'Total'}</span>
                                    </th>
                                    {customerPortalSettingEntity?.deleteProductFlag && (
                                      <th scope="col" class="as-px-6 as-py-3">
                                        <span className="as-sr-only as-action-icon"></span>
                                      </th>
                                    )}
                                  </tr>
                                </thead>
                                <tbody>
                                {subscriptionEntities?.lines?.edges?.map((prd, index) => {
                                  return (
                                    prd?.node &&
                                    prd?.node != null && (
                                      <SubscriptionDetailProduct
                                        prd={prd}
                                        customerPortalSettingEntity={customerPortalSettingEntity}
                                        attributeEdit={attributeEdit}
                                        setAttributeEdit={setAttributeEdit}
                                        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                        contractId={contractId}
                                        shopName={shopName}
                                        getCustomerPortalEntity={getCustomerPortalEntity}
                                        index={index}
                                        subscriptionEntities={subscriptionEntities}
                                        subUpcomingOrderEntities={subUpcomingOrderEntities}
                                        key={`1${index}`}
                                        sellingPlan={prd?.node?.sellingPlanId}
                                        sellingPlanData={sellingPlanData}
                                        subscriptionContractDetails={subscriptionContractDetails}
                                        recurringProductsInSubscription={recurringProductsInSubscription}
                                        freeProductsInSubscription={freeProductsInSubscription}
                                        oneTimeProductsInSubscription={oneTimeProductsInSubscription}
                                        babDetails={babDetails}
                                      />
                                    )
                                  );
                                })}
                                {activeOneTimeProducts?.length > 0 &&
                                  activeOneTimeProducts?.map((prd, index) => {
                                    return (
                                      <SubscriptionDetailProductOneTime
                                        prd={prd}
                                        customerPortalSettingEntity={customerPortalSettingEntity}
                                        attributeEdit={attributeEdit}
                                        setAttributeEdit={setAttributeEdit}
                                        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                        contractId={contractId}
                                        shopName={shopName}
                                        getCustomerPortalEntity={getCustomerPortalEntity}
                                        index={index}
                                        subscriptionEntities={subscriptionEntities}
                                        subUpcomingOrderEntities={subUpcomingOrderEntities}
                                        deleteOneOffProducts={deleteOneOffProducts}
                                        key={`2${index}`}
                                      />
                                    );
                                  })}
                                <tr className="as-hidden md:as-table-row as-subtotal-price">
                                  <th scope="row"></th>
                                  <td
                                    className="as-px-6 as-pt-4 as-pb-1 as-text-right as-text-lg as-font-medium as-text-gray-900"
                                    colSpan={customerPortalSettingEntity?.deleteProductFlag ? 4 : 3}
                                  >
                                    <span className="as-subtotal as-subtotal-label">
                                        {customerPortalSettingEntity?.subTotalLabelTextV2}
                                        <span className="colon-symbol">:</span>
                                      </span>{' '}
                                      <span className="as-subtotal as-subtotal-value">
                                        {contractSubTotal}
                                      </span>
                                    </td>
                                </tr>
                                <tr className="as-hidden md:as-table-row as-delivery-price">
                                  <th scope="row"></th>
                                  <td
                                    className="as-px-6 as-text-sm as-text-gray-800 as-text-right as-pb-4"
                                    colSpan={customerPortalSettingEntity?.deleteProductFlag ? 4 : 3}
                                  >
                                    <span className="as-delivery-price as-delivery-price-label">
                                        {customerPortalSettingEntity?.deliveryPriceText}
                                        <span className="colon-symbol">:</span>
                                      </span>{' '}
                                      <span className="as-delivery-price as-delivery-price-value">
                                        {formatPrice(parseFloat(subscriptionEntities?.deliveryPrice?.amount)?.toFixed(2)*100)}
                                      </span>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                              <div className="as-flex as-justify-end as-bg-gray-50 md:as-hidden as-subtotal-price">
                                <p className="as-px-6 as-pt-4 as-pb-1 as-text-right as-font-medium as-text-gray-900">
                                  {customerPortalSettingEntity?.subTotalLabelTextV2}
                                  <span className="colon-symbol">:</span>{' '}
                                  {contractSubTotal}
                                </p>
                              </div>
                              <div className="as-flex as-justify-end as-bg-gray-50 md:as-hidden as-delivery-price">
                                <p className="as-px-6 as-pb-4 as-pt-1 as-text-right as-font-medium as-text-gray-900">
                                  {customerPortalSettingEntity?.deliveryPriceText}
                                  <span className="colon-symbol">:</span>{' '}
                                  {formatPrice(parseFloat(subscriptionEntities?.deliveryPrice?.amount)?.toFixed(2)*100)}
                                </p>
                              </div>
                            </div>
                          </div>
                        </div>
                      )}

                        {(subscriptionContractDetails.subscriptionType == null ||
                        subscriptionContractDetails?.subscriptionType !== 'BUILD_A_BOX_SINGLE_PRODUCT') &&
                        subscriptionEntities?.lines?.edges?.length &&
                        (customerPortalSettingEntity?.addAdditionalProduct || customerPortalSettingEntity?.addOneTimeProduct) &&
                        subscriptionEntities?.status === 'ACTIVE' && (
                          <div className="as-hidden lg:as-block">
                            <AddUpSellProduct
                              upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
                              fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
                              sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                                return line?.node?.sellingPlanId;
                              })}
                              shopName={shopName}
                              contractId={contractId}
                              subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                              customerPortalSettingEntity={customerPortalSettingEntity}
                              isSwapProduct={false}
                              forcePurchaseOption={(customerPortalSettingEntity?.selectOneTimePurchaseTabByDefaultV2 === 'true') ? 'ONE_TIME' : 'SUBSCRIBE'}
                            />
                          </div>
                        )}
                    </div>
                    <div className="lg:as-col-span-4 as-grid as-gap-4 as-full-width as-panel-right">
                      {subscriptionEntities?.status == 'ACTIVE' && subUpcomingOrderEntities && subUpcomingOrderEntities.length > 0 && (
                        <EditBillingDate
                          subscriptionEntity={subscriptionEntities}
                        />
                      )}

                      {(subscriptionEntities?.status == 'ACTIVE' || subscriptionEntities?.status == 'PAUSED') && (
                        <EditFrequency
                          subscriptionEntity={subscriptionEntities}
                        />
                      )}

                      {isPrepaid &&
                        <div class="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-delivery-frequency">
                          <div class="as-flex as-justify-between as-mb-2">
                            <p class="as-text-sm as-text-gray-500 as-card_title as-edit-frequency_title" dangerouslySetInnerHTML={{__html: customerPortalSettingEntity.deliveryIntervalLabelTextV2 || "Delivery Interval <span className='colon-symbol'>:</span>"}} />
                          </div>
                          <h3 class="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-pt-3 as-card_data as-edit-frequency_data">
                            <span className="as-value">
                              {customerPortalSettingEntity?.everyLabelTextV2} {' '} {subscriptionEntities?.deliveryPolicy?.intervalCount} {' '} {(getFrequencyTitle(subscriptionEntities?.deliveryPolicy?.interval, subscriptionEntities?.deliveryPolicy?.intervalCount))}
                            </span>
                          </h3>
                        </div>
                      }

                      {customerPortalSettingEntity?.orderNoteFlag &&
                        (subscriptionEntities?.status == 'ACTIVE' || subscriptionEntities?.status == 'PAUSED') && (
                          <EditOrderNote
                            subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                            contractId={contractId}
                            shopName={shopName}
                            orderNote={subscriptionContractDetails.orderNote}
                            subscriptionContractDetails={subscriptionContractDetails}
                          />
                        )}

                      <AdditionalOrderDetails isPrepaid={isPrepaid} />
                      <OrderAttributes />

                      <SubscriptionDiscount
                        subscriptionEntities={subscriptionEntities}
                        customerPortalSettingEntity={customerPortalSettingEntity}
                        removeDiscountInProgress={removeDiscountInProgress}
                        applyDiscountOnCustomerPortal={applyDiscountOnCustomerPortal}
                        contractId={contractId}
                        shopName={shopName}
                        removeDiscountOnCustomerPortal={removeDiscountOnCustomerPortal}
                        globalDiscounts={globalDiscounts}
                        customerEntity={customerEntity}
                      />
                      <ShippingDetails
                        subscriptionEntity={subscriptionEntities}
                      />

                      <UpdatePayment
                        paymentData={paymentData}
                        customerPortalSettingEntity={customerPortalSettingEntity}
                        updateCustomerPaymentDetail={editPaymentInfo}
                        updatePaymentButtonText={updatePaymentButtonText}
                        updatingPayment={updatingPayment}
                        customerPaymentDetails={customerPaymentDetails}
                        customerPaymentMethod={subscriptionEntities?.customerPaymentMethod}
                        getCustomerPortalEntity={loadDetails}
                        subscriptionContractDetails={subscriptionContractDetails}
                      />
                      {(subscriptionContractDetails?.cancellationFeedback || subscriptionContractDetails?.cancellationNote) ? (
                        <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-payment">
                          <div className="as-flex as-justify-between as-mb-2">
                            <p className="as-text-sm as-text-gray-500 as-card_title as-edit-payment_title">
                            {customerPortalSettingEntity?.cancelAccordionTitle || "Cancellation Reason"}
                            </p>
                          </div>
                          <p class="as-text-sm as-text-gray-800 as-pt-3 as-card_data as-edit-payment_data">
                            <p className="as-card-type">
                              <span className="as-label">
                              {customerPortalSettingEntity?.cancellationDateTitleText || 'Cancellation Date'}
                                <span className='colon-symbol'>:</span>&nbsp;
                                {DateTime.fromISO(subscriptionContractDetails?.cancelledOn).toFormat(customerPortalSettingEntity?.dateFormat)}
                              </span>
                            </p>
                            <p className="as-card-type">
                              <span className="as-label">
                                {customerPortalSettingEntity?.selectedCancellationReasonTitleText || 'Selected Cancellation Reason'}
                                <span className='colon-symbol'>:</span>&nbsp; {subscriptionContractDetails?.cancellationFeedback}
                              </span>
                            </p>
                            <p className="as-card-type">
                              <span className="as-label">
                                {customerPortalSettingEntity?.cancellationNoteTitleText || 'Cancellation Note'}
                                <span className='colon-symbol'>:</span>&nbsp; {subscriptionContractDetails?.cancellationNote}
                              </span>
                            </p>
                          </p>
                        </div>
                      ) : null}
                      <div>
                        <SubscriptionAction
                          customerPortalSettingEntity={customerPortalSettingEntity}
                          subscriptionEntities={subscriptionEntities}
                          updateContractStatus={updateContractStatus}
                          checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
                          subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                          updatingContractStatus={updatingContractStatus}
                        />
                        <CancelSubscription
                          contractId={parseInt(contractId)}
                          subscriptionEntity={subscriptionEntities}
                          currentCycle={props?.currentCycle}
                        />
                      </div>
                      <SwapProductCustomer
                        upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
                        fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
                        sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                          return line?.node?.sellingPlanId;
                        })}
                        shopName={shopName}
                        contractId={contractId}
                        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                        customerPortalSettingEntity={customerPortalSettingEntity}
                        setIsSwapProductModalOpen={setIsSwapProductModalOpen}
                        isSwapProductModalOpen={isSwapProductModalOpen}
                        addProductModalOpen={addProductModalOpen}
                        setAddProductModalOpen={setAddProductModalOpen}
                        isSwapProduct={false}
                      />
                    </div>
                  </div>
                </Tab.Panel>
                {isPrepaid && <Tab.Panel>
                  {isPrepaid &&
                    fulfillmentOrderEntity?.fulfillmentOrders?.edges
                      .sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))
                      .map((fulfillmentData, index) => {
                          return (
                            <>
                              <CustomerPortalFulfilment
                                contractId={contractId}
                                key={index}
                                indexKey={index}
                                upcomingSwapProductList={!index ? undefined : upcomingSwapProduct[index - 1]}
                                skipShipment={skipShipment}
                                isPrepaid={isPrepaid}
                                isFulfillment={true}
                                ordData={fulfillmentData?.node}
                                shopName={shopName}
                                subscriptionEntities={subscriptionEntities}
                                updatingSkipOrder={props.updatingSkipOrder}
                                subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
                                setNextOrderDateChangedResponce={setNextOrderDateChangedData}
                                getSubscriptionContractDetailsByContractId={getSubscriptionContractDetailsByContractId}
                              />
                            </>
                          );
                  })}
                </Tab.Panel>}
                {subUpcomingOrderEntities.length > 0 && <Tab.Panel>
                  {customerPortalSettingEntity?.schedulesTabHeaderHTML && <div className='appstle-schedules-html appstle-cp-tab-header' dangerouslySetInnerHTML={{__html: customerPortalSettingEntity?.schedulesTabHeaderHTML}} />}
                  {
                    subUpcomingOrderEntities &&
                    subUpcomingOrderEntities.length > 0 &&
                    subUpcomingOrderEntities
                      ?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))
                      .map((ordData, index) => {
                        return (
                          <CustomerPortalUpcoming
                            key={ordData.id}
                            indexKey={index}
                            upcomingSwapProductList={!isPrepaid ? (!index ? undefined : upcomingSwapProduct[index - 1]) : upcomingSwapProduct[index] }
                            skipShipment={skipShipment}
                            isPrepaid={isPrepaid}
                            upcomingOneTimeVariants={upcomingOneTimeVariantList}
                            ordData={ordData}
                            shopName={shopName}
                            subscriptionEntities={subscriptionEntities}
                            updatingSkipOrder={props.updatingSkipOrder}
                            contractId={contractId}
                            subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                            checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
                            activeOneTimeProducts={activeOneTimeProducts}
                            setNextOrderDateChangedResponce={setNextOrderDateChangedData}
                            isFulfillment={false}
                            isUpcomingNextOrder={!index}
                            subUpcomingOrderEntities={subUpcomingOrderEntities}
                            subscriptionContractDetails={subscriptionContractDetails}
                            getSubscriptionContractDetailsByContractId={getSubscriptionContractDetailsByContractId}
                          />
                        );
                      })}
                </Tab.Panel>}
                <Tab.Panel>
                  {customerPortalSettingEntity?.historyTabHeaderHTML && <div className='appstle-history-html appstle-cp-tab-header' dangerouslySetInnerHTML={{__html: customerPortalSettingEntity?.historyTabHeaderHTML}} />}
                  {failedOrderLoading ? (
                    <div className="as-flex as-justify-center">
                      <Loader />
                    </div>
                  ) : (
                    <div>
                      {subFailedOrderEntities &&
                        subFailedOrderEntities.length > 0 &&
                        subFailedOrderEntities?.map((ordData, index) => {
                          return (
                            <>
                              <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-my-8">
                                <div className="as-px-4 as-py-5 sm:as-px-6 as-flex as-flex-col lg:as-flex-row as-items-center lg:as-justify-between">
                                  {ordData?.orderName && (
                                    <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-flex as-items-center">
                                      {ordData?.orderName}
                                    </h3>
                                  )}
                                  <p className="as-mt-1 as-max-w-2xl as-text-sm as-text-gray-900">
                                    {(customDisplayTextShop.includes(Shopify?.shop) && window?.top?.customDisplayTextEnabled && ordData?.status != 'SKIPPED') ? (
                                      <DeliveryScheduledDate billingDate={ordData?.billingDate} dateFormat={customerPortalSettingEntity?.dateFormat} displayText={'<span style="font-size: 18px;"> <span style="font-weight: 600;">Billing Date: </span> {{billingDate}} <span style="font-weight: 600;">Delivery Between:</span> {{fromDate}} and {{toDate}}</span>'} />
                                    ) : (
                                      <>
                                        <span className="as-font-medium">{customerPortalSettingEntity?.orderDateTextV2 || 'Order Date'}</span>:{' '}
                                        {DateTime.fromISO(ordData?.billingDate).toFormat(customerPortalSettingEntity?.dateFormat)}
                                      </>
                                    )}
                                  </p>
                                  <div className='as-flex as-mt-2 lg:as-mt-0'>
                                    {ordData.status == 'SUCCESS' ? (
                                      <>
                                         {ordData.orderDisplayFinancialStatus ? <div className="as-orderfinancial-tag as-ml-2 as-bg-orange-100 as-text-orange-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {ordData.orderDisplayFinancialStatus}
                                        </div> : null}
                                        {ordData.orderDisplayFulfillmentStatus ? <div className="as-orderfulfilment-tag as-ml-2 as-bg-pink-100 as-text-pink-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                          {ordData.orderDisplayFulfillmentStatus}
                                        </div> : null}
                                        {ordData.orderCancelledAt ? <div className="as-order-cancelled-tag as-ml-2 as-bg-cyan-100 as-text-cyan-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                          {ordData.orderCancelLabelText || "Canceled"}
                                        </div> : null}
                                        {ordData.orderClosed ? <div className="as-order-closed-tag as-ml-2 as-bg-blue-100 as-text-blue-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                          {ordData.orderArchivedLabelText || "Archived"}
                                        </div> : null}
                                      </>
                                    ) : ordData.status == 'SKIPPED' ? (
                                      <div className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {customerPortalSettingEntity.skipBadgeTextV2 || ordData.status}
                                      </div>
                                    ) : ordData.status == 'SKIPPED_INVENTORY_MGMT' ? (
                                      <div className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {customerPortalSettingEntity.skippedInventoryMGMTTextV2 || ordData.status}
                                      </div>
                                    ) : ordData.status == 'CONTRACT_CANCELLED' ? (
                                      <div className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {customerPortalSettingEntity?.contractCancelledBadgeText || ordData.status}
                                      </div>
                                    ) : ordData.status == 'FAILURE' ? (
                                      <div className="as-bg-red-100 as-text-red-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {customerPortalSettingEntity?.failureText || ordData.status}
                                      </div>
                                    ) : (
                                      <div className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full">
                                        {ordData?.status?.split('_').join(' ') === 'CONTRACT PAUSED'
                                          ? customerPortalSettingEntity?.contractPausedTextV2
                                            ? customerPortalSettingEntity?.contractPausedTextV2
                                            : ordData?.status?.split('_').join(' ')
                                          : ordData?.status?.split('_').join(' ')}
                                      </div>
                                    )}
                                  </div>
                                </div>
                                <div className="as-border-t as-border-b as-border-gray-200 as-px-4 as-py-5 sm:as-px-6">
                                  <div className="as-grid as-gap-4 lg:as-grid-cols-2">
                                    {ordData && ordData.variantList && ordData.variantList.length > 0
                                      ? ordData.variantList.map(prd => {
                                          return (
                                            <div className="as-grid as-grid-cols-4 as-gap-4 as-items-center">
                                              <div className="as-col-span-1">
                                                <img
                                                  src={
                                                    prd.productId == null
                                                      ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                                                      : prd?.image
                                                  }
                                                  alt=""
                                                  className="as-w-full"
                                                />
                                            </div>
                                            <div className="as-col-span-3">
                                              {prd?.title && (
                                                <>
                                                  <p
                                                    className={`as-text-sm as-text-gray-800 as-mb-2 ${
                                                      !prd?.productId ? 'as-line-through' : ''
                                                    }`}
                                                  >
                                                    {prd?.title}
                                                  </p>
                                                  {!prd.productId && (
                                                    <span>
                                                        &nbsp;{customerPortalSettingEntity?.hasBeenRemovedText || 'has been removed'}
                                                      </span>
                                                  )}
                                                </>
                                              )}
                                              {prd?.variantTitle && prd?.variantTitle !== '-' && prd?.variantTitle !== 'Default Title' && (
                                                <>
                                                  <p className="as-text-xs as-text-gray-500">
                                                      <span className="as-font-medium as-text-gray-600">
                                                        {customerPortalSettingEntity?.variantLblText || 'Variant'}:{' '}
                                                      </span>
                                                    {prd?.variantTitle}
                                                  </p>
                                                </>
                                              )}
                                              {prd?.quantity && (
                                                <p className="as-text-xs as-text-gray-500">
                                                    <span className="as-font-medium as-text-gray-600">
                                                      {customerPortalSettingEntity?.quantityLbl || 'Quantity'}:
                                                    </span>{' '}
                                                  {prd?.quantity}
                                                </p>
                                              )}
                                            </div>
                                          </div>
                                        );
                                      })
                                      : subscriptionEntities?.lines?.edges?.map(prd => {
                                        let isValidProduct = prd?.node && prd?.node != null && prd?.node?.productId != null;
                                        return (
                                         (ordData?.status == 'SKIPPED' && isOneTimeProduct(prd)) ? <></> :
                                          <div className="as-grid as-grid-cols-4 as-gap-4 as-items-center">
                                            <div className="as-col-span-1">
                                              <img
                                                src={
                                                  !isValidProduct
                                                    ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
                                                    : prd?.node?.variantImage?.transformedSrc
                                                }
                                                alt=""
                                                className="as-w-full"
                                              />
                                            </div>
                                            <div className="as-col-span-3">
                                              {prd?.node?.title && (
                                                <>
                                                  <p
                                                    className={`as-text-sm as-text-gray-800 as-mb-2 ${
                                                      !isValidProduct ? 'as-line-through' : ''
                                                    }`}
                                                  >
                                                    {prd?.node?.title}
                                                  </p>
                                                  {!isValidProduct && (
                                                    <span>
                                                        &nbsp;{customerPortalSettingEntity?.hasBeenRemovedText || 'has been removed'}
                                                      </span>
                                                  )}
                                                </>
                                              )}
                                              {prd?.node?.variantTitle &&
                                                prd?.node?.variantTitle !== '-' &&
                                                prd?.node?.variantTitle !== 'Default Title' && (
                                                  <>
                                                    <p className="as-text-xs as-text-gray-500">
                                                        <span className="as-font-medium as-text-gray-600">
                                                          {customerPortalSettingEntity?.variantLblText || 'Variant'}:{' '}
                                                        </span>
                                                      {prd?.node?.variantTitle}
                                                    </p>
                                                  </>
                                                )}
                                              {prd?.node?.quantity && (
                                                <p className="as-text-xs as-text-gray-500">
                                                    <span className="as-font-medium as-text-gray-600">
                                                      {customerPortalSettingEntity?.quantityLbl || 'Quantity'}:
                                                    </span>{' '}
                                                  {prd?.node?.quantity}
                                                </p>
                                              )}
                                            </div>
                                          </div>
                                        );
                                      })}
                                  </div>
                                </div>
                                <div className="as-px-4 as-py-5 sm:as-px-6 as-flex as-justify-between">
                                  <p className="as-text-md as-leading-6 as-font-medium as-text-gray-700 as-flex as-items-center">
                                    <span className="as-font-medium">
                                      {customerPortalSettingEntity?.orderTotalText || 'Order Total'}:&nbsp;
                                    </span>
                                    {formatPrice(parseFloat(ordData?.orderAmount).toFixed(2)*100)}
                                  </p>
                                </div>
                              </div>
                            </>
                          );
                        })}
                      <div className="as-mx-auto as-flex as-justify-center as-flex-col as-items-center">
                        <br />
                        <div>
                          <Pagination
                            activePage={pastOrderActivePage}
                            itemsCountPerPage={pastOrderItemsPerPage}
                            totalItemsCount={pastOrderTotalRowData}
                            // pageRangeDisplayed={5}
                            onChange={handlePaginationPastOrder}
                          />
                        </div>
                        <br />
                        {/* <JhiItemCount page={pastOrderActivePage} total={pastOrderTotalRowData} itemsPerPage={pastOrderItemsPerPage} /> */}
                      </div>
                    </div>
                  )}
                </Tab.Panel>
              </Tab.Panels>
            </Tab.Group>
          </div>
        </>
      )}
      {isQuickActionAvailable ? (
        <QuickActionDetail
          currentQueryParams={currentQueryParams}
          setIsQuickActionAvailable={setIsQuickActionAvailable}
          contractId={contractId}
          getCustomerPortalEntity={getCustomerPortalEntity}
        />
      ) : (
        <div class="container clearfix">
          {
            subscriptionEntities &&
            subscriptionEntities?.lines?.edges?.length > 0 && (
              <>
                <Link to={`/subscriptions/list`} className="appstle_contract_see_more" style={{ marginTop: '15px' }}>
                  <button className="appstle_order-detail_update-button" style={{ paddingLeft: '0' }}>
                    <KeyboardArrowLeft style={{ height: '18px' }} /> {customerPortalSettingEntity?.goBackButtonText || `Go Back`}
                  </button>
                </Link>
              </>
            )
          }
        </div>
      )}
             </>
          )}

    </> : ''}
    {window?.showBandBoxCP ? <>
      <BandBox customerEntity={customerEntity}
        customerPortalSettingEntity={customerPortalSettingEntity}
        subscriptionEntities={subscriptionEntities}
        setNextOrderDateChangedResponce={setNextOrderDateChangedData}
        getCustomerPortalEntity={getCustomerPortalEntity}
        contractId={contractId}
        productIds={productIds}
        isPrepaid={isPrepaid}
        upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
        fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
        sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
          return line?.node?.sellingPlanId;
        })}
        activeOneTimeProducts={activeOneTimeProducts}
        history={props?.history}
        loading={loading}
        updateContractStatus={updateContractStatus}
        checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
        updatingContractStatus={updatingContractStatus}
        cancellationManagementEntity={cancellationManagementEntity}
        deleteCustomerEntity={deleteCustomerEntity}
        updating={updating}
        globalDiscounts={globalDiscounts}
        addDiscountCode={addDiscountCode}
        skipShipment={skipShipment}
        subUpcomingOrderEntities={subUpcomingOrderEntities}
        shopName={shopName}
        updatingSkipOrder={props.updatingSkipOrder}
      />
    </> : ''}
    </>
  );
};

const mapStateToProps = state => ({
  account: state.authentication.account,
  subscriptionEntities: state.subscription.entity,
  updating: state.subscription.updating,
  subscriptionLoading: state.subscription.loading,
  updatingBilling: state.subscription.updatingBilling,
  updateSuccess: state.subscription.updateSuccess,
  updatingNextOrderFlag: state.subscription.updatingBillingDate,
  updatingShippingAddress: state.subscription.updatingShippingAddress,
  updatingDeliveryPrice: state.subscription.updatingDeliveryPrice,
  loading: state.customerPortalSettings.loading,
  updatingPayment: state.subscription.updatingPayment,
  updatePaymentSuccess: state.subscription.updatePaymentSuccess,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  cancellationManagementEntity: state.cancellationManagement.entity,
  subscriptionContractDetails: state.userSubscriptionContractDetails.entity,
  removeDiscountInProgress: state.subscription.removeDiscountInProgress,
  discountAppliedData: state.subscription.discountAppliedData,
  subscriptionContractFreezeStatus: state.subscription.subscriptionContractFreezeStatus,
  subscriptionContractFreezeStatusMessage: state.subscription.subscriptionContractFreezeStatusMessage,
  deleteSubscriptionMessage: state.subscription.deleteSubscriptionMessage,
  customerInfoEntity: state.customer.entity,
  countriesCodes: state.customer.countriesCodes,
  subUpcomingOrderEntities: state.subscriptionBillingAttempt.entity,
  updatingSkipOrder: state.subscriptionBillingAttempt.updating,
  skippedBillingAttempt: state.subscriptionBillingAttempt.skippedBillingAttempt,
  subFailedOrderEntities: state.subscriptionBillingAttempt.failedBillingAttempt,
  failedOrderLoading: state.subscriptionBillingAttempt.failedOrderLoading,
  totalFailedBillingAttemptItems: state.subscriptionBillingAttempt.totalFailedBillingAttemptItems,
  sellingPlanData: state.subscriptionGroup.sellingPlanData,
  upcomingOrderDetailsEntity: state.subscriptionContractDetails.upcomingOrders,
  customerEntity: state.customer.entity,
  currentCycle: state.subscriptionGroup.currentCycle,
  loadingCustomer: state.customer.loading,
  showRedirectMessage: state.customer.showRedirectMessage,
  shopInfo: state.shopInfo.entity,
  customerPaymentDetails: state.customer.customerPaymentDetails
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  deleteCustomerEntity,
  getCustomerUpcomingOrderEntity,
  skipCustomerBillingOrder,
  updateCustomerBillingInterval,
  updateCustomerPaymentDetail,
  updateCustomerBillingDate,
  updateCustomerShippingAddress,
  updateCustomerDeliveryPrice,
  getPortalSettings,
  getCancellationManagementEntity,
  getSubscriptionContractDetailsByContractId,
  removeDiscountOnCustomerPortal,
  applyDiscountOnCustomerPortal,
  getCustomerInfoEntity,
  getSubscriptionFreezeStatus,
  deleteOneOffProducts,
  getFailedOrderEntity,
  getSellingPlans,
  getUpcomingOrderEntityList,
  getCurrentCycle,
  getShipingCountryCodes,
  getShopifyCustomerPaymentDetails,
  updateCustomerExistingPaymentDetail
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionDetails);

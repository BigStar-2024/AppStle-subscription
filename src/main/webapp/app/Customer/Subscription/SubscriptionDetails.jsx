import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import axios from 'axios';
import moment from 'moment';
import { toast } from 'react-toastify';
import Popover from '@mui/material/Popover';
import { Alert, Button, Card, CardBody, CardHeader, Col, Collapse, FormFeedback, FormGroup, Input, Label, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getCustomerPortalEntity as getCustomerInfoEntity } from 'app/entities/customers/customer.reducer';
import { faInfoCircle, faLock, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import { formatPrice, getCustomerIdentifier, redirectToAccountPage, urlParamsToObject } from 'app/shared/util/customer-utils';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import CustomerPortalUpdateShippingAddressModel from './CustomerPortalUpdateShippingAddressModel';
import { CardMembership, KeyboardArrowLeft, KeyboardArrowRight, PauseCircleFilled, Payment, PlayCircleFilled } from '@mui/icons-material';
import { AnimatePresence, motion } from 'framer-motion';
import CustomerPortalUpcoming from './CustomerPortalUpcoming';
import EditCustomerContractDetail from './EditCustomerContractDetail';
import EditBillingDate from './EditBillingDate';
import EditOrderNote from './EditOrderNote';
import EditFrequency from './EditFrequency';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import AddNewProduct from './AddProductModelStep/AddNewProduct';
import EditOneOffProduct from './EditOneOffProduct';

// ALL REDUCERS
import { getEntity as getCancellationManagementEntity } from 'app/entities/cancellation-management/cancellation-management.reducer';
import { getCustomerSubscriptionContractDetailsByContractId as getSubscriptionContractDetailsByContractId } from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
import {
  getCustomerUpcomingOrderEntity,
  skipCustomerBillingOrder
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { getCustomerPortalSettingEntity as getPortalSettings } from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {
  applyDiscountOnCustomerPortal,
  deleteCustomerEntity,
  getCustomerPortalEntity,
  getSubscriptionFreezeStatus,
  removeDiscountOnCustomerPortal,
  updateCustomerBillingDate,
  updateCustomerBillingInterval,
  updateCustomerDeliveryPrice,
  updateCustomerPaymentDetail,
  updateCustomerShippingAddress
} from 'app/entities/subscriptions/subscription.reducer';
import AddCustomAttributes from './AddCustomAttributes';
import MessageBox from './MessageBox';
import QuickActionDetail from './QuickActionDetail';
import SwapProductCustomer from './AddProductModelStep/SwapProductCustomer';
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
    subscriptionContractFreezeStatusMessage
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
  const [isAddingProducts, setIsAddingProduct] = useState(false);
  const [isUpdateShippingOpenFlag, setIsUpdateShippingOpenFlag] = useState(false);
  const [updatingShipping, setUpdatingShipping] = useState(false);
  const [isPrepaid, setIsPrepaid] = useState(
    subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true
  );
  const [updatedDeliveryPrice, setUpdatedDeliveryPrice] = useState('');
  const [isEditDeliveryPrice, setIsEditDeliveryPrice] = useState(false);
  const [subAnalyticsData, setSubAnalyticsData] = useState({});
  const [subAnalysisToggleModel, setSubAnalysisToggleModel] = useState(false);
  const [isAnalyticsProcess, setIsAnalyticsProcess] = useState(false);
  const [updatingContractStatus, setUpdatingContractStatus] = useState(false);
  const [showAddProduct, setShowAddProduct] = useState(false);
  const [shippingLabelData, setShippingLabelData] = useState('');

  const [updatePaymentButtonText, setUpdatePaymentButtonText] = useState(customerPortalSettingEntity?.updatePaymentBtnText);
  const [variantQuantityList, setVariantQuantityList] = useState([]);
  const [upcomingSwapProduct, setUpcomingSwapProduct] = useState([]);
  const [discountCode, setDiscountCode] = useState('');
  const [isDiscountCodeInValid, setIsDiscountCodeInValid] = useState(false);
  const [upcomingOneTimeVariantList, setUpcomingOneTimeVariantList] = useState([]);
  const [cancelSubToggle, setCancelSubToggle] = useState(false);
  const [globalDiscounts, setGlobalDiscounts] = useState([]);
  const [cancellationfeedback, setCancellationfeedback] = useState('');
  const [cancellationfeedbackValid, setCancellationfeedbackValid] = useState(true);
  const [nextOrderDateChangedData, setNextOrderDateChangedData] = useState({});
  const [currentNextOrderDate, setCurrentNextOrderDate] = useState('');
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
    window?.parent?.postMessage('appstle_scroll_top');
    getSubscriptionFreezeStatus(props.match.params.id);
    if (queryStringObj['quickAction']) {
      setIsQuickActionAvailable(true);
      setCurrentQueryParams(queryStringObj);
    }
  }, []);

  useEffect(() => {
    let variantArray = [];
    subscriptionEntities?.lines?.edges?.map(({ node }) => {
      variantArray = [...variantArray, { variantId: node?.variantId?.split('/').pop(), quantity: node.quantity }];
    });
    setVariantQuantityList([...variantArray]);
    extractGlobalDiscounts();
    setProductIds(
      subscriptionEntities?.lines?.edges?.map((prd, index) => {
        return prd?.node?.productId?.split('/')?.pop();
      })
    );
    if (subscriptionEntities?.status === 'CANCELLED') {
      window?.parent?.postMessage('appstle_scroll_top');
    }
  }, [subscriptionEntities]);

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
              const productvariantUrl = `https://${window?.appstle_public_domain}/products/${item.variantHandle}.js`;
              await axios
                .get(productvariantUrl)
                .then(res => {
                  let varData = res.data?.variants.find(variant => variant.id == item.variantId);
                  let upcomingOneTimeVariantsData = {
                    billingID: item?.billingAttemptId,
                    prdImage: `https:${res.data?.featured_image}`,
                    variantName: res.data?.title + (res.data?.variants?.length > 1 ? ' - ' + varData?.title : ''),
                    id: varData?.id,
                    price: varData?.price
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
        let activeOneTimeVariantObj = upcomingOneTimeVariantList?.some(function(item) {
          return item?.billingID == ordData?.id;
        });
        activeOneTimeVariants.push(activeOneTimeVariantObj);
      });
      setActiveOneTimeProducts(activeOneTimeVariants);
    }
  }, [subUpcomingOrderEntities, upcomingOneTimeVariantList]);

  useEffect(() => {
    if (customerPortalSettingEntity?.customerPortalSettingJson && customerPortalSettingEntity?.customerPortalSettingJson?.length) {
      setShippingLabelData(JSON.parse(customerPortalSettingEntity?.customerPortalSettingJson));
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

  const cancelSubscription = values => {
    if (cancellationManagementEntity?.cancellationType === 'CUSTOMER_RETENTION_FLOW') {
      if (cancellationfeedback) {
        setCancellationfeedbackValid(true);
        deleteCustomerEntity(contractId, cancellationfeedback);
      } else {
        setCancellationfeedbackValid(false);
        return;
      }
    } else {
      deleteCustomerEntity(contractId, null);
    }
  };

  // const deleteCustomerSubcsriptionWithFeedback = async (contractId, cancellationfeedback) => {
  //   const requestUrl = `/api/subscription-contracts/${contractId}?${
  //     cancellationfeedback ? `cancellationFeedback=${cancellationfeedback}` : ``
  //       }&isExternal=true`;

  //       await axios.delete(requestUrl)
  //       .then(res => {
  //         console.log(res)
  //       }
  //       )
  //       .catch(err => {
  //           console.log(err);
  //       });
  // }

  useEffect(() => {
    if (contractId && customerId) getCustomerUpcomingOrderEntity(contractId, customerId);
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
  const editPaymentInfo = () => {
    updateCustomerPaymentDetail(contractId);
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
      popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
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
      popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
    }
  }, [skippedBillingAttempt]);

  // useEffect(() => {
  //   if(Object.keys(nextOrderDateChangedData)?.length > 0)
  //     {
  //       // for prepaid plans (not much useful)
  //       var currentUpcomingOrderDate = moment(nextOrderDateChangedData?.nextBillingDate).format('MMMM DD, YYYY') ;
  //       var updatedUpcomingOrderDate;
  //       if(isPrepaid && nextOrderDateChangedData?.originOrder?.fulfillmentOrders?.edges?.length)
  //       {
  //         updatedUpcomingOrderDate = moment(nextOrderDateChangedData?.originOrder?.fulfillmentOrders?.edges?.sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))[0]?.node?.fulfillAt)?.format("MMMM DD, YYYY");
  //         console.log(nextOrderDateChangedData?.originOrder?.fulfillmentOrders?.edges.sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))[0]?.node)
  //       }

  //       popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
  //   }
  // }, [nextOrderDateChangedData])

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
    return frequencyIntervalTranslate[interval.toLowerCase(interval)] ? frequencyIntervalTranslate[interval.toLowerCase()] : interval;
  };

  const updateShippingAddressMethod = async updateShipping => {
    await updateCustomerShippingAddress(updateShipping, contractId);
    toggleUpdateShippingModal();
  };

  const toggleUpdateShippingModal = () => setIsUpdateShippingOpenFlag(!isUpdateShippingOpenFlag);

  const updateContractStatus = (contractId, status) => {
    setUpdatingContractStatus(true);
    axios
      .put(`api/subscription-contracts-update-status?contractId=${contractId}&status=${status}&isExternal=true`)
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
      })
      .catch(err => {
        setUpdatingContractStatus(false);
        window.scroll({
          top: 0,
          left: 0,
          behavior: 'smooth'
        });
        setErrorMessages({ ...errorMessages, pauseBtnMessage: err.response.data.message, pauseBtnMessageColor: 'danger' });
        window?.parent?.postMessage('appstle_scroll_top');
        toast.error(err.response.data.message, options);
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
    if (nextBillingDate) {
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

  const replaceStringWithSpecificWord = (textMsg, replaceWord, newReplacableWord) => {
    return textMsg.replace(replaceWord, newReplacableWord);
  };

  const open = Boolean(anchorEl);
  const id = open ? 'simple-popover' : undefined;

  return (
    <Fragment>
      {isQuickActionAvailable ? (
        <QuickActionDetail
          currentQueryParams={currentQueryParams}
          setIsQuickActionAvailable={setIsQuickActionAvailable}
          contractId={contractId}
          getCustomerPortalEntity={getCustomerPortalEntity}
        />
      ) : (
        <div class="container clearfix">
          {loading ? (
            <div
              style={{ margin: '10% 0 0 43%', flexDirection: 'column' }}
              className="loader-wrapper d-flex justify-content-center align-items-center"
            >
              <div class="appstle_preloader appstle_loader--big"></div>
            </div>
          ) : (
            subscriptionEntities &&
            subscriptionEntities?.lines?.edges?.length > 0 && (
              <>
                <Link to={`/subscriptions/list`} className="appstle_contract_see_more" style={{ marginTop: '15px' }}>
                  <button className="appstle_order-detail_update-button" style={{ paddingLeft: '0' }}>
                    <KeyboardArrowLeft style={{ height: '18px' }} /> {customerPortalSettingEntity?.goBackButtonText || `Go Back`}
                  </button>
                </Link>
                <div class="d-flex" style={{ justifyContent: 'space-between', alignItems: 'center', marginTop: '15px' }}>
                  <h2 className="appstle_greeting_header">
                    {customerPortalSettingEntity?.helloNameText || `Hello`}{' '}
                    {appstleCustomerData?.firstName || appstleCustomerData?.lastName ? (
                      <>
                        <span className="appstle_customer_firstName">{appstleCustomerData?.firstName}</span>{' '}
                        <span className="appstle_customer_lastName">
                          {appstleCustomerData?.lastName}
                          {','}
                        </span>{' '}
                      </>
                    ) : (
                      <span className="appstle_greeting_text">
                        {'There'}
                        {','}
                      </span>
                    )}
                  </h2>
                  {appstleCustomerData?.customerId && (
                    <div style={{ fontSize: '20px' }}>
                      {customerPortalSettingEntity?.customerIdText || `Customer ID`}: {appstleCustomerData?.customerId}
                    </div>
                  )}
                </div>
                {subscriptionContractFreezeStatus && (
                  <div className="mt-4 mb-4">
                    <Alert color="warning">
                      <div style={{ fontSize: '18px', textAlign: 'center' }}>{subscriptionContractFreezeStatusMessage}</div>
                    </Alert>
                  </div>
                )}
                <Card style={{ margin: '20px 0px', borderRadius: '10px', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)' }}>
                  <CardHeader
                    style={{
                      background: '#242222',
                      color: '#fff',
                      padding: '3px 0',
                      borderRadius: '8px 8px 0px 0px',
                      justifyContent: 'center'
                    }}
                  >
                    <p className="appstle_sub-title" style={{ color: '#fff' }}>
                      {' '}
                      <CardMembership /> &nbsp;
                      <span className="appstle-subscription-text">
                        {replaceStringWithSpecificWord(
                          customerPortalSettingEntity?.subscriptionNoText,
                          '{{subscriptionFrequency}}',
                          subscriptionEntities.billingPolicy.intervalCount +
                            ' ' +
                            getFrequencyTitle(subscriptionEntities.billingPolicy.interval)
                        )}
                      </span>
                      &nbsp;
                      <span className="appstle-subscription-id"># {subscriptionEntities?.id?.split('/')[4]}</span>
                    </p>
                  </CardHeader>
                  <CardBody>
                    <Row>
                      <Col md={4} className="appstle-text-center">
                        {subscriptionEntities.lines?.edges.map(
                          contractItem =>
                            contractItem.node.productId !== null && (
                              <img
                                src={contractItem.node?.variantImage?.transformedSrc}
                                alt=""
                                style={{
                                  maxHeight: '100%',
                                  alignSelf: 'flex-start',
                                  padding: '5px',
                                  borderRadius: '2px',
                                  flexGrow: 1,
                                  maxWidth: '150px',
                                  marginBottom: '15px'
                                }}
                              />
                            )
                        )}
                      </Col>
                      <Col md={8}>
                        {
                          <p className="appstle_sub-title  appstle_subscription_contract_title">
                            {subscriptionEntities.billingPolicy.intervalCount +
                              ' ' +
                              getFrequencyTitle(subscriptionEntities.billingPolicy.interval)}{' '}
                            {/* {customerPortalSettingEntity?.subscriptionNoText} */}
                            {replaceStringWithSpecificWord(
                              customerPortalSettingEntity?.subscriptionNoText,
                              '{{subscriptionFrequency}}',
                              ''
                            )}
                            <span
                              style={{
                                marginLeft: '20px',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center'
                              }}
                            >
                              {subscriptionEntities.status == 'ACTIVE' ? (
                                <span className={`appstle_badge appstle_${subscriptionEntities.status}`}>
                                  {customerPortalSettingEntity?.activeBadgeText}
                                </span>
                              ) : subscriptionEntities.status == 'CANCELLED' ? (
                                <span className={`appstle_badge appstle_${subscriptionEntities.status}`}>
                                  {customerPortalSettingEntity?.closeBadgeText}
                                </span>
                              ) : subscriptionEntities.status == 'PAUSED' ? (
                                <span className={`appstle_badge appstle_${subscriptionEntities.status}`}>
                                  {customerPortalSettingEntity?.pauseBadgeText}
                                </span>
                              ) : (
                                ''
                              )}
                            </span>
                          </p>
                        }
                        {subscriptionEntities.status == 'ACTIVE' && subUpcomingOrderEntities && subUpcomingOrderEntities.length > 0 && (
                          <EditBillingDate
                            subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                            contractId={contractId}
                            shopName={shopName}
                            setNextOrderDateChangedResponce={setNextOrderDateChangedData}
                          />
                        )}
                        {/* { subscriptionEntities.status == 'ACTIVE' &&
                        <EditMinCycle contractId={contractId} shopName={shopName} />
                      }
                      { subscriptionEntities.status == 'ACTIVE' &&
                        <EditMaxCycle contractId={contractId} shopName={shopName} />
                      } */}
                        {customerPortalSettingEntity?.orderNoteFlag && (
                          <EditOrderNote
                            subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                            contractId={contractId}
                            shopName={shopName}
                            orderNote={subscriptionContractDetails.orderNote}
                          />
                        )}
                        {(subscriptionEntities.status == 'ACTIVE' || subscriptionEntities.status == 'PAUSED') && (
                          <>
                            <EditFrequency
                              isPrepaid={isPrepaid}
                              contractId={contractId}
                              shopName={shopName}
                              setNextOrderDateChangedResponce={setNextOrderDateChangedData}
                              productIds={productIds}
                              subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                              sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                                return line?.node?.sellingPlanId;
                              })}
                            />
                            {/* <EditDeliveryFrequency contractId={contractId} shopName={shopName} /> */}
                            <p className="deliveryPriceTextWrapper">
                              <b>{customerPortalSettingEntity?.deliveryPriceText}</b>{' '}
                              {parseFloat(subscriptionEntities?.deliveryPrice.amount)?.toFixed(2)}{' '}
                              {subscriptionEntities?.deliveryPrice?.currencyCode}
                            </p>
                            {subscriptionEntities?.deliveryMethod?.shippingOption?.title && (
                              <p className="shippingOptionTextWrapper">
                                <b>{customerPortalSettingEntity?.shippingOptionText}</b>{' '}
                                {subscriptionEntities?.deliveryMethod?.shippingOption?.title}
                              </p>
                            )}
                          </>
                        )}
                        <b>{customerPortalSettingEntity?.productLabelTextV2}</b>
                        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                          {subscriptionEntities.lines?.edges.map((contractItem, index) => (
                            <div
                              className={`appstle_font_size ${contractItem.node.productId == null && 'appstle_strike_out'}`}
                              style={{ marginTop: '5px', color: '#13b5ea' }}
                            >
                              {contractItem.node?.title}
                              {contractItem?.node?.variantTitle &&
                                contractItem?.node?.variantTitle !== '-' &&
                                contractItem?.node?.variantTitle !== 'Default Title' &&
                                ' - ' + contractItem?.node?.variantTitle}
                              {contractItem.node?.quantity > 1 ? (
                                <span style={{ marginLeft: '8px' }}>x {contractItem.node?.quantity}</span>
                              ) : (
                                ''
                              )}
                              {contractItem.node.productId == null && (
                                <>
                                  <FontAwesomeIcon
                                    data-for={`Tooltip-${index}`}
                                    data-tip={customerPortalSettingEntity?.productRemovedTooltip}
                                    icon={faInfoCircle}
                                    color="#465661"
                                    style={{
                                      marginLeft: '10px',
                                      cursor: 'pointer',
                                      bottom: '2px',
                                      position: 'relative'
                                    }}
                                  />

                                  <ReactTooltip
                                    html={true}
                                    id={`Tooltip-${index}`}
                                    effect="solid"
                                    delayUpdate={500}
                                    place="right"
                                    border={true}
                                    type="info"
                                  />
                                </>
                              )}
                            </div>
                          ))}
                        </div>
                        {subscriptionEntities.status == 'ACTIVE' &&
                          customerPortalSettingEntity?.discountCode &&
                          (globalDiscounts?.length > 0 ? (
                            globalDiscounts?.map(discount => {
                              return (
                                <>
                                  <div class="d-flex align-items-center justify-content-space-between mt-3 mb-3">
                                    <span>
                                      <b>{customerPortalSettingEntity?.discountCouponAppliedText || 'Discount Coupon Applied'}: </b>{' '}
                                      {discount?.node?.title} @ {discount?.node?.value?.percentage || discount?.node?.value?.amount?.amount}
                                      {discount?.node?.value?.percentage ? '% ' : discount?.node?.value?.amount?.currencyCode}
                                    </span>
                                    <span
                                      className="ml-3 appstle_font_size"
                                      style={{ cursor: 'pointer' }}
                                      onClick={() => removeDiscountOnCustomerPortal(contractId, discount?.node?.id, shopName)}
                                    >
                                      {!removeDiscountInProgress && (
                                        <span
                                          style={{
                                            color: '#13b5ea',
                                            textDecoration: 'underline'
                                          }}
                                        >
                                          {customerPortalSettingEntity?.discountCouponRemoveText || 'Remove'}
                                        </span>
                                      )}
                                      {removeDiscountInProgress && <div className="appstle_loadersmall" />}
                                    </span>
                                  </div>
                                </>
                              );
                            })
                          ) : (
                            <div className="mt-3 mb-3 d-flex">
                              <FormGroup>
                                <Label>
                                  <b>{customerPortalSettingEntity.discountCodeText}</b>
                                </Label>
                                <div className="d-flex">
                                  <div>
                                    <Input
                                      value={discountCode}
                                      invalid={isDiscountCodeInValid}
                                      type="text"
                                      onInput={event => setDiscountCode(event.target.value)}
                                    />
                                    <FormFeedback>Please enter valid number</FormFeedback>
                                    {invalidDiscountCode && (
                                      <FormFeedback style={{ display: 'block' }}>
                                        {customerPortalSettingEntity?.discountCouponNotAppliedText || 'Invalid discount code'}
                                      </FormFeedback>
                                    )}
                                  </div>
                                  <Button
                                    className="ml-2 appstle_font_size"
                                    color="primary"
                                    onClick={() => applyDiscountOnCustomerPortal(contractId, discountCode)}
                                  >
                                    {!removeDiscountInProgress && customerPortalSettingEntity.discountCodeApplyButtonText}
                                    {removeDiscountInProgress && <div className="appstle_loadersmall" />}
                                  </Button>
                                </div>
                              </FormGroup>
                            </div>
                          ))}
                        {
                          <>
                            {customerPortalSettingEntity?.pauseResumeSub && subscriptionEntities.status !== 'CANCELLED' && (
                              <div style={{ marginTop: '20px' }}>
                                <Button
                                  className="appstle_font_size d-flex align-items-center"
                                  disabled={
                                    subscriptionContractFreezeStatus ||
                                    checkIfPreventCancellationBeforeDays(
                                      customerPortalSettingEntity?.preventCancellationBeforeDays,
                                      subscriptionEntities?.nextBillingDate
                                    )
                                  }
                                  color={subscriptionEntities.status == 'ACTIVE' ? 'danger' : 'success'}
                                  onClick={() =>
                                    updateContractStatus(
                                      subscriptionEntities?.id?.split('/').pop(),
                                      subscriptionEntities.status == 'ACTIVE' ? 'PAUSED' : 'ACTIVE'
                                    )
                                  }
                                >
                                  {updatingContractStatus ? (
                                    <div className="d-flex" style={{ alignItems: 'center' }}>
                                      <div className="appstle_loadersmall" />
                                      <span className="ml-2">
                                        {customerPortalSettingEntity?.pleaseWaitLoaderText
                                          ? customerPortalSettingEntity?.pleaseWaitLoaderText
                                          : 'Please Wait..'}
                                      </span>
                                    </div>
                                  ) : (
                                    <>
                                      {subscriptionEntities.status == 'ACTIVE' ? (
                                        <>
                                          {!subscriptionContractFreezeStatus ? (
                                            <PauseCircleFilled style={{ width: '22px', height: '22px', marginRight: '5px' }} />
                                          ) : (
                                            <FontAwesomeIcon style={{ marginRight: '5px' }} icon={faLock} />
                                          )}
                                          {customerPortalSettingEntity?.pauseSubscriptionText}{' '}
                                        </>
                                      ) : (
                                        <>
                                          {!subscriptionContractFreezeStatus ? (
                                            <PlayCircleFilled style={{ width: '22px', height: '22px', marginRight: '5px' }} />
                                          ) : (
                                            <FontAwesomeIcon style={{ marginRight: '5px' }} icon={faLock} />
                                          )}{' '}
                                          {customerPortalSettingEntity?.resumeSubscriptionText}{' '}
                                        </>
                                      )}
                                    </>
                                  )}
                                </Button>
                                {errorMessages?.pauseBtnMessage && (
                                  <MessageBox message={errorMessages?.pauseBtnMessage} color={errorMessages?.pauseBtnMessageColor} />
                                )}
                                {(subscriptionContractFreezeStatus ||
                                  checkIfPreventCancellationBeforeDays(
                                    customerPortalSettingEntity?.preventCancellationBeforeDays,
                                    subscriptionEntities?.nextBillingDate
                                  )) && (
                                  <Alert color="info mt-2">
                                    {subscriptionContractFreezeStatus
                                      ? subscriptionContractFreezeStatusMessage ||
                                        customerPortalSettingEntity?.subscriptionContractFreezeMessageV2 ||
                                        'Your subscription contract is freezed by your shop owner.'
                                      : checkIfPreventCancellationBeforeDays(
                                          customerPortalSettingEntity?.preventCancellationBeforeDays,
                                          subscriptionEntities?.nextBillingDate
                                        ) &&
                                        (replaceStringWithSpecificWord(
                                          customerPortalSettingEntity?.preventCancellationBeforeDaysMessage,
                                          '{{preventDays}}',
                                          customerPortalSettingEntity?.preventCancellationBeforeDays
                                        ) ||
                                          `You can not pause/cancel/skip the subscription before the ${customerPortalSettingEntity?.preventCancellationBeforeDays} days from your next order date.`)}
                                  </Alert>
                                )}
                              </div>
                            )}
                          </>
                        }
                      </Col>
                    </Row>
                  </CardBody>
                </Card>
                <div id="accordion" className="accordion-wrapper mb-3">
                  {subscriptionEntities?.status.toLowerCase() === 'active' && (
                    <Fragment>
                      <Card
                        style={{
                          margin: '40px 0px 20px',
                          borderRadius: '10px',
                          boxShadow: '0 10px 20px 0 rgb(0 0 0 / 8%)'
                        }}
                      >
                        <CardHeader onClick={() => toggleAccordion(0)} aria-expanded={accordion[0]} aria-controls="collapseFour">
                          {/* <i className="header-icon lnr-arrow-up-circle icon-gradient bg-plum-plate"> </i> */}
                          {customerPortalSettingEntity?.productInSubscriptionTextV2}
                          <div
                            style={{
                              ...forward_arrow_icon,
                              display: 'inline-block',
                              transform: accordion[0] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <KeyboardArrowRight fontSize="20px" color="rgba(18, 21, 78, 0.7)" />
                          </div>
                        </CardHeader>
                        <Collapse isOpen={accordion[0]} data-parent="#accordion" id="collapseFour" aria-labelledby="headingFour">
                          <CardBody>
                            {/* {
                            shopName !== "dev-dharmik-test.myshopify.com" &&
                            <div>
                            {customerPortalSettingEntity?.addAdditionalProduct && (
                              <SearchableProductSelect
                                shopName={shopName}
                                contractId={contractId}
                                sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                                  return line?.node?.sellingPlanId
                                })}
                                onComplete={() => {
                                  setShowAddProduct(false);
                                }}
                              />
                            )}
                          </div>
                          } */}
                            {(customerPortalSettingEntity?.addAdditionalProduct || customerPortalSettingEntity?.addOneTimeProduct) && (
                              <AddNewProduct
                                upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
                                isPrepaid={isPrepaid}
                                fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
                                sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                                  return line?.node?.sellingPlanId;
                                })}
                                shopName={shopName}
                                contractId={contractId}
                                subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                customerPortalSettingEntity={customerPortalSettingEntity}
                              />
                            )}

                            {subscriptionEntities?.lines?.edges?.map((prd, index) => {
                              return (
                                prd?.node &&
                                prd?.node != null &&
                                prd?.node?.productId != null && (
                                  <Card
                                    key={prd.id}
                                    style={{
                                      boxShadow: '0 0px 4px 0 rgb(0 0 0 / 15%)',
                                      marginTop: '16px',
                                      backgroundColor: prd?.node?.productId == null ? 'beige' : ''
                                    }}
                                  >
                                    <CardBody>
                                      {prd?.node?.productId != null && (
                                        <Fragment>
                                          <Row>
                                            <Col md={2}>
                                              <img
                                                src={prd?.node?.variantImage?.transformedSrc}
                                                width={100}
                                                style={{ borderRadius: '2px' }}
                                              ></img>
                                            </Col>
                                            <Col md={8}>
                                              <h6 className="mb-2" style={{ color: '#13b5ea' }}>
                                                {prd?.node?.title}{' '}
                                                {prd?.node?.variantTitle &&
                                                  prd?.node?.variantTitle !== '-' &&
                                                  prd?.node?.variantTitle !== 'Default Title' &&
                                                  '-' + prd?.node?.variantTitle}
                                              </h6>
                                              {prd.node?.sellingPlanName && (
                                                <div className="mt-2 d-flex align-items-center">
                                                  <b>{customerPortalSettingEntity?.sellingPlanNameText || 'Selling Plan Name'}: </b>
                                                  <span className={`ml-2`}>{prd.node?.sellingPlanName}</span>
                                                </div>
                                              )}
                                              {prd.node?.pricingPolicy &&
                                                prd.node.pricingPolicy?.cycleDiscounts?.length == 2 &&
                                                prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage !=
                                                  prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage && (
                                                  <div>
                                                    <b>{customerPortalSettingEntity?.discountNoteTitle}: </b>
                                                    <div style={{ padding: '5px 0px', fontSize: '14px' }}>
                                                      <div
                                                        dangerouslySetInnerHTML={{
                                                          __html: customerPortalSettingEntity?.initialDiscountNoteDescription
                                                            ?.split('{{initialProductPrice}}')
                                                            ?.join(
                                                              parseFloat(
                                                                prd.node?.pricingPolicy?.cycleDiscounts[0]?.computedPrice?.amount
                                                              )?.toFixed(2) +
                                                                ' ' +
                                                                prd.node?.pricingPolicy?.cycleDiscounts[0]?.computedPrice?.currencyCode
                                                            )
                                                            ?.split('{{initialDiscount}}')
                                                            ?.join(
                                                              prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.amount
                                                                ? parseFloat(
                                                                    prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.amount
                                                                  )?.toFixed(2) +
                                                                    ' ' +
                                                                    prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue
                                                                      ?.currencyCode
                                                                : prd.node?.pricingPolicy?.cycleDiscounts[0]?.adjustmentValue?.percentage +
                                                                    '%'
                                                            )
                                                        }}
                                                      ></div>

                                                      <div
                                                        dangerouslySetInnerHTML={{
                                                          __html: customerPortalSettingEntity?.afterCycleDiscountNoteDescription
                                                            ?.split('{{numberOfOrderCycle}}')
                                                            ?.join(prd.node?.pricingPolicy?.cycleDiscounts[1]?.afterCycle)
                                                            ?.split('{{afterCycleProductPrice}}')
                                                            ?.join(
                                                              parseFloat(
                                                                prd.node?.pricingPolicy?.cycleDiscounts[1]?.computedPrice?.amount
                                                              )?.toFixed(2) +
                                                                ' ' +
                                                                prd.node?.pricingPolicy?.cycleDiscounts[1]?.computedPrice?.currencyCode
                                                            )
                                                            ?.split('{{afterCycleDiscount}}')
                                                            ?.join(
                                                              prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.amount
                                                                ? parseFloat(
                                                                    prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.amount
                                                                  )?.toFixed(2) +
                                                                    ' ' +
                                                                    prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue
                                                                      ?.currencyCode
                                                                : prd.node?.pricingPolicy?.cycleDiscounts[1]?.adjustmentValue?.percentage +
                                                                    '%'
                                                            )
                                                        }}
                                                      ></div>
                                                    </div>
                                                  </div>
                                                )}
                                              <div className="mt-2">
                                                <b>{customerPortalSettingEntity?.quantityLbl}:</b>
                                                <span className="ml-2">{prd?.node?.quantity}</span>{' '}
                                              </div>
                                              <div className="mt-2 d-flex align-items-center">
                                                <b>{customerPortalSettingEntity?.amountLblV2}: </b>
                                                <div
                                                  className="badge  badge-alternate ml-2"
                                                  style={{ padding: '5px 6px', fontSize: '12px' }}
                                                >
                                                  {formatPrice(parseFloat(prd?.node?.lineDiscountedPrice?.amount)?.toFixed(2)) ||
                                                    parseFloat(prd?.node?.lineDiscountedPrice?.amount).toFixed(2)}{' '}
                                                  <span className={`product-currency-code`}>
                                                    {prd.node?.lineDiscountedPrice?.currencyCode}
                                                  </span>
                                                </div>
                                              </div>
                                              {prd?.node?.discountAllocations?.map(prdDiscountNode => {
                                                let finalnode = null;
                                                let isDiscountGlobal = globalDiscounts?.some(discount => {
                                                  return prdDiscountNode?.discount?.id === discount?.node?.id;
                                                });

                                                if (!isDiscountGlobal) {
                                                  finalnode = subscriptionEntities?.discounts?.edges?.find(discountNode => {
                                                    return discountNode?.node?.id === prdDiscountNode?.discount?.id;
                                                  });
                                                }

                                                if (finalnode) {
                                                  return (
                                                    <p className="mt-2" style={{ wordBreak: 'break-all' }}>
                                                      <b>Discount Coupon Applied: </b>
                                                      {finalnode?.node?.title} @{' '}
                                                      {finalnode?.node?.value?.percentage || finalnode?.node?.value?.amount?.amount}
                                                      {finalnode?.node?.value?.percentage
                                                        ? '% '
                                                        : finalnode?.node?.value?.amount?.currencyCode}
                                                      &nbsp;{' '}
                                                      {!removeDiscountInProgress && (
                                                        <span
                                                          style={{
                                                            color: '#13b5ea',
                                                            textDecoration: 'underline',
                                                            cursor: 'pointer'
                                                          }}
                                                          onClick={() =>
                                                            removeDiscountOnCustomerPortal(contractId, finalnode?.node?.id, shopName)
                                                          }
                                                        >
                                                          delete
                                                        </span>
                                                      )}{' '}
                                                      {removeDiscountInProgress && (
                                                        <span style={{ display: 'inline-block' }} className="appstle_loadersmall" />
                                                      )}
                                                    </p>
                                                  );
                                                }
                                              })}
                                            </Col>
                                            {/* (!((prd?.node?.variantId?.split('/')?.pop() == "41471913853099" || prd?.node?.variantId?.split('/')?.pop() == "41471913885867")  &&  (shopName == 'japan-art-club.myshopify.com' || shopName == 'shop.theotakubox.com')) ||  */}
                                            {!customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(
                                              prd?.node?.variantId?.split('/')?.pop()
                                            ) && (
                                              <EditCustomerContractDetail
                                                contractId={contractId}
                                                lineId={prd?.node?.id}
                                                shop={shopName}
                                                index={index}
                                                totalProductPriceObj={prd?.node?.lineDiscountedPrice}
                                                productQuantity={prd?.node?.quantity}
                                                productId={prd?.node?.productId}
                                                variantId={prd?.node?.variantId}
                                                subscriptionEntities={subscriptionEntities?.lines?.edges}
                                                productPrice={prd?.node?.lineDiscountedPrice?.amount}
                                                currencyCode={subscriptionEntities?.deliveryPrice?.currencyCode}
                                                attributeEdit={attributeEdit}
                                                subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                              />
                                            )}

                                            {customerPortalSettingEntity?.enableViewAttributes && (
                                              <AddCustomAttributes
                                                contractId={contractId}
                                                lineId={prd?.node?.id}
                                                shop={shopName}
                                                currentVariant={prd?.node}
                                                attributeEdit={attributeEdit}
                                                setAttributeEdit={setAttributeEdit}
                                                subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                                customerPortalSettingEntity={customerPortalSettingEntity}
                                              />
                                            )}

                                            {customerPortalSettingEntity?.enableSwapProductFeature &&
                                              !customerPortalSettingEntity?.variantIdsToFreezeEditRemove?.includes(
                                                prd?.node?.variantId?.split('/')?.pop()
                                              ) && (
                                                <SwapProductCustomer
                                                  upcomingOrderId={subUpcomingOrderEntities[0]?.id.toString()}
                                                  lineId={prd?.node?.id}
                                                  fullfillmentId={subscriptionEntities?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
                                                  sellingPlanIds={subscriptionEntities?.lines?.edges?.map(line => {
                                                    return line?.node?.sellingPlanId;
                                                  })}
                                                  shopName={shopName}
                                                  contractId={contractId}
                                                  subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                                  customerPortalSettingEntity={customerPortalSettingEntity}
                                                />
                                              )}
                                          </Row>
                                        </Fragment>
                                      )}
                                    </CardBody>
                                  </Card>
                                )
                              );
                            })}

                            {activeOneTimeProducts?.length > 0 &&
                              activeOneTimeProducts?.map((prd, index) => {
                                return (
                                  <Card
                                    key={prd.id}
                                    style={{
                                      boxShadow: '0 0px 4px 0 rgb(0 0 0 / 15%)',
                                      marginTop: '16px',
                                      backgroundColor: prd?.id == null ? 'beige' : ''
                                    }}
                                  >
                                    <CardBody>
                                      {prd?.id != null && (
                                        <Fragment>
                                          <Row>
                                            <Col md={2}>
                                              <img src={prd?.prdImage} width={100} style={{ borderRadius: '2px' }}></img>
                                            </Col>
                                            <Col md={8}>
                                              <h6 className="mb-2" style={{ color: '#13b5ea' }}>
                                                {prd?.variantName}
                                              </h6>
                                              {prd.node?.sellingPlanName && (
                                                <div className="mt-2 d-flex align-items-center">
                                                  <b>{customerPortalSettingEntity?.sellingPlanNameText || 'Selling Plan Name'}: </b>
                                                  <span className={`ml-2`}>{prd.node?.sellingPlanName}</span>
                                                </div>
                                              )}
                                              <div className="mt-2">
                                                <b>{customerPortalSettingEntity?.quantityLbl}:</b>
                                                <span className="ml-2">1</span>{' '}
                                              </div>
                                              <div className="mt-2 d-flex align-items-center mb-3">
                                                <b>{customerPortalSettingEntity?.amountLblV2}: </b>
                                                <div
                                                  className="badge  badge-alternate ml-2"
                                                  style={{ padding: '5px 6px', fontSize: '12px' }}
                                                >
                                                  {formatPrice(prd?.price)}{' '}
                                                  <span className={`product-currency-code`}>
                                                    {subscriptionEntities?.deliveryPrice?.currencyCode}
                                                  </span>
                                                </div>
                                              </div>
                                              <div>Note: This product is added as One Time purchase only.</div>
                                            </Col>
                                            <EditOneOffProduct
                                              id={prd?.id}
                                              contractId={contractId}
                                              billingId={prd?.billingID}
                                              item={prd}
                                              index={index}
                                            />
                                          </Row>
                                        </Fragment>
                                      )}
                                    </CardBody>
                                  </Card>
                                );
                              })}
                          </CardBody>
                        </Collapse>
                      </Card>
                      {subscriptionEntities.status.toLowerCase() === 'active' && (
                        <Card
                          style={{
                            margin: '20px 0px',
                            borderRadius: '10px',
                            boxShadow: '0 10px 20px 0 rgb(0 0 0 / 8%)'
                          }}
                        >
                          <CardHeader onClick={() => toggleAccordion(4)} aria-expanded={accordion[4]} aria-controls="collapseFour">
                            {/* <i className="header-icon lnr-trash icon-gradient bg-plum-plate"> </i> */}
                            {shippingLabelData?.shippingLabelText}
                            <div
                              style={{
                                ...forward_arrow_icon,
                                display: 'inline-block',
                                transform: accordion[4] ? 'rotate(90deg)' : ''
                              }}
                            >
                              <KeyboardArrowRight fontSize="20px" color="rgba(18, 21, 78, 0.7)" />
                            </div>
                          </CardHeader>
                          <Collapse isOpen={accordion[4]} data-parent="#accordion" id="collapseFour">
                            <CardBody>
                              <div style={{ fontSize: '28px', marginBottom: '15px' }}>
                                {shippingLabelData?.addressHeaderTitleText}
                                {/* {subscriptionEntities.status == 'ACTIVE' && customerPortalSettingEntity?.changeShippingAddress && */}
                                {!subscriptionContractFreezeStatus ? (
                                  !isUpdateShippingOpenFlag &&
                                  customerPortalSettingEntity?.changeShippingAddressFlag && (
                                    <FontAwesomeIcon
                                      icon={faPencilAlt}
                                      style={{
                                        marginLeft: '10px',
                                        cursor: 'pointer',
                                        bottom: '2px',
                                        fontSize: '16px',
                                        position: 'relative'
                                      }}
                                      onClick={() => {
                                        toggleUpdateShippingModal();
                                      }}
                                    />
                                  )
                                ) : (
                                  <FontAwesomeIcon className="ml-2" style={{ fontSize: '18px' }} icon={faLock} />
                                )}
                              </div>
                              {!isUpdateShippingOpenFlag && (
                                <address>
                                  {!subscriptionEntities?.deliveryMethod?.address ? (
                                    <p className="text-muted">
                                      {customerPortalSettingEntity?.shippingAddressNotAvailableText || 'Not Available'}
                                    </p>
                                  ) : (
                                    <>
                                      {subscriptionEntities?.deliveryMethod?.address?.name && (
                                        <>
                                          {subscriptionEntities?.deliveryMethod?.address?.name}
                                          <br />
                                        </>
                                      )}
                                      {subscriptionEntities?.deliveryMethod?.address?.address1 && (
                                        <>
                                          {subscriptionEntities?.deliveryMethod?.address?.address1}
                                          <br />
                                        </>
                                      )}
                                      {subscriptionEntities?.deliveryMethod?.address?.address2 && (
                                        <>
                                          {subscriptionEntities?.deliveryMethod?.address?.address2}
                                          <br />
                                        </>
                                      )}
                                      {subscriptionEntities?.deliveryMethod?.address?.city &&
                                        subscriptionEntities?.deliveryMethod?.address?.city}
                                      {subscriptionEntities?.deliveryMethod?.address?.province && (
                                        <>, {subscriptionEntities?.deliveryMethod?.address?.province}</>
                                      )}
                                      {subscriptionEntities?.deliveryMethod?.address?.country && (
                                        <>, {subscriptionEntities?.deliveryMethod?.address?.country} - </>
                                      )}
                                      {subscriptionEntities?.deliveryMethod?.address?.zip &&
                                        subscriptionEntities?.deliveryMethod?.address?.zip}
                                      {subscriptionEntities?.deliveryMethod?.address?.phone && (
                                        <>
                                          <br />
                                          {subscriptionEntities?.deliveryMethod?.address?.phone}
                                        </>
                                      )}
                                    </>
                                  )}
                                </address>
                              )}
                              <AnimatePresence initial={false}>
                                {isUpdateShippingOpenFlag && (
                                  <motion.div
                                    key="content"
                                    initial="collapsed"
                                    animate="open"
                                    exit="collapsed"
                                    variants={{
                                      open: { opacity: 1, height: 'auto' },
                                      collapsed: { opacity: 0, height: 0 }
                                    }}
                                    transition={{ duration: 0.8, ease: [0.04, 0.62, 0.23, 0.98] }}
                                  >
                                    <CustomerPortalUpdateShippingAddressModel
                                      isUpdating={updatingShippingAddress}
                                      modaltitle="Update Shipping Address"
                                      confirmBtnText="Update"
                                      cancelBtnText="Cancel"
                                      initialShippingAddress={subscriptionEntities?.deliveryMethod?.address}
                                      isUpdateShippingOpenFlag={isUpdateShippingOpenFlag}
                                      toggleShippingModal={toggleUpdateShippingModal}
                                      updateShippingAddressMethod={addressModel => updateShippingAddressMethod(addressModel)}
                                      shippingLabelData={shippingLabelData}
                                    />
                                  </motion.div>
                                )}
                              </AnimatePresence>
                            </CardBody>
                          </Collapse>
                        </Card>
                      )}
                      <Card style={{ margin: '20px 0px', borderRadius: '10px', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 8%)' }}>
                        <CardHeader onClick={() => toggleAccordion(1)} aria-expanded={accordion[0]} aria-controls="collapseOne">
                          {/* <i className="header-icon lnr-arrow-up-circle icon-gradient bg-plum-plate"> </i> */}
                          {isPrepaid
                            ? customerPortalSettingEntity?.upcomingFulfillmentText
                            : ' ' + customerPortalSettingEntity?.upcomingOrderAccordionTitle}
                          <div
                            style={{
                              ...forward_arrow_icon,
                              display: 'inline-block',
                              transform: accordion[1] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <KeyboardArrowRight fontSize="20px" color="rgba(18, 21, 78, 0.7)" />
                          </div>
                        </CardHeader>
                        <Collapse isOpen={accordion[1]} data-parent="#accordion" id="collapseOne" aria-labelledby="headingOne">
                          <CardBody>
                            {isPrepaid
                              ? subscriptionEntities?.originOrder?.fulfillmentOrders?.edges
                                  .sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))
                                  .map((fulfillmentData, fid) => {
                                    return (
                                      <CustomerPortalUpcoming
                                        key={fid}
                                        indexKey={fid}
                                        upcomingSwapProductList={upcomingSwapProduct[fid]}
                                        skipShipment={skipShipment}
                                        isPrepaid={isPrepaid}
                                        ordData={fulfillmentData?.node}
                                        shopName={shopName}
                                        checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
                                        subscriptionEntities={subscriptionEntities}
                                        updatingSkipOrder={props.updatingSkipOrder}
                                        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                      />
                                    );
                                  })
                              : subUpcomingOrderEntities &&
                                subUpcomingOrderEntities.length > 0 &&
                                subUpcomingOrderEntities
                                  ?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))
                                  .map((ordData, index) => {
                                    return (
                                      <CustomerPortalUpcoming
                                        key={ordData.id}
                                        indexKey={index}
                                        upcomingSwapProductList={upcomingSwapProduct[index]}
                                        skipShipment={skipShipment}
                                        isPrepaid={isPrepaid}
                                        upcomingOneTimeVariants={upcomingOneTimeVariantList}
                                        ordData={ordData}
                                        shopName={shopName}
                                        checkIfPreventCancellationBeforeDays={checkIfPreventCancellationBeforeDays}
                                        subscriptionEntities={subscriptionEntities}
                                        updatingSkipOrder={props.updatingSkipOrder}
                                        contractId={contractId}
                                        subscriptionContractFreezeStatus={subscriptionContractFreezeStatus}
                                      />
                                    );
                                  })}
                          </CardBody>
                        </Collapse>
                      </Card>
                      <Card style={{ margin: '20px 0px', borderRadius: '10px', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 8%)' }}>
                        <CardHeader
                          className="b-radius-0"
                          onClick={() => toggleAccordion(2)}
                          aria-expanded={accordion[2]}
                          aria-controls="collapseTwo"
                        >
                          {/* <i className="header-icon lnr-cart icon-gradient bg-plum-plate"> </i>{' '} */}
                          {customerPortalSettingEntity?.paymentDetailAccordionTitleV2}
                          <div
                            style={{
                              ...forward_arrow_icon,
                              display: 'inline-block',
                              transform: accordion[2] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <KeyboardArrowRight fontSize="20px" color="rgba(18, 21, 78, 0.7)" />
                          </div>
                        </CardHeader>
                        <Collapse isOpen={accordion[2]} data-parent="#accordion" id="collapseTwo">
                          <CardBody>
                            <Row className="no-gutters">
                              <Col md="8">
                                {!paymentData ? (
                                  '-'
                                ) : paymentData?.__typename === 'CustomerCreditCard' ? (
                                  <>
                                    <p>
                                      {' '}
                                      <b>{customerPortalSettingEntity?.paymentMethodTypeText}:</b>&nbsp;
                                      {paymentData?.__typename === 'CustomerCreditCard'
                                        ? customerPortalSettingEntity?.creditCardTextV2
                                        : paymentData?.__typename}{' '}
                                      - <span style={{ textTransform: 'uppercase' }}>{paymentData?.brand}</span>{' '}
                                      {customerPortalSettingEntity?.endingWithTextV2} {paymentData?.lastDigits}
                                    </p>

                                    <p>
                                      <b>{customerPortalSettingEntity?.cardHolderNameText}:</b>&nbsp;{paymentData?.name}
                                    </p>
                                    <p>
                                      <b>{customerPortalSettingEntity?.cardExpiryTextV2}:</b>{' '}
                                      {moment(paymentData?.expiryMonth, 'MM')?.format('MMMM')} {paymentData?.expiryYear}
                                    </p>
                                  </>
                                ) : paymentData?.__typename === 'CustomerPaypalBillingAgreement' ? (
                                  <div className="btn-icon-vertical btn-square btn-transition" outline="true" color="primary">
                                    {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                    <span style={{ fontSize: '15px' }}>
                                      {' '}
                                      <b>{customerPortalSettingEntity?.paymentMethodTypeText}</b>
                                    </span>
                                    <br></br>
                                    <Payment /> Paypal
                                  </div>
                                ) : paymentData?.__typename === 'CustomerShopPayAgreement' ? (
                                  <div className="btn-icon-vertical btn-square btn-transition" outline="true" color="primary">
                                    {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                    <span style={{ fontSize: '15px' }}>
                                      {' '}
                                      <b>{customerPortalSettingEntity?.paymentMethodTypeText}</b>
                                    </span>
                                    <br></br>
                                    <Payment /> ShopPay
                                  </div>
                                ) : (
                                  'Unknown. Please reach out us.'
                                )}
                              </Col>
                              <Col md={4}>
                                {paymentData?.__typename != 'CustomerShopPayAgreement' && (
                                  <MySaveButton
                                    onClick={() => editPaymentInfo()}
                                    text={updatePaymentButtonText}
                                    updating={updatingPayment}
                                    updatingText={updatePaymentButtonText}
                                    className="appstle_font_size appstle_updatePaymentButton "
                                  >
                                    {updatePaymentButtonText}
                                  </MySaveButton>
                                )}
                                <br />
                                <br />
                                <p>
                                  <strong>
                                    {paymentData?.__typename === 'CustomerShopPayAgreement' ? (
                                      customerPortalSettingEntity?.shopPayPaymentUpdateTextV2 ? (
                                        <>
                                          <div
                                            dangerouslySetInnerHTML={{ __html: customerPortalSettingEntity?.shopPayPaymentUpdateTextV2 }}
                                          />
                                        </>
                                      ) : (
                                        <>
                                          {' '}
                                          You are using Shop Pay, please use this article for updating the payment methods.
                                          <a
                                            href="https://help.shop.app/hc/en-us/articles/4412203886996-How-do-I-manage-my-subscription-orders-with-Shop-Pay-"
                                            target="_blank"
                                          >
                                            click here
                                          </a>
                                        </>
                                      )
                                    ) : (
                                      customerPortalSettingEntity?.paymentNotificationText
                                    )}
                                  </strong>
                                </p>
                              </Col>
                            </Row>
                            <div className="d-block text-center">
                              {updatePaymentSuccess && (
                                <p>
                                  <br></br>
                                  <Alert color="success"> {customerPortalSettingEntity?.updatePaymentMessageV2}</Alert>
                                </p>
                              )}
                            </div>
                          </CardBody>
                        </Collapse>
                      </Card>
                    </Fragment>
                  )}

                  {subscriptionEntities.status !== 'CANCELLED' && customerPortalSettingEntity?.cancelSub && (
                    <Card
                      style={{ margin: '20px 0px', borderRadius: '10px', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 8%)' }}
                      id="cancelSubscription-popover"
                    >
                      <CardHeader onClick={() => toggleAccordion(3)} aria-expanded={accordion[3]} aria-controls="collapseThree">
                        {/* <i className="header-icon lnr-trash icon-gradient bg-plum-plate"> </i>{' '} */}
                        {customerPortalSettingEntity?.cancelAccordionTitle}
                        <div
                          style={{
                            ...forward_arrow_icon,
                            display: 'inline-block',
                            transform: accordion[3] ? 'rotate(90deg)' : ''
                          }}
                        >
                          <KeyboardArrowRight fontSize="20px" color="rgba(18, 21, 78, 0.7)" />
                        </div>
                      </CardHeader>
                      <Collapse isOpen={accordion[3]} data-parent="#accordion" id="collapseThree">
                        <CardBody>
                          <div
                            className={`d-block ${
                              cancellationManagementEntity.cancellationType === 'CANCEL_IMMEDIATELY' ? 'text-center' : ''
                            }`}
                          >
                            {(cancellationManagementEntity.cancellationType === 'CANCEL_IMMEDIATELY' ||
                              cancellationManagementEntity.cancellationType === 'CUSTOMER_RETENTION_FLOW') && (
                              <>
                                {cancellationManagementEntity.cancellationType === 'CUSTOMER_RETENTION_FLOW' && (
                                  <div className="cancellationReasonSelectWrapper">
                                    <label>
                                      {customerPortalSettingEntity?.selectCancellationReasonLabelText || `Select Cancellation Reason*`}
                                    </label>
                                    <Input
                                      type="select"
                                      name="cancellationReason"
                                      invalid={!cancellationfeedbackValid}
                                      required={true}
                                      onChange={event => setCancellationfeedback(event.target.value)}
                                    >
                                      <option value="">select reason</option>
                                      {JSON.parse(cancellationManagementEntity?.cancellationReasonsJSON).map(option => {
                                        return <option value={option?.cancellationReason}>{option?.cancellationReason}</option>;
                                      })}
                                    </Input>
                                    {!cancellationfeedbackValid && (
                                      <FormFeedback>
                                        {customerPortalSettingEntity?.selectCancellationReasonRequiredMsg ||
                                          'please select cancellation reason'}
                                      </FormFeedback>
                                    )}
                                  </div>
                                )}
                                <MySaveButton
                                  aria-describedby={id}
                                  onClick={handleClick}
                                  type="button"
                                  text={customerPortalSettingEntity?.cancelSubscriptionBtnText}
                                  addBuffer={!removeBuffer}
                                  disabled={
                                    subscriptionContractFreezeStatus ||
                                    checkIfPreventCancellationBeforeDays(
                                      customerPortalSettingEntity?.preventCancellationBeforeDays,
                                      subscriptionEntities?.nextBillingDate
                                    )
                                  }
                                  lockedIcon={
                                    subscriptionContractFreezeStatus ||
                                    checkIfPreventCancellationBeforeDays(
                                      customerPortalSettingEntity?.preventCancellationBeforeDays,
                                      subscriptionEntities?.nextBillingDate
                                    )
                                  }
                                  updating={updating}
                                  updatingText={'Processing'}
                                  className="appstle_cencelSubscription appstle_font_size"
                                />
                                {(subscriptionContractFreezeStatus ||
                                  checkIfPreventCancellationBeforeDays(
                                    customerPortalSettingEntity?.preventCancellationBeforeDays,
                                    subscriptionEntities?.nextBillingDate
                                  )) && (
                                  <Alert color="info mt-2">
                                    {subscriptionContractFreezeStatus
                                      ? subscriptionContractFreezeStatusMessage ||
                                        customerPortalSettingEntity?.subscriptionContractFreezeMessageV2 ||
                                        'Your subscription contract is freezed by your shop owner.'
                                      : checkIfPreventCancellationBeforeDays(
                                          customerPortalSettingEntity?.preventCancellationBeforeDays,
                                          subscriptionEntities?.nextBillingDate
                                        ) &&
                                        (replaceStringWithSpecificWord(
                                          customerPortalSettingEntity?.preventCancellationBeforeDaysMessage,
                                          '{{preventDays}}',
                                          customerPortalSettingEntity?.preventCancellationBeforeDays
                                        ) ||
                                          `You can not pause/cancel the subscription before the ${customerPortalSettingEntity?.preventCancellationBeforeDays} days from your next order date.`)}
                                  </Alert>
                                )}

                                <Popover
                                  id={id}
                                  open={open}
                                  anchorEl={anchorEl}
                                  onClose={handleClose}
                                  anchorOrigin={{
                                    vertical: 'top',
                                    horizontal: 'center'
                                  }}
                                  transformOrigin={{
                                    vertical: 'top',
                                    horizontal: 'center'
                                  }}
                                >
                                  <div>
                                    <h6
                                      style={{
                                        margin: '0',
                                        fontSize: '16px',
                                        padding: '16px',
                                        backgroundColor: '#ddd',
                                        fontFamily: 'Times New Roman'
                                      }}
                                    >
                                      {isPrepaid
                                        ? customerPortalSettingEntity?.cancelSubscriptionConfirmPrepaidText
                                        : customerPortalSettingEntity?.cancelSubscriptionConfirmPayAsYouGoText}
                                    </h6>
                                    <div
                                      style={{
                                        padding: '16px',
                                        display: 'flex',
                                        justifyContent: 'flex-end',
                                        fontFamily: 'Times New Roman'
                                      }}
                                    >
                                      <button
                                        onClick={() => {
                                          handleClose();
                                          cancelSubscription();
                                        }}
                                        style={{
                                          backgroundColor: '#d92550',
                                          borderColor: '#d92550',
                                          color: '#fff',
                                          padding: '6px 12px',
                                          border: 'none',
                                          fontSize: '13px',
                                          borderRadius: '4px',
                                          fontFamily: 'Times New Roman'
                                        }}
                                      >
                                        {customerPortalSettingEntity?.yesBtnTextV2 || 'Yes'}
                                      </button>

                                      <button
                                        onClick={() => {
                                          setCancelSubToggle(false);
                                          handleClose();
                                        }}
                                        style={{
                                          backgroundColor: '#3ac47d',
                                          borderColor: '#3ac47d',
                                          color: '#fff',
                                          padding: '6px 12px',
                                          marginLeft: '10px',
                                          border: 'none',
                                          fontSize: '13px',
                                          borderRadius: '4px',
                                          fontFamily: 'Times New Roman'
                                        }}
                                      >
                                        {customerPortalSettingEntity?.noBtnTextV2 || 'No'}
                                      </button>
                                    </div>
                                  </div>
                                </Popover>
                              </>
                            )}
                            {cancellationManagementEntity.cancellationType === 'CANCELLATION_INSTRUCTIONS' && (
                              <div dangerouslySetInnerHTML={{ __html: cancellationManagementEntity?.cancellationInstructionsText }}></div>
                            )}
                            {errorMessages?.removeSubscriptionBtnMessage && (
                              <MessageBox
                                message={errorMessages?.removeSubscriptionBtnMessage}
                                color={errorMessages?.removeSubscriptionBtnMessageColor}
                              />
                            )}
                          </div>
                        </CardBody>
                      </Collapse>
                    </Card>
                  )}
                </div>
              </>
            )
          )}
        </div>
      )}
    </Fragment>
  );
};

const mapStateToProps = state => ({
  account: state.authentication.account,
  subscriptionEntities: state.subscription.entity,
  updating: state.subscription.updating,
  loading: state.subscription.loading,
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
  subUpcomingOrderEntities: state.subscriptionBillingAttempt.entity,
  updatingSkipOrder: state.subscriptionBillingAttempt.updating,
  skippedBillingAttempt: state.subscriptionBillingAttempt.skippedBillingAttempt
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
  getSubscriptionFreezeStatus
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionDetails);

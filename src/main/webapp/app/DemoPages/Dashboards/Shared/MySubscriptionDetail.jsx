import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import {addDays} from 'date-fns';
import axios from 'axios';
import moment from 'moment';
import swal from 'sweetalert';
import {Link} from 'react-router-dom';
import Loader from 'react-loaders';
import {
  Alert,
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  Collapse,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  ListGroupItem,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row
} from 'reactstrap';
import Switch from 'react-switch';
import 'react-datepicker/dist/react-datepicker.css';
import {Field, Form} from 'react-final-form';
import { ChevronForward } from 'react-ionicons';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
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
import AddProductModal from './AddProductModal';
import {
  getSubscriptionContractDetailsByContractId,
  getUpcomingOrderEntityList,
  reset,
  splitSubscriptionContract,
  updateCustomerInfo,
  updateEntity,
  updateSubscriptionsAttributes
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import UpdateShippingAddressModel from './UpdateShippingAddressModel';
import UpcomingOrder from './UpcomingOrder';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import {Assessment, CallSplit, PauseCircleFilled, Payment, PlayCircleFilled} from '@mui/icons-material';
import SubAnalyticsViewModel from '../Subscriptions/SubAnalyticsViewModel';
import PastOrder from 'app/DemoPages/Dashboards/Shared/PastOrder';
import UpdateOrderNoteModal from './UpdateOrderNoteModal';
import {getEntity as getPortalSettings} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import SubscriptionActivityList from 'app/DemoPages/Dashboards/ActivityLog/SubscriptionActivityList';
import {getSellingPlans} from 'app/entities/subscription-group/subscription-group.reducer';
import {getEntities as getEmailSetting} from 'app/entities/email-template-setting/email-template-setting.reducer';
import { convertToShopTimeZoneDate} from './SuportedShopifyTImeZone';
import AddDiscountCode from './AddDiscountCode';
import ErrorBoundary from 'app/shared/error/error-boundary';
import DatePickerWithTimezone from 'app/DemoPages/Dashboards/Shared/DatePickerWithTimezone';
import SweetAlert from 'sweetalert-react';
import EditOneOffContractDetail from './EditOneOffContractDetail';
import {JhiItemCount} from 'react-jhipster';
import Pagination from 'react-js-pagination';
import './MySubscriptionDetail.scss';
import SubscriptionLineItem from './SubscriptionLineItem';
import {useHistory, useLocation} from 'react-router';
import { BsArrowLeftShort, BsArrowRightShort } from 'react-icons/bs';
import { IoMdArrowBack } from 'react-icons/io';
import UpdateCustomerInfoModel from 'app/DemoPages/Dashboards/Shared/UpdateCustomerInfoModel';
import UpdateSubscriptionAttributesModal from 'app/DemoPages/Dashboards/Shared/UpdateSubscriptionAttributesModal';
import SubscriptionSingleProductLineItem from "app/DemoPages/Dashboards/Shared/SubscriptionSingleProductLineItem";
import { isOneTimeProduct, isFreeProduct } from 'app/shared/util/subscription-utils';
import { replaceVariablesValue } from '../../../shared/util/customer-utils';

var momentTZ = require('moment-timezone');

const MySubscriptionDetail = ({
                                subUpcomingOrderEntities,
                                updateShippingAddress,
                                updateCustomerInfo,
                                updatingShippingAddress,
                                updatingCustomerInfo,
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
                                getEmailSetting,
                                splitContractUpdating,
                                splitSubscriptionContract,
                                splitContractUpdated,
                                splitContractDetails,
                                contracts,
                                reset,
                                updateSuccessSubscriptionContractDetails,
                                subscriptionListEntities,
                                updateSubscriptionsAttributes,
                                updatingAttribute,
                                getSubscriptionEntity,
                                ...props
                              }) => {
  const history = useHistory();
  const [accordion, setAccordion] = useState([false, false, false, false, false, false]);
  const [paymentData, setPaymentData] = useState(subscriptionEntities?.customerPaymentMethod?.instrument);
  const [editBilling, setEditBilling] = useState(false);
  const [editDelivery, setEditDelivery] = useState(false);
  const [updatePaymentSuccess, setUpdatePaymentSuccess] = useState(false);
  const [updatingPayment, setUpdatingPayment] = useState(false);
  const [updatedNextOrderDate, setUpdatedNextOrderDate] = useState();
  const [isEditNextOrder, setIsEditNextOrder] = useState(false);
  const [isAddingProducts, setIsAddingProduct] = useState(false);
  const [isUpdateShippingOpenFlag, setIsUpdateShippingOpenFlag] = useState(false);
  const [isUpdateCustomerInfoOpenFlag, setIsUpdateCustomerInfoOpenFlag] = useState(false);
  const [isUpdateOrderNoteOpenFlag, setIsUpdateOrderNoteOpenFlag] = useState(false);
  const [isUpdateAttributeOpenFlag, setIsUpdateAttributeOpenFlag] = useState(false);
  const [updatingShipping, setUpdatingShipping] = useState(false);
  const [isPrepaid, setIsPrepaid] = useState(
    subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true
  );
  const [updatedDeliveryPrice, setUpdatedDeliveryPrice] = useState('');
  const [updatedMinCycle, setUpdatedMinCycle] = useState('');
  const [updatedMaxCycle, setUpdatedMaxCycle] = useState('');
  const [isEditDeliveryPrice, setIsEditDeliveryPrice] = useState(false);
  const [subAnalyticsData, setSubAnalyticsData] = useState({});
  const [subAnalysisToggleModel, setSubAnalysisToggleModel] = useState(false);
  const [isAnalyticsProcess, setIsAnalyticsProcess] = useState(false);
  const [updatingContractStatus, setUpdatingContractStatus] = useState(false);
  const [variantQuantityList, setVariantQuantityList] = useState([]);
  const [upcomingSwapProduct, setUpcomingSwapProduct] = useState([]);
  const [isAttemptBillingDisplayedOnce, setIsAttemptBillingDisplayedOnce] = useState(-1);
  const [isEditMinCycles, setIsEditMinCycles] = useState(false);
  const [isEditMaxCycles, setIsEditMaxCycles] = useState(false);
  const [discountCopoun, setDiscountCopoun] = useState('');
  const [showAddDiscountModal, setShowAddDiscountModal] = useState(false);
  const [showModalFlag, setShowModalFlag] = useState(false);
  const [isAllowDeliveryPriceOverride, setIsAllowDeliveryPriceOverride] = useState(false);
  const [globalDiscounts, setGlobalDiscounts] = useState([]);
  const [nextOrderDateChanged, setNextOrderDateChanged] = useState(false);
  const [updatedMinOrderCycle, setUpdatedMinOrderCycle] = useState(false);
  const [updatedMaxOrderCycle, setUpdatedMaxOrderCycle] = useState(false);
  const [invokePopup, setInvokePopup] = useState(false);
  const [skippedBillingAttempted, setSkippedBillingAttempted] = useState(false);
  const [updatedBillingInterval, setUpdatedBillingInterval] = useState(false);
  const [isEditDeliveryMethod, setIsEditDeliveryMethod] = useState(false);
  const [deliveryMethodValue, setDeliveryMethodValue] = useState(subscriptionEntities?.deliveryMethod?.shippingOption?.title);
  const [upcomingOneTimeVariantList, setUpcomingOneTimeVariantList] = useState([]);
  const [activeOneTimeVariantList, setActiveOneTimeVariantList] = useState([]);
  const [showOneTimeSuccessAlert, setShowOneTimeSuccessAlert] = useState(false);

  const [pastOrderActivePage, setPastOrderActivePage] = useState(1);
  const [pastOrderTotalRowData, setPastOrderTotalRowData] = useState(0);
  const [pastOrderItemsPerPage, setPastOrderItemsPerPage] = useState(10);

  const [fulfillmentOrderEntity, setFulfillmentOrderEntity] = useState([]);
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [pastOrderIds, setPastOrderIds] = useState([]);

  const [currentCycle, setCurrentCycle] = useState(0);
  const [billingIsAttempted, setBillingIsAttempted] = useState(false);
  const [fulfillmentLoading, setFulfillmentLoading] = useState(false);
  const [currentCycleLoaded, setCurrentCycleLoaded] = useState(false);
  const [openSplitContractPopup, setOpenSplitContractPopup] = useState(false);
  const [continuePausedOrActiveProcess, setContinuePausedOrActiveProcess] = useState(false);
  const [cancelSubscriptionProcess, setCancelSubscriptionProcess] = useState(false);
  const [splitContractProducts, setSplitContractProducts] = useState([]);
  const [isEditQuantity, setIsEditQuantity] = useState(false);
  const [isEditQuantityInProgress, setIsEditQuantityInProgress] = useState(false);
  const [isQuantityInputInValid, setIsQuantityInputInValid] = useState(false);
  const [quantityInputBlurred, setQuantityInputBlurred] = useState(false);
  const [quantity, setQuantity] = useState('');
  const [quantityChangeProductID, setQuantityChangeProductID] = useState();
  const [billingAttemptId, setBillingAttemptId] = useState();
  const location = useLocation();
  const [splitWithOrder, setSplitWithOrder] = useState(false);

  const [billingFrequencies, setBillingFrequencues] = useState([]);
  const [selectedContractSellingPlan, setSelectedContractSellingPlan] = useState(null)
  const [deliveryFrequencies, setDeliveryFrequencies] = useState([])
  const [sellingPlanUpdateInProgress, setSellingPlanUpdateInProgress] = useState(false)

  const [checkAnchorDayNextOrderDate, setCheckAnchorDayNextOrderDate] = useState(false)

  const [oneTimeProductsInSubscription, setOneTimeProductsInSubscription] = useState([]);
  const [recurringProductsInSubscription, setRecurringProductsInSubscription] = useState([]);
  const [freeProductsInSubscription, setFreeProductsInSubscription] = useState([]);
  const [totalUpcomingAmount, setTotalUpcomingAmount] = useState(0);



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

  useEffect(() => {
    setIsAllowDeliveryPriceOverride(subscriptionContractDetailsEntity?.allowDeliveryPriceOverride);
  }, [subscriptionContractDetailsEntity]);

  useEffect(() => {
    getSellingPlans();
    getEmailSetting();
  }, []);

  useEffect(() => {
    const upcomingOrderEntity = async () => {
      if (contractId && customerId && shopName) {
        await getUpcomingOrderEntity(contractId, customerId, shopName);
        setInvokePopup(true);
      }
    };

    upcomingOrderEntity();
  }, [subscriptionEntities]);

  useEffect(() => {
    if (contractId && customerId && shopName)
      getFailedOrderEntity(contractId, customerId, shopName, pastOrderActivePage - 1, pastOrderItemsPerPage);
  }, [subscriptionEntities, pastOrderActivePage]);

  useEffect(() => {
    if (contractId) {
      getSubscriptionContractDetailsByContractId(contractId);
    }
  }, [subscriptionEntities, updateSuccessSubscriptionContractDetails]);

  useEffect(() => {
    if (updateSuccessSubscriptionContractDetails) {
      setIsUpdateCustomerInfoOpenFlag(false);
    }
  }, [updateSuccessSubscriptionContractDetails]);

  useEffect(() => {
    setPastOrderTotalRowData(totalFailedBillingAttemptItems);
  }, [totalFailedBillingAttemptItems]);

  useEffect(() => {
    let upcomingOneTimeVariants = [];
    if (contractId) {
      let subscriptionContractOneoff = `api/subscription-contract-one-offs-by-contractId?contractId=${contractId}`;
      axios
        .get(subscriptionContractOneoff)
        .then(async resp => {
          if (resp?.data?.length > 0) {
            // setUpcomingOneTimeVariantList(resp?.data)
            let globalIndex = 0;
            for await (let item of resp?.data) {
              try {
                globalIndex = globalIndex + 1;
                const productvariantUrl = `/api/data/variant?variantId=${item?.variantId}`;

                await axios
                  .get(productvariantUrl)
                  .then(async res => {
                    if (res?.data) {
                      const productDataUrl = `/api/data/product?productId=${res?.data?.product_id}`;
                      let productData = {};
                      await axios
                        .get(productDataUrl)
                        .then(res => {
                          productData = res?.data;
                        })
                        .catch(err => console.log(err));

                      let varData = res.data;
                      let upcomingOneTimeVariantsData = {
                        billingAttemptId: item?.billingAttemptId,
                        prdImage: `${productData?.image?.src}`,
                        prdId: varData?.product_id,
                        title: productData?.title,
                        variantTitle: varData?.title,
                        id: varData?.id,
                        price: varData?.price,
                        quantity: item?.quantity || 1
                      };
                      upcomingOneTimeVariants.push(upcomingOneTimeVariantsData);
                      if (globalIndex === resp?.data?.length) {
                        setUpcomingOneTimeVariantList([...upcomingOneTimeVariants]);
                      }
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
    }
  }, [subscriptionEntities]);

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


  useEffect(() => {
    if (!isPrepaid && subUpcomingOrderEntities?.length > 0 && upcomingOneTimeVariantList?.length > 0) {
      let activeOneTimeVariants = [];
      let upcomingOrderID = subUpcomingOrderEntities[0]?.id?.toString();
      let activeOneTimeVariantObj = upcomingOneTimeVariantList?.filter(function (item) {
        return item?.billingAttemptId == upcomingOrderID;
      });
      activeOneTimeVariants = [...activeOneTimeVariants, ...activeOneTimeVariantObj];
      setActiveOneTimeVariantList(activeOneTimeVariants);
    } else {
      setActiveOneTimeVariantList([]);
    }
  }, [subUpcomingOrderEntities, upcomingOneTimeVariantList]);

  useEffect(() => {
    getPortalSettings(contractId, shopName);
  }, [contractId]);

  useEffect(() => {
    setPaymentData(subscriptionEntities?.customerPaymentMethod?.instrument);
    let checkPrepaid =
      subscriptionEntities?.billingPolicy?.intervalCount == subscriptionEntities?.deliveryPolicy?.intervalCount ? false : true;
    setIsPrepaid(checkPrepaid);
    setDeliveryMethodValue(subscriptionEntities?.deliveryMethod?.shippingOption?.title);
    if (contractId && checkPrepaid) {
      getUpcomingOrderEntityList(contractId);
    }
    if (contractId) {
      getCurrentCycle(contractId);
    }
  }, [subscriptionEntities]);

  useEffect(() => {
    setEditBilling(false);
    setIsEditNextOrder(false);
    setIsEditDeliveryPrice(false);
    setIsEditMaxCycles(false);
    setIsEditMinCycles(false);
    setEditDelivery(false);
  }, [props.updateSuccess]);

  useEffect(() => {
    let variantArray = [];
    subscriptionEntities?.lines?.edges?.map((prd) => {
      if (!isOneTimeProduct(prd) && !isFreeProduct(prd)) {
        variantArray = [
          ...variantArray,
          {variantId: prd.node.variantId !== null ? prd.node.variantId.split('/').pop() : null, quantity: prd.node.quantity}
        ];
      }
    });
    setVariantQuantityList([...variantArray]);
    extractGlobalDiscounts();
  }, [subscriptionEntities]);

  useEffect(() => {
    if (subscriptionEntities?.lines?.edges.length > 0 && variantQuantityList.length > 0) {
      var data = {
        variantQuantityList: variantQuantityList
      };

      axios.post(`/api/product-swaps-by-variant-groups/${contractId}`, data).then(res => {
        setUpcomingSwapProduct(res.data);
      });
    }
  }, [variantQuantityList]);

  useEffect(() => {
    if (
      (nextOrderDateChanged || updatedBillingInterval || updatedMaxOrderCycle || updatedMinOrderCycle || skippedBillingAttempted) &&
      subUpcomingOrderEntities.length > 0 &&
      invokePopup
    ) {
      var currentUpcomingOrderDate = moment(subscriptionEntities?.nextBillingDate).format('MMMM DD, YYYY');
      var updatedUpcomingOrderDate;
      // if(isPrepaid)
      // {
      //   updatedUpcomingOrderDate = moment(upcomingOrderDetailsEntity?.fulfillmentOrders?.edges?.sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))[0]?.node?.fulfillAt)?.format("MMMM DD, YYYY");
      //   console.log(upcomingOrderDetailsEntity?.fulfillmentOrders?.edges?.sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt)))
      // }
      // else {
      if (subUpcomingOrderEntities?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))[0].status === 'QUEUED') {
        updatedUpcomingOrderDate = moment(
          subUpcomingOrderEntities?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))[0]?.billingDate
        ).format('MMMM DD, YYYY');
      }
      // }
      popUpWhenUpcomingOrderChange(currentUpcomingOrderDate, updatedUpcomingOrderDate);
    } else if (invokePopup) {
      setInvokePopup(false);
    }
  }, [nextOrderDateChanged, updatedBillingInterval, skippedBillingAttempted, invokePopup]);

  useEffect(() => {
    if (splitContractUpdated && splitContractDetails && splitContractDetails.id) {
      history.push(`/dashboards/subscription/${splitContractDetails.id.split('/').pop()}/detail`);
      reset();
    }
  }, [splitContractUpdated]);

  useEffect(() => {
    let productIds = subscriptionEntities?.lines?.edges?.map((prd, index) => {
      return prd?.node?.productId?.split('/')?.pop();
    })
    if (productIds && productIds?.length) {
      let tempProductIds = productIds.map(prd => `productIds=${prd}`)
      axios.get(`/api/data/products-selling-plans?${tempProductIds.join("&")}`)
        .then(data => {
          let billingFrequencies = [];
          let deliveryFrequencies = [];
          data?.data?.forEach(opt => {
            opt?.sellingPlanGroups?.edges.forEach(edge => {
              edge?.node?.sellingPlans.edges.forEach(sellingPlanEdge => {
                let biilingFreq = {};
                let deliveryFreq = {};
                biilingFreq["name"] = `${sellingPlanEdge?.node?.name} (${sellingPlanEdge?.node?.billingPolicy?.intervalCount} ${sellingPlanEdge?.node?.billingPolicy?.interval})`;
                biilingFreq["value"] = sellingPlanEdge?.node?.id?.split('/').pop();
                biilingFreq["intervalCount"] = sellingPlanEdge?.node?.billingPolicy?.intervalCount;
                biilingFreq["interval"] = sellingPlanEdge?.node?.billingPolicy?.interval;
                deliveryFreq["name"] = `${sellingPlanEdge?.node?.name} (${sellingPlanEdge?.node?.deliveryPolicy?.intervalCount} ${sellingPlanEdge?.node?.deliveryPolicy?.interval})`;
                deliveryFreq["value"] = sellingPlanEdge?.node?.id?.split('/').pop();
                deliveryFreq["intervalCount"] = sellingPlanEdge?.node?.deliveryPolicy?.intervalCount;
                deliveryFreq["interval"] = sellingPlanEdge?.node?.deliveryPolicy?.interval;
                if (!billingFrequencies?.find(element => element["value"] === biilingFreq["value"])) {
                  billingFrequencies.push(biilingFreq)
                }
                if (!deliveryFrequencies?.find(element => element["value"] === deliveryFreq["value"])) {
                  deliveryFrequencies.push(deliveryFreq)
                }
              })
            })
          })
          setDeliveryFrequencies(deliveryFrequencies);
          setBillingFrequencues(billingFrequencies);
        })
    }
  }, [subscriptionEntities])

  const selectOrderIdChangeIdChangeHandler = event => {
    setSelectedOrderId(event.target.value);
  };

  const popUpWhenUpcomingOrderChange = (currentUpcomingOrderDate, updatedUpcomingOrderDate) => {
    if (currentUpcomingOrderDate && updatedUpcomingOrderDate) {
      if (currentUpcomingOrderDate != updatedUpcomingOrderDate) {
        console.log(moment(updatedNextOrderDate).format('MMMM DD, YYYY'));
        swal({
          title: 'Try Again!',
          text: 'Something went wrong. Your next billing date is not in sync with first upcoming order date.',
          icon: 'error',
          button: 'close'
        });
      } else if (currentUpcomingOrderDate === updatedUpcomingOrderDate) {
        swal({
          title: 'Successfully Updated!',
          text: 'Your subscription was updated successfully. Please still confirm your update by reviewing upcoming orders section.',
          icon: 'success',
          button: 'okay'
        });
      }
      setNextOrderDateChanged(false);
      setSkippedBillingAttempted(false);
      setUpdatedMaxOrderCycle(false);
      setUpdatedBillingInterval(false);
      setUpdatedMinOrderCycle(false);
      setInvokePopup(false);
    }
  };

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const toggle = () => {
    setShowModalFlag(!showModalFlag);
  };

  const getCurrentCycle = contractId => {
    axios.get(`api/subscription-contract-details/current-cycle/${contractId}`).then(res => {
      setCurrentCycleLoaded(true);
      setCurrentCycle(res.data);
    });
  };

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

  const addLineItem = (items, purchaseOption, quantity) => {
    const selectedItems = items;
    let upcomingOrderID = subUpcomingOrderEntities[0]?.id?.toString();
    setIsAddingProduct(true);
    const addline = async () => {
      if (selectedItems.length > 0) {
        const item = selectedItems.shift();
        axios
          .get(`/api/data/product?productId=${item?.productId}`)
          .then(res => {
            const firstVariant = res?.data?.variants.filter(variant => variant?.id === item?.id).pop();
            let addProductURL = `/api/v2/subscription-contracts-add-line-item?contractId=${contractId}&price=${firstVariant?.price}&quantity=${quantity}&variantId=${firstVariant?.admin_graphql_api_id}`;
            if (purchaseOption === 'ONETIME') {
              addProductURL = addProductURL + "&isOneTimeProduct=true";
            }
            return axios.put(addProductURL);
          })
          .catch(err => {
            console.log(err);
            toast.error(err.response.data.title, options);
            setIsAddingProduct(false);
          })
          .then(res => {
            addline();
          })
          .catch(err => {
            console.log(err);
            toast.error('Error in add product', options);
            setIsAddingProduct(false);
          });
      } else {
        await getEntity(contractId);
        setIsAddingProduct(false);
        if (purchaseOption === 'ONETIME') {
          setShowOneTimeSuccessAlert(true);
        } else {
          toast.success('Products added to Contract', options);
        }
      }
    };
    addline();
  };
  useEffect(() => {
    if (billingIsAttempted) {
      toggleAccordion(4);
    }
  }, [billingIsAttempted]);
  const toggleAccordion = tab => {
    const state = accordion.map((x, index) => (tab === index ? !x : false));
    setAccordion(state);
  };
  const saveEntity = async values => {
    if (values) {
      swal({
        title: 'Are you sure?',
        text: 'If you change the frequency, it may change your next order date',
        icon: 'warning',
        buttons: true,
        dangerMode: true
      }).then(async value => {
        if (value) {
          setSellingPlanUpdateInProgress(true);
          let interval = values.frequencyInterval;
          let intervalCount = values.frequencyCount;

          if (values?.sellingPlanId) {
            let selectedPlanDetails = ((!isPrepaid ? billingFrequencies : deliveryFrequencies).filter(freq => {
              return freq?.value == values?.sellingPlanId
            })).pop();
            interval = selectedPlanDetails.interval;
            intervalCount = selectedPlanDetails.intervalCount;
          }
          axios.get(`api/subscription-contracts-is-overwrite-anchor-day?contractId=${contractId}&interval=${interval}&intervalCount=${intervalCount}`)
          .then(res => {
            if (res.data) {
              return swal({
                title: "Warning",
                text: "This action will override anchor day",
                icon: "warning",
                buttons: [("Cancel"), ("Confirm")],
                dangerMode: true
              }).then(selectedOption => {
                if(selectedOption) {
                  return updateBilling(values);
                }
              })
            } else {
              return updateBilling(values);
            }
          })
        }
      });
    }
  };

  const updateBilling = async (values) => {
    if (values?.sellingPlanId) {
      let requestUrl = `api/subscription-contracts-update-frequency-by-selling-plan?contractId=${contractId}&sellingPlanId=${values?.sellingPlanId}`;
      axios.put(requestUrl)
      .then(async res => {
        await getSubscriptionEntity(contractId);
        setUpdatedBillingInterval(true);
        setSellingPlanUpdateInProgress(false);
        setEditBilling(false);
      })
      .catch(err => {
        setSellingPlanUpdateInProgress(false);
      });
    } else {
      await updateBillingInterval(contractId, values.frequencyInterval, values.frequencyCount, shopName);
      setUpdatedBillingInterval(true);
    }
  }

  const saveDeliveryEntity = values => {
    if (values) {
      updateDeliveryInterval(contractId, values.deliveryFrequencyInterval, values.deliveryFrequencyCount, shopName);
    }
  };

  const editPaymentInfo = () => {
    setUpdatingPayment(true);
    //     axios.put(`api/subscription-contracts-update-payment-method?contractId=${contractId}&shop=${shopName}`).then(res => {
    axios.put(`api/subscription-contracts-update-payment-method?contractId=${contractId}`).then(res => {
      if (res.status == 200) {
        setUpdatePaymentSuccess(true);
        toast.success('Request has been sent to your mail.', options);
      }
      setUpdatingPayment(false);
    });
  };

  const updateNextOrderDateMethod = async () => {
    if (updatedNextOrderDate > new Date()) {
      checkAnchorDay(updatedNextOrderDate)
    }
  };

  const checkAnchorDay = (updatedNextOrderDate) => {
   let updatedDate = getTimezoneWiseDate(updatedNextOrderDate);
    if (subscriptionEntities?.billingPolicy?.anchors?.length) {
      return swal({
        title: "Warning",
        text: "This action will override anchor day",
        icon: "warning",
        buttons: [("Cancel"), ("Confirm")],
        dangerMode: true
      }).then(selectedOption => {
        if(selectedOption) {
          // setCheckAnchorDayNextOrderDate(false);
          updateBillingDate(contractId, updatedDate.toISOString(), shopName);
          setNextOrderDateChanged(true);
        }
      })
    } else {
      // setCheckAnchorDayNextOrderDate(false);
      updateBillingDate(contractId, updatedDate.toISOString(), shopName);
      setNextOrderDateChanged(true);
    }
  }

  const getTimezoneWiseDate = (updatedNextOrderDate) => {
    try {
      let oldDate = new Date(moment.tz(subscriptionEntities.nextBillingDate, shopInfo?.ianaTimeZone).format("YYYY-MM-DDTHH:mm:ss.SSS"));
      let oldLocalDate = new Date(subscriptionEntities.nextBillingDate);
      return new Date(oldLocalDate.getTime() + (updatedNextOrderDate - oldDate)); 
    } catch (error) {
      return updatedNextOrderDate;
    }
  };

  const updateDeliveryMethod = async () => {
    swal({
      title: 'Are you sure?',
      text:
        "Please note that this would only update your delivery method name that is used for placing subscription orders. It should be used only if you have fixed rate shipping. This approach won't work for shipping that is tied to carriers and should be avoided for any future issues.",
      icon: 'warning',
      buttons: true,
      dangerMode: true
    }).then(async value => {
      if (value) {
        await updateDeliveryMethodName(contractId, deliveryMethodValue.toString());
        setIsEditDeliveryMethod(false);
      }
    });
  };

  const updateAttemptBilling = id => {
    return updateAttemptBillingEntity(id, shopName, contractId);
  };

  const updateMinCyclesMethod = async () => {
    await updateMinCycles(contractId, updatedMinCycle, shopName);
    setUpdatedMinOrderCycle(true);
  };

  const updateMaxCyclesMethod = async () => {
    await updateMaxCycles(contractId, updatedMaxCycle, shopName);
    setUpdatedMaxOrderCycle(true);
  };

  const updateDeliveryPriceMethod = () => {
    updateDeliveryPrice(contractId, parseFloat(updatedDeliveryPrice), shopName);
  };

  const skipShipment = async (ordDataId, isFullfilment) => {
    await skipBillingOrder(isFullfilment ? ordDataId.split('/').pop() : ordDataId, shopName, contractId, customerId, isFullfilment);
    setSkippedBillingAttempted(true);
  };

  // Shipping Address Update Start Here

  const updateShippingAddressMethod = async updateShipping => {
    await updateShippingAddress(updateShipping, contractId, shopName);
    toggleUpdateShippingModal();
  };

  const updateCustomerInformation = async updateCustomer => {
    await updateCustomerInfo(updateCustomer);
    toggleUpdateCustomerInfoModal();
  };

  const toggleUpdateShippingModal = () => setIsUpdateShippingOpenFlag(!isUpdateShippingOpenFlag);
  const toggleUpdateCustomerInfoModal = () => setIsUpdateCustomerInfoOpenFlag(!isUpdateCustomerInfoOpenFlag);

  // Shipping Address Update Logic End Here

  const updateOrderNoteMethod = async updateShippingContractDetail => {
    await updateEntity(updateShippingContractDetail);
    toggleUpdateOrderNoteModal();
  };

  const updateSubscriptionsAttributesMethod = async customAttributes => {
    await updateSubscriptionsAttributes({
      subscriptionContractId: contractId,
      customAttributesList: customAttributes
    });
    toggleUpdateAttributeModal();
  };

  const toggleUpdateOrderNoteModal = () => setIsUpdateOrderNoteOpenFlag(!isUpdateOrderNoteOpenFlag);
  const toggleUpdateAttributeModal = () => setIsUpdateAttributeOpenFlag(!isUpdateAttributeOpenFlag);

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

  const analyticsDataAxios = subContractId => {
    setIsAnalyticsProcess(true);
    axios
      .get(`/api/subscription-contract-details/analytics/${subContractId}`)
      .then(data => {
        setSubAnalyticsData(data?.data);
        setSubAnalysisToggleModel(true);
        setIsAnalyticsProcess(false);
      })
      .catch(err => {
        console.log(err);
        setIsAnalyticsProcess(false);
      });
  };

  const updateDeliveryPriceOverride = () => {
    subscriptionContractDetailsEntity.allowDeliveryPriceOverride = !isAllowDeliveryPriceOverride;
    updateEntity(subscriptionContractDetailsEntity);
  };

  const updateContractStatus = (contractId, status) => {
    setUpdatingContractStatus(true);
    axios.put(`/api/subscription-contracts-update-status?contractId=${contractId}&status=${status}`).then(res => {
      if (res.status == 200) {
        setUpdatingContractStatus(false);
        getEntity(contractId);
        if (status == 'PAUSED') {
          toast.success(customerPortalSettingEntity.subscriptionPausedMessageText, options);
        } else {
          toast.success(customerPortalSettingEntity.subscriptionActivatedMessageText, options);
        }
        setContinuePausedOrActiveProcess(false)
      } else {
        toast.error(customerPortalSettingEntity.unableToUpdateSubscriptionStatusMessageText);
        setUpdatingContractStatus(false);
        setContinuePausedOrActiveProcess(false)
      }
    });
  };

  const isSubAnalysisToggle = () => {
    setSubAnalysisToggleModel(false);
  };

  const getDiscountCodeFromID = (subscriptionEntities, prdDiscountNode) => {
    let discountData = subscriptionEntities?.discounts?.edges?.find(discountNode => {
      return discountNode?.node?.id === prdDiscountNode?.discount?.id;
    });
    return 'Code:' + discountData?.node?.title;
  };

  useEffect(() => {
    let flag = true;
    // if (!isPrepaid) {
    if (Array.isArray(subUpcomingOrderEntities)) {
      subUpcomingOrderEntities?.map((ordData, index) => {
        if (ordData.status == 'QUEUED' && flag) {
          setIsAttemptBillingDisplayedOnce(index);
          flag = false;
        }
      });
    } else {
      setIsAttemptBillingDisplayedOnce(-1);
    }
    // }
  }, [subUpcomingOrderEntities]);

  const handlePaginationPastOrder = activePage => {
    setPastOrderActivePage(activePage);
  };

  const addDiscountCode = async (contractId, discountCopoun, shopName) => {
    let results = await applyDiscount(contractId, discountCopoun, shopName);
    let isDiscountApplied = false;
    if (results?.value?.data?.discounts && results?.value?.data?.discounts?.edges?.length) {
      results?.value?.data?.discounts?.edges.forEach(edge => {
        if (edge?.node?.title === discountCopoun) {
          isDiscountApplied = true;
        }
      });
    }
    if (isDiscountApplied) {
      toast.success('Discount applied.', options);
    } else {
      toast.error('Discount apply failed.', options);
    }
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

  const processSplitContract = () => {
    setOpenSplitContractPopup(false);
    splitSubscriptionContract(contractId, true, splitWithOrder, splitContractProducts);
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
  const saveQuantity = () => {
    if (!isQuantityInputInValid) {
      setIsEditQuantityInProgress(true);
      axios
        .put(
          `/api/subscription-contract-one-offs-update-quantity?contractId=${contractId}&billingAttemptId=${billingAttemptId}&variantId=${quantityChangeProductID}&quantity=${quantity}`
        )
        .then(res => {
          return getEntity(contractId);
        })
        .then(res => {
          setIsEditQuantityInProgress(false);
          setIsEditQuantity(false);
          toast.success('Quantity Updated', options);
        })
        .catch(err => {
          setIsEditQuantityInProgress(false);
          toast.error('Quantity Updated failed', options);
        });
    }
  };

  useEffect(() => {
    let upcomingAmount = 0;
    if (subscriptionEntities?.lines?.edges && subscriptionEntities?.lines?.edges?.length > 0) {
      subscriptionEntities?.lines?.edges?.map(value => {
        if (value?.node?.lineDiscountedPrice?.amount) {
          upcomingAmount += parseFloat(value?.node?.lineDiscountedPrice?.amount);
        }
      });
    }
    setTotalUpcomingAmount(upcomingAmount.toFixed(2))
  }, [subscriptionEntities]);

  return (
    subscriptionEntities &&
    subscriptionEntities?.lines?.edges?.length > 0 && (
      <Fragment>
        <SubAnalyticsViewModel
          subContractId={subscriptionEntities.id.split('/').pop()}
          analyticsData={subAnalyticsData}
          isToggle={() => isSubAnalysisToggle()}
          toggle={subAnalysisToggleModel}
        />


        {/*Split Contract Modal code start */}
        <Modal size="md" isOpen={openSplitContractPopup}
               toggle={() => setOpenSplitContractPopup(!openSplitContractPopup)}>
          <ModalHeader
            toggle={() => setOpenSplitContractPopup(!openSplitContractPopup)}
            close={() => setOpenSplitContractPopup(!openSplitContractPopup)}
          >
            Split Contract
          </ModalHeader>
          <ModalBody className="multiselect-modal-body">
            <div>
              <p>Once you confirm the split contract. A new, separate contract will be created.</p>
              <FormGroup className={'ml-5'}>
                {subscriptionEntities?.lines?.edges?.map(product => {
                  return product?.node && product?.node != null && product?.node?.productId != null ? (
                    <div className="d-flex align-items-center">
                      <Input
                        id={product?.node?.id}
                        checked={splitContractProducts.findIndex(item => item === product?.node?.id) > -1}
                        type="checkbox"
                        onChange={() => handleSplitContractProducts(product?.node?.id)}
                      />
                      <img src={product?.node?.variantImage?.transformedSrc} width={50}
                           style={{borderRadius: '29%', padding: '10px'}}/>
                      <a href={`https://${props.shopName}/products/${product?.node?.productId?.split('/')[4]}`}
                         target="_blank">
                        <span>
                          {' '}
                          {product?.node?.title}{' '}
                          {product?.node?.variantTitle && product?.node?.variantTitle !== '-' && '-' + product?.node?.variantTitle}
                        </span>
                      </a>
                    </div>
                  ) : (
                    ''
                  );
                })}
              </FormGroup>
            </div>
          </ModalBody>
          <ModalFooter>
            <div className={"w-100 d-flex justify-content-between"}>
              <div>
                <Input type={"select"} checked={splitWithOrder}
                       onChange={(event) => setSplitWithOrder(event.target.value)}>
                  <option value={false}>Split without order placed</option>
                  <option value={true}>Split with order placed</option>
                </Input>
              </div>
              <div>
                <Button color="danger" onClick={() => setOpenSplitContractPopup(!openSplitContractPopup)}>
                  Cancel
                </Button>
                <Button color="primary" onClick={() => processSplitContract()} disabled={splitContractUpdating}>
                  {splitContractUpdating ? (
                    <div className="d-flex">
                      <div className="appstle_loadersmall"/>
                      <span className="ml-2"> Please Wait..</span>
                    </div>
                  ) : (
                    'Confirm'
                  )}
                </Button>
              </div>
            </div>
          </ModalFooter>
        </Modal>


        <Modal size="md" isOpen={continuePausedOrActiveProcess}
               toggle={() => setContinuePausedOrActiveProcess(!continuePausedOrActiveProcess)}>
          <ModalHeader
            toggle={() => setContinuePausedOrActiveProcess(!continuePausedOrActiveProcess)}
            close={() => setContinuePausedOrActiveProcess(!continuePausedOrActiveProcess)}
          >
            {subscriptionEntities.status == 'ACTIVE' ? 'Pause' : 'Active'} Contract
          </ModalHeader>
          <ModalBody className="multiselect-modal-body">
            <div>
              <p>Are you sure you want to {subscriptionEntities.status == 'ACTIVE' ? 'pause' : 'active'} the subscription?</p>
            </div>
          </ModalBody>
          <ModalFooter>
            <div className={"w-100 d-flex justify-content-end"}>
              <Button color="danger" className={"m-1"}
                      onClick={() => setContinuePausedOrActiveProcess(!continuePausedOrActiveProcess)}>
                Cancel
              </Button>
              <Button color="primary" className={"m-1"} onClick={() =>
                updateContractStatus(
                  subscriptionEntities.id.split('/').pop(),
                  subscriptionEntities.status == 'ACTIVE' ? 'PAUSED' : 'ACTIVE'
                )
              } disabled={updatingContractStatus}>
                {updatingContractStatus ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall"/>
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  'Confirm'
                )}
              </Button>
            </div>
          </ModalFooter>
        </Modal>

        <Modal size="md" isOpen={cancelSubscriptionProcess}
               toggle={() => setCancelSubscriptionProcess(!cancelSubscriptionProcess)}>
          <ModalHeader
            toggle={() => setCancelSubscriptionProcess(!cancelSubscriptionProcess)}
            close={() => setCancelSubscriptionProcess(!cancelSubscriptionProcess)}
          >
            Cancel Contract
          </ModalHeader>
          <ModalBody className="multiselect-modal-body">
            <div>
              <p>Are you sure want to cancel subscription ?</p>
            </div>
          </ModalBody>
          <ModalFooter>
            <div className={"w-100 d-flex justify-content-end"}>
              <Button color="danger" className={"m-1"}
                      onClick={() => setCancelSubscriptionProcess(!cancelSubscriptionProcess)}>
                Go Back
              </Button>
              <Button color="primary" className={"m-1"} onClick={() => {
                cancelSubscription(contractId)
                setCancelSubscriptionProcess(false)
              }
              } disabled={updating}>
                {updating ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall"/>
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  'Confirm'
                )}
              </Button>
            </div>
          </ModalFooter>
        </Modal>
        {/*Split Contract Modal code end */}
        {(currentCycle == subscriptionEntities?.billingPolicy?.maxCycles) && (
          <Alert color="warning">
            For this subscription, the maximum number of orders is set to 
            {subscriptionEntities?.billingPolicy?.maxCycles}, which has been already
            reached (when the {subscriptionEntities?.billingPolicy?.maxCycles}
            {subscriptionEntities?.billingPolicy?.maxCycles.toString().split().pop() == '1'
                ? 'st'
                : subscriptionEntities?.billingPolicy?.maxCycles.toString().split().pop() == '2'
                ? 'nd'
                : subscriptionEntities?.billingPolicy?.maxCycles.toString().split().pop() == '3'
                ? 'rd'
                : 'th'} order of the subscription contract got placed).
            <strong>There will be no further processing of payments.</strong>
            You need to remove this condition first in order to have further payments generated.
          </Alert>
        )}
        <Card className="card-hover-shadow-2x mb-3">

          {/*Subscription header actions start*/}
          <CardHeader className="cardHeaderItemsWrapper">

            {/*Go back to subscriptions list */}
            <div style={{display: 'flex', alignItem: 'center'}}>
              <IoMdArrowBack
                size={22}
                className="mr-2 subscription-go-back-icon"
                style={{cursor: 'pointer'}}
                onClick={() =>
                  history.push({
                    pathname: location.state?.pathname || `/dashboards/subscriptions`,
                    state: {from: location.state?.from}
                  })
                }
              />
              Subscription #{subscriptionEntities.id.split('/')[4]} (Upcoming Order Subtotal:  {totalUpcomingAmount} {subscriptionEntities?.deliveryPrice?.currencyCode})
            </div>
            <div className="d-flex cardHeaderButtons">
              {subscriptionListEntities &&
              subscriptionListEntities.length > 0 &&
              props.currentSubscriptionIndex != -1 &&
              props.currentSubscriptionIndex != undefined &&
              props.currentSubscriptionIndex != null ? (
                <>
                  <Button
                    className="mr-2 previous-subscription-icon"
                    size="sm"
                    color={'primary'}
                    disabled={props.currentSubscriptionIndex <= 0 && props.currentSubscriptionIndex <= subscriptionListEntities.length - 1}
                    onClick={props.previousSubscriptionDetails}
                  >
                    <BsArrowLeftShort size={21} color="white"/>
                    Previous Subscription
                  </Button>
                  <Button
                    className="mr-2  next-subscription-icon"
                    size="sm"
                    color={'primary'}
                    disabled={props.currentSubscriptionIndex >= 0 && props.currentSubscriptionIndex >= subscriptionListEntities.length - 1}
                    onClick={props.nextSubscriptionDetails}
                  >
                    Next Subscription
                    <BsArrowRightShort size={21} color="white"/>
                  </Button>
                </>
              ) : (
                ''
              )}


              {/* <ResendEmails contractId={contractId}/> */}
              {subscriptionEntities?.lines?.edges?.length > 1 ? (
                <Button className="mr-2 split-contract-button" color={'warning'}
                        onClick={() => setOpenSplitContractPopup(!openSplitContractPopup)}>
                  {splitContractUpdating ? (
                    <div className="d-flex">
                      <div className="appstle_loadersmall"/>
                      <span className="ml-2"> Please Wait..</span>
                    </div>
                  ) : (
                    <>
                      <CallSplit/> Split Contract
                    </>
                  )}
                </Button>
              ) : (
                ''
              )}
              <Button
                className="mr-2 paused-or-active-subscription-icon"
                color={subscriptionEntities.status == 'ACTIVE' ? 'danger' : 'success'}
                onClick={() =>
                  setContinuePausedOrActiveProcess(!continuePausedOrActiveProcess)
                }
              >
                {updatingContractStatus ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall"/>
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  <>
                    {subscriptionEntities.status == 'ACTIVE' ? (
                      <>
                        {' '}
                        <PauseCircleFilled/> Pause Subscription{' '}
                      </>
                    ) : (
                      <>
                        {' '}
                        <PlayCircleFilled/> Activate Subscription{' '}
                      </>
                    )}
                  </>
                )}
              </Button>{' '}
              <Button color="primary" className={"view-subscription-analitycs-button"}
                      onClick={() => analyticsDataAxios(subscriptionEntities.id.split('/').pop())}>
                {isAnalyticsProcess ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall"/>
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  <>
                    <Assessment/> View Subscription Analysis
                  </>
                )}
              </Button>
            </div>
          </CardHeader>
          {/*Subscription header actions end*/}

          <CardBody>
            {subscriptionEntities?.lines?.edges?.length > 0 && (
              <Fragment>
                {updatePaymentSuccess && (
                  <Fragment>
                    <Alert
                      color="success"> {replaceVariablesValue(customerPortalSettingEntity?.updatePaymentMessageV2, {
                        'customer_email_id': subscriptionContractDetailsEntity?.customerEmail || ''
                      })}</Alert>
                  </Fragment>
                )}
                <FeatureAccessCheck
                  hasAnyAuthorities={'enableSubscriptionManagement'}
                  upgradeButtonText="Upgrade to manage subscription"
                  tooltip="To access update shipping feature, upgrade your plan"
                  designType={'LOCK'}
                >
                  <Row>
                    <Col md="5">
                      {/*Subscription Line Items Start*/}
                      {subscriptionEntities?.lines?.edges?.map((prd, index) => {

                        if (prd?.node && subscriptionContractDetailsEntity.subscriptionType != null && subscriptionContractDetailsEntity.subscriptionType === "BUILD_A_BOX_SINGLE_PRODUCT") {
                          return (
                            <SubscriptionSingleProductLineItem
                              prd={prd}
                              subscriptionEntities={subscriptionEntities}
                              isPrepaid={isPrepaid}
                              index={index}
                              contractId={contractId}
                              sellingPlanData={sellingPlanData}
                              currentCycle={currentCycle}
                              shopName={shopName}
                              currentCycleLoaded={currentCycleLoaded}
                            />
                          );
                        } else if (prd?.node) {
                          return (
                            <SubscriptionLineItem
                              prd={prd}
                              subscriptionEntities={subscriptionEntities}
                              isPrepaid={isPrepaid}
                              index={index}
                              contractId={contractId}
                              sellingPlanData={sellingPlanData}
                              currentCycle={currentCycle}
                              shopName={shopName}
                              currentCycleLoaded={currentCycleLoaded}
                              recurringProductsInSubscription={recurringProductsInSubscription}
                              freeProductsInSubscription={freeProductsInSubscription}
                              oneTimeProductsInSubscription={oneTimeProductsInSubscription}
                            />
                          );
                        }
                      })}

                      {activeOneTimeVariantList?.length > 0 &&
                        activeOneTimeVariantList?.map((prd, index) => (
                          <ListGroupItem className="mb-3" key={`2${index}}`}>
                            <div className="widget-content p-0">
                              <div className="widget-content-wrapper"
                                   style={{alignItems: 'stretch'}}>
                                <Fragment>
                                  <div className="widget-content-left">
                                    <img src={prd?.prdImage} width={100}
                                         style={{borderRadius: '2px'}}/>
                                  </div>
                                  <div
                                    className="widget-content-left ml-3 d-flex align-items-start"
                                    style={{flexDirection: 'column'}}>
                                    <div className="widget-heading">
                                      <h6 className="mb-2">
                                        <a href={`https://${shopName}/admin/products/${prd?.prdId}`}
                                           target="_blank">
                                          {prd.title}{' '}
                                          {prd?.variantTitle &&
                                            prd?.variantTitle !== '-' &&
                                            prd?.variantTitle !== 'Default Title' &&
                                            '-' + prd?.variantTitle}
                                        </a>
                                      </h6>
                                    </div>
                                    <span>
                                      <b>Quantity:</b>
                                      <span className="ml-2">{prd?.quantity}</span>{' '}
                                      {!isEditQuantity && (
                                        <a
                                          style={{
                                            color: '#545cd8',
                                            cursor: 'pointer'
                                          }}
                                          onClick={() => {
                                            setIsEditQuantity(true),
                                              setQuantityChangeProductID(prd?.id),
                                              setBillingAttemptId(prd?.billingAttemptId);
                                          }}
                                        >
                                          {' '}
                                          <i class="lnr lnr-pencil btn-icon-wrapper"></i>
                                        </a>
                                      )}
                                    </span>
                                    <span>
                                      <b>Amount:</b>
                                      <span className="badge  badge-alternate ml-2"
                                            style={{padding: '5px 6px', fontSize: '12px'}}>
                                        {prd?.price} {subscriptionEntities?.deliveryPrice?.currencyCode}
                                      </span>
                                    </span>
                                    <span>*Added as one time product</span>
                                  </div>
                                </Fragment>
                              </div>
                              <EditOneOffContractDetail billingID={prd?.billingAttemptId}
                                                        contractId={contractId}
                                                        variantId={prd?.id}/>
                            </div>
                          </ListGroupItem>
                        ))}
                      <div className="d-flex justify-content-end mt-3">
                        {subscriptionContractDetailsEntity.subscriptionType !== "BUILD_A_BOX_SINGLE_PRODUCT" ? (
                          <AddProductModal
                            upcomingOrderId={subUpcomingOrderEntities[0]?.id?.toString()}
                            value={JSON.stringify([])}
                            onChange={value => {
                              console.log(value);
                            }}
                            totalTitle="Add Product"
                            index={1}
                            methodName="Save"
                            buttonLabel="Add Products"
                            header="Select Product"
                            addHandler={addLineItem}
                            processing={isAddingProducts}
                          />) : (
                          "")}
                      </div>
                    </Col>

                    {/*Subscription order operations*/}
                    <Col md="4">
                      <div
                        style={{
                          borderRadius: '15px',
                          border: '1px solid darkblue',
                          boxShadow: '7px 8px #84818b',
                          padding: '13px 13px'
                        }}
                      >
                        <h4
                          className={"subscription-type"}
                          style={{
                            textAlign: 'center',
                            fontFamily: 'auto'
                          }}
                        >
                          {isPrepaid ? 'Prepaid Plan' : 'Pay As You Go Plan'}
                        </h4>
                        <hr/>
                        {
                          subscriptionContractDetailsEntity?.importedId != null &&
                        <div style={{display: 'flex', justifyContent: 'flex-end'}}>
                            <div
                              className="badge badge-pill badge-info">Imported</div>
                        </div>
                        }
                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                          <span style={{fontWeight: 'bold'}}>ORDER: </span>{' '}
                          <a
                            href={`https://${shopName}/admin/orders/${subscriptionEntities?.originOrder?.id?.split('/')[4]}`}
                            target="blank"
                          >
                            {' '}
                            {subscriptionEntities?.originOrder?.name}
                          </a>
                        </div>

                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                          <span style={{fontWeight: 'bold'}}>Created At: </span>{' '}
                            {subscriptionEntities?.createdAt ? convertToShopTimeZoneDate(subscriptionEntities?.createdAt, shopInfo.ianaTimeZone) : '-'}
                            {/* {subscriptionEntities?.createdAt ? moment(subscriptionEntities?.createdAt).format('MMMM DD, YYYY') : '-'} */}
                        </div>
                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                          <span style={{fontWeight: 'bold'}}>Status: </span>
                          {subscriptionEntities.status.toLowerCase() === 'active' ? (
                            <div
                              className="badge badge-pill badge-success">{subscriptionEntities.status}</div>
                          ) : (
                            <div
                              className="badge badge-pill badge-secondary">{subscriptionEntities.status}</div>
                          )}
                        </div>
                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                          <span style={{fontWeight: 'bold'}}>Total Products: </span>
                          {subscriptionEntities?.lines?.edges?.length}
                        </div>
                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                          <span style={{fontWeight: 'bold'}}>Current Billing Cycle: </span>
                          {currentCycle}
                        </div>
                        {/* <div className="mb-1"
                             style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                          <b>Delivery Method: </b>
                          {subscriptionEntities?.deliveryMethod?.shippingOption?.title}
                        </div> */}

                        <>
                          {isEditDeliveryMethod ? (
                            <>
                              <div
                                className="mb-1"
                                style={{
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'space-between'
                                }}
                              >
                                                                <span
                                                                  style={{fontWeight: 'bold'}}>{'Delivery Method'}: </span>
                                                              {subscriptionEntities?.deliveryMethod?.shippingOption?.title ? subscriptionEntities?.deliveryMethod?.shippingOption?.title : subscriptionEntities?.deliveryMethod?.pickupOption?.title}
                                <Button
                                  color="link"
                                  title="Edit Delivery Method"
                                  className="btn-icon btn-icon-only btn-pill"
                                  active
                                  onClick={() => setIsEditDeliveryMethod(true)}
                                >
                                  <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                </Button>
                              </div>
                              <Card className="card-margin" style={{borderRadius: '10%'}}>
                                <CardBody>
                                  <Input type="text" value={deliveryMethodValue}
                                         onChange={e => setDeliveryMethodValue(e?.target?.value)}/>

                                  <div style={{display: 'flex', marginTop: '10px'}}>
                                    <br/>
                                    <Button
                                      className="mr-2 btn-icon btn-icon-only"
                                      color="danger"
                                      title="Cancel"
                                      onClick={() => setIsEditDeliveryMethod(false)}
                                    >
                                      <i className="pe-7s-close btn-icon-wrapper"> </i>
                                    </Button>
                                    <Button
                                      className="mr-2 btn-icon btn-icon-only"
                                      color="success"
                                      title="Update"
                                      onClick={() => updateDeliveryMethod()}
                                    >
                                      {updatingDeliveryMethodName ? 'Processing...' :
                                        <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                    </Button>
                                  </div>
                                </CardBody>
                              </Card>
                            </>
                          ) : (
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                                                            <span
                                                              style={{fontWeight: 'bold'}}> {'Delivery Method'}: </span>
                              {subscriptionEntities?.deliveryMethod?.shippingOption?.title ? subscriptionEntities?.deliveryMethod?.shippingOption?.title : (subscriptionEntities?.deliveryMethod?.pickupOption?.title ? subscriptionEntities?.deliveryMethod?.pickupOption?.title : subscriptionEntities?.deliveryMethod?.localDeliveryOption?.title)}
                              <Button
                                color="link"
                                title="Edit Next Order"
                                className="btn-icon btn-icon-only btn-pill"
                                active
                                onClick={() => setIsEditDeliveryMethod(true)}
                              >
                                <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                              </Button>
                            </div>
                          )}
                        </>

                        {subscriptionEntities.status.toLowerCase() === 'active' && (
                          <>
                            {isEditNextOrder ? (
                              <>
                                <div
                                  className="mb-1"
                                  style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                  }}
                                >
                                  <span
                                    style={{fontWeight: 'bold'}}>{isPrepaid ? 'Next Billing Date' : 'Next Order'}: </span>
                                  {momentTZ(subscriptionEntities.nextBillingDate).tz(shopInfo?.ianaTimeZone).format('MMMM DD, YYYY hh:mm A')}
                                  <Button
                                    color="link"
                                    title="Edit Next Order"
                                    className="btn-icon btn-icon-only btn-pill"
                                    active
                                    onClick={() => setIsEditNextOrder(true)}
                                  >
                                    <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                  </Button>
                                </div>
                                <Card className="card-margin"
                                      style={{borderRadius: '10%'}}>
                                  <CardBody>
                                    <DatePickerWithTimezone
                                      selected={
                                        updatedNextOrderDate ? updatedNextOrderDate : new Date(moment.tz(subscriptionEntities?.nextBillingDate, shopInfo?.ianaTimeZone).format("YYYY-MM-DDTHH:mm:ss.SSS")) 
                                      }
                                      onChange={date => setUpdatedNextOrderDate(date)}
                                      timeInputLabel="Time:"
                                      minDate={addDays(new Date(), 1)}
                                      dateFormat="MM/dd/yyyy h:mm aa"
                                      timezone={shopInfo?.ianaTimeZone}
                                      showTimeInput
                                    />
                                    <div
                                      style={{
                                        display: 'flex',
                                        marginTop: '10px'
                                      }}
                                    >
                                      <br/>
                                      <Button
                                        className="mr-2 btn-icon btn-icon-only"
                                        color="danger"
                                        title="Cancel"
                                        onClick={() => setIsEditNextOrder(false)}
                                      >
                                        <i className="pe-7s-close btn-icon-wrapper"> </i>
                                      </Button>
                                      <Button
                                        className="mr-2 btn-icon btn-icon-only"
                                        color="success"
                                        title="Update"
                                        onClick={() => updateNextOrderDateMethod()}
                                      >
                                        {(props.updatingNextOrderFlag || checkAnchorDayNextOrderDate) ? (
                                          'Processing...'
                                        ) : (
                                          <i className="pe-7s-diskette btn-icon-wrapper"> </i>
                                        )}
                                      </Button>
                                    </div>
                                  </CardBody>
                                </Card>
                              </>
                            ) : (
                              <div
                                className="mb-1"
                                style={{
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'space-between'
                                }}
                              >
                              <span
                                style={{fontWeight: 'bold'}}> {isPrepaid ? 'Next Billing Date' : 'Next Order'}: </span>
                                {momentTZ(subscriptionEntities.nextBillingDate).tz(shopInfo?.ianaTimeZone).format('MMMM DD, YYYY hh:mm A')}
                                <Button
                                  color="link"
                                  title="Edit Next Order"
                                  className="btn-icon btn-icon-only btn-pill"
                                  active
                                  onClick={() => setIsEditNextOrder(true)}
                                >
                                  <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                </Button>
                              </div>
                            )}
                          </>
                        )}

                        {editBilling && !props.updateSuccess ? (
                          <Fragment>
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                              <span style={{fontWeight: 'bold'}}>Order frequency: </span>
                            </div>
                            <Form
                              initialValues={{
                                frequencyInterval: subscriptionEntities.billingPolicy.interval,
                                frequencyCount: subscriptionEntities.billingPolicy.intervalCount
                              }}
                              onSubmit={saveEntity}
                              render={({
                                         handleSubmit,
                                         form,
                                         submitting,
                                         pristine,
                                         values,
                                         errors
                                       }) => {
                                return (
                                  <form onSubmit={handleSubmit}>
                                    <Card className="card-margin">
                                      <CardBody>

                                        <Field
                                            type="select"
                                            id="sellingPlanId"
                                            name="sellingPlanId"
                                            render={({input, meta}) => (
                                              <>
                                              <Label>Select from existing selling plan.</Label>
                                              <Input
                                                invalid={meta.error && meta.touched ? true : null}
                                                style={{width: '100%', marginBottom: "20px"}}
                                                {...input}
                                              >
                                                <option value="">{"Please select"}</option>
                                                {!isPrepaid ? billingFrequencies?.map((opt, index) => {
                                                  return (
                                                    <option key={index} value={opt?.value}>
                                                      {opt?.name}
                                                    </option>
                                                  );
                                                }) : deliveryFrequencies?.map((opt, index) => {
                                                  return (
                                                    <option key={index} value={opt?.value}>
                                                      {opt?.name}
                                                    </option>
                                                  );
                                                })}
                                              </Input>
                                              </>

                                            )}
                                            className="form-control"
                                          />
                                      <div style={{fontWeight: "bold", textAlign: "center", marginBottom: "20px"}}>OR</div>
                                      <Label>Select Custom</Label>
                                        <FormGroup style={{display: 'flex'}}>
                                          <Field
                                            render={({input, meta}) => (
                                              <Fragment>
                                                <Input {...input}
                                                       invalid={meta.error && meta.touched ? true : null}/>
                                              </Fragment>
                                            )}
                                            validate={value => {
                                              if (!value) {
                                                return 'required.';
                                              } else {
                                                if (value <= 0) {
                                                  return 'required.';
                                                } else {
                                                  return undefined;
                                                }
                                              }
                                            }}
                                            type="number"
                                            id="frequencyCount"
                                            className="mr-2"
                                            style={{width: '100px'}}
                                            name="frequencyCount"
                                          />
                                          <Field
                                            type="select"
                                            id="frequencyInterval"
                                            name="frequencyInterval"
                                            render={({input, meta}) => (
                                              <Input
                                                invalid={meta.error && meta.touched ? true : null}
                                                style={{width: '100px'}}
                                                {...input}
                                              >
                                                <option value="DAY"
                                                        selected>
                                                  Days
                                                </option>
                                                <option
                                                  value="WEEK">Week
                                                </option>
                                                <option
                                                  value="MONTH">Month
                                                </option>
                                                <option
                                                  value="YEAR">Year
                                                </option>
                                              </Input>
                                            )}
                                            className="form-control"
                                          />
                                        </FormGroup>
                                        <div style={{display: 'flex'}}>
                                          <Button
                                            className="mr-2 btn-icon btn-icon-only"
                                            color="danger"
                                            title="Cancel"
                                            onClick={() => setEditBilling(false)}
                                          >
                                            <i className="pe-7s-close btn-icon-wrapper"> </i>
                                          </Button>
                                          <Button
                                            className="mr-2 btn-icon btn-icon-only"
                                            color="success" title="Save">
                                            {(props.updatingBilling || sellingPlanUpdateInProgress) ? 'Processing...' :
                                              <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                          </Button>
                                        </div>
                                      </CardBody>
                                    </Card>
                                  </form>
                                );
                              }}
                            />
                          </Fragment>
                        ) : (
                          <Fragment>
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                              <span style={{fontWeight: 'bold'}}>Order frequency: </span>
                              {subscriptionEntities.billingPolicy.intervalCount} {subscriptionEntities.billingPolicy.interval}
                              {subscriptionEntities.status.toLowerCase() === 'active' && (
                                <Button
                                  color="link"
                                  title="Edit Order Frequency"
                                  className="btn-icon btn-icon-only btn-pill"
                                  active
                                  onClick={() => setEditBilling(true)}
                                >
                                  <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                </Button>
                              )}
                            </div>
                          </Fragment>
                        )}

                        {isPrepaid &&
                          (editDelivery && !props.updateDelivery ? (
                            <Fragment>
                              <div
                                className="mb-1"
                                style={{
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'space-between'
                                }}
                              >
                                                                <span
                                                                  style={{fontWeight: 'bold'}}>Delivery frequency: </span>
                              </div>
                              <Form
                                initialValues={{
                                  deliveryFrequencyInterval: subscriptionEntities.deliveryPolicy.interval,
                                  deliveryFrequencyCount: subscriptionEntities.deliveryPolicy.intervalCount
                                }}
                                onSubmit={saveDeliveryEntity}
                                render={({
                                           handleSubmit,
                                           form,
                                           submitting,
                                           pristine,
                                           values,
                                           errors
                                         }) => {
                                  return (
                                    <form onSubmit={handleSubmit}>
                                      <Card className="card-margin">
                                        <CardBody>
                                          <FormGroup
                                            style={{display: 'flex'}}>
                                            <Field
                                              render={({input, meta}) => (
                                                <div>
                                                  <Input {...input}
                                                         invalid={meta.error && meta.touched ? true : null}/>
                                                  {meta.error && meta.touched &&
                                                    <span
                                                      className="text-danger">{meta.error}</span>}
                                                </div>
                                              )}
                                              validate={value => {
                                                if (!value) {
                                                  return 'required.';
                                                } else {
                                                  if (value <= 0) {
                                                    return 'enter valid value.';
                                                  } else if (
                                                    parseInt(subscriptionEntities?.billingPolicy?.intervalCount) % parseInt(value) !=
                                                    0
                                                  ) {
                                                    return 'delivery interval should be multiplication of billing frequency';
                                                  } else {
                                                    return undefined;
                                                  }
                                                }
                                              }}
                                              type="number"
                                              id="deliveryFrequencyCount"
                                              className="mr-2"
                                              style={{width: '100px'}}
                                              name="deliveryFrequencyCount"
                                            />
                                            <Field
                                              type="select"
                                              id="deliveryFrequencyInterval"
                                              name="deliveryFrequencyInterval"
                                              render={({input, meta}) => (
                                                <Input
                                                  invalid={meta.error && meta.touched ? true : null}
                                                  style={{width: '100px'}}
                                                  {...input}
                                                >
                                                  <option value="DAY"
                                                          selected>
                                                    Days
                                                  </option>
                                                  <option
                                                    value="WEEK">Week
                                                  </option>
                                                  <option
                                                    value="MONTH">Month
                                                  </option>
                                                  <option
                                                    value="YEAR">Year
                                                  </option>
                                                </Input>
                                              )}
                                              className="form-control"
                                            />
                                          </FormGroup>
                                          <div style={{display: 'flex'}}>
                                            <Button
                                              className="mr-2 btn-icon btn-icon-only"
                                              color="danger"
                                              title="Cancel"
                                              onClick={() => setEditDelivery(false)}
                                            >
                                              <i className="pe-7s-close btn-icon-wrapper"> </i>
                                            </Button>
                                            <Button
                                              className="mr-2 btn-icon btn-icon-only"
                                              color="success"
                                              title="Save">
                                              {props.updatingDeliveryInterval ? (
                                                'Processing...'
                                              ) : (
                                                <i className="pe-7s-diskette btn-icon-wrapper"> </i>
                                              )}
                                            </Button>
                                          </div>
                                        </CardBody>
                                      </Card>
                                    </form>
                                  );
                                }}
                              />
                            </Fragment>
                          ) : (
                            <Fragment>
                              <div
                                className="mb-1"
                                style={{
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'space-between'
                                }}
                              >
                                                                <span
                                                                  style={{fontWeight: 'bold'}}>Delivery frequency: </span>
                                {subscriptionEntities?.deliveryPolicy?.intervalCount} {subscriptionEntities?.deliveryPolicy?.interval}
                                {subscriptionEntities.status.toLowerCase() === 'active' && (
                                  <Button
                                    color="link"
                                    title="Edit Order Frequency"
                                    className="btn-icon btn-icon-only btn-pill"
                                    active
                                    onClick={() => setEditDelivery(true)}
                                  >
                                    <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                                  </Button>
                                )}
                              </div>
                            </Fragment>
                          ))}

                        <div
                          className="mb-1"
                          style={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                          }}
                        >
                                                    <span
                                                      style={{fontWeight: 'bold'}}> Allow Delivery Price Override: </span>
                          <div className="badge  badge-alternate ml-2"
                               style={{padding: '5px 6px', fontSize: '12px'}}></div>
                          <Switch
                            onChange={updateDeliveryPriceOverride}
                            onColor="#3ac47d"
                            checked={Boolean(isAllowDeliveryPriceOverride)}
                            handleDiameter={20}
                            boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                            activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                            height={17}
                            width={36}
                            id="material-switch"
                          />
                        </div>
                        {isEditDeliveryPrice && Boolean(isAllowDeliveryPriceOverride) ? (
                          <>
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                              <span style={{fontWeight: 'bold'}}> Delivery Price: </span>
                            </div>
                            <Card className="card-margin" style={{borderRadius: '10%'}}>
                              <CardBody>
                                <InputGroup>
                                  <Input
                                    type="number"
                                    defaultValue={parseFloat(subscriptionEntities?.deliveryPrice?.amount)}
                                    onChange={e => setUpdatedDeliveryPrice(e.target.value)}
                                  />
                                  <InputGroupAddon addonType="append">{subscriptionEntities?.deliveryPrice?.currencyCode}</InputGroupAddon>
                                </InputGroup>
                                <div style={{display: 'flex', marginTop: '10px'}}>
                                  <br/>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="danger"
                                    title="Cancel"
                                    onClick={() => setIsEditDeliveryPrice(false)}
                                  >
                                    <i className="pe-7s-close btn-icon-wrapper"> </i>
                                  </Button>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="success"
                                    title="Update"
                                    onClick={() => updateDeliveryPriceMethod()}
                                  >
                                    {updatingDeliveryPrice ? 'Processing...' :
                                      <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                  </Button>
                                </div>
                                <div
                                  style={{
                                    color: '#545cd8',
                                    fontSize: '11px',
                                    lineHeight: '1.2',
                                    marginTop: '12px'
                                  }}
                                >
                                  Note: New Delivery Price will ONLY work if "Allow
                                  Delivery Price Override" toggled on.
                                </div>
                              </CardBody>
                            </Card>
                          </>
                        ) : (
                          <div
                            className="mb-1"
                            style={{
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'space-between'
                            }}
                          >
                            <span style={{fontWeight: 'bold'}}> Delivery Price: </span>

                            <div className="badge  badge-alternate ml-2"
                                 style={{padding: '5px 6px', fontSize: '12px'}}>
                              {parseFloat(subscriptionEntities?.deliveryPrice?.amount)} {subscriptionEntities?.deliveryPrice?.currencyCode}
                            </div>
                            <Button
                              color="link"
                              title="Edit Delivery Price"
                              className="btn-icon btn-icon-only btn-pill"
                              active
                              onClick={() => setIsEditDeliveryPrice(true)}
                            >
                              {Boolean(isAllowDeliveryPriceOverride) &&
                                <i className="lnr lnr-pencil btn-icon-wrapper"></i>}
                            </Button>
                          </div>
                        )}
                        {isEditMinCycles ? (
                          <>
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                              <span style={{fontWeight: 'bold'}}>
                                {isPrepaid ? 'Minimum Number of billing Iterations: ' : 'Minimum Number of Orders: '}
                              </span>
                            </div>
                            <Card
                              className="card-margin"
                              style={{
                                borderRadius: '6px',
                                border: '1px solid #0000f6'
                              }}
                            >
                              <CardBody className="d-flex">
                                <InputGroup>
                                  <Input
                                    type="number"
                                    defaultValue={parseFloat(subscriptionEntities?.billingPolicy?.minCycles)}
                                    onChange={e => setUpdatedMinCycle(e.target.value)}
                                  />
                                  <InputGroupAddon addonType="append">Cycle</InputGroupAddon>
                                </InputGroup>
                                <div style={{display: 'flex', marginLeft: '5px'}}>
                                  <br/>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="danger"
                                    title="Cancel"
                                    onClick={() => setIsEditMinCycles(false)}
                                  >
                                    <i className="pe-7s-close btn-icon-wrapper"> </i>
                                  </Button>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="success"
                                    title="Update"
                                    onClick={() => updateMinCyclesMethod()}
                                  >
                                    {updatingMinCycles ? 'Processing...' :
                                      <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                  </Button>
                                </div>
                              </CardBody>
                            </Card>
                          </>
                        ) : (
                          <div
                            className="mb-1"
                            style={{
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'space-between'
                            }}
                          >
                            <span style={{fontWeight: 'bold'}}>
                              {isPrepaid ? 'Minimum Number of billing Iterations: ' : 'Minimum Number of Orders: '}
                            </span>{' '}
                            {subscriptionEntities?.billingPolicy?.minCycles ? subscriptionEntities?.billingPolicy?.minCycles : '-'}
                            <Button
                              color="link"
                              title="Edit Min Cycle"
                              className="btn-icon btn-icon-only btn-pill"
                              active
                              onClick={() => setIsEditMinCycles(true)}
                            >
                              <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                            </Button>
                          </div>
                        )}

                        {isEditMaxCycles ? (
                          <>
                            <div
                              className="mb-1"
                              style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between'
                              }}
                            >
                              <span style={{fontWeight: 'bold'}}>
                                {isPrepaid ? 'Maximum Number of billing Iterations: ' : 'Maximum Number of Orders: '}
                              </span>
                            </div>
                            <Card
                              className="card-margin"
                              style={{
                                borderRadius: '6px',
                                border: '1px solid #0000f6'
                              }}
                            >
                              <CardBody className="d-flex">
                                <InputGroup>
                                  <Input
                                    type="number"
                                    defaultValue={subscriptionEntities?.billingPolicy?.maxCycles}
                                    onChange={e => setUpdatedMaxCycle(e.target.value)}
                                  />
                                  <InputGroupAddon addonType='append'>Cycle</InputGroupAddon>
                                </InputGroup>
                                <div style={{display: 'flex', marginLeft: '5px'}}>
                                  <br/>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="danger"
                                    title="Cancel"
                                    onClick={() => setIsEditMaxCycles(false)}
                                  >
                                    <i className="pe-7s-close btn-icon-wrapper"> </i>
                                  </Button>
                                  <Button
                                    className="mr-2 btn-icon btn-icon-only"
                                    color="success"
                                    title="Update"
                                    onClick={() => updateMaxCyclesMethod()}
                                  >
                                    {updatingMaxCycles ? 'Processing...' :
                                      <i className="pe-7s-diskette btn-icon-wrapper"> </i>}
                                  </Button>
                                </div>
                              </CardBody>
                            </Card>
                          </>
                        ) : (
                          <div
                            className="mb-1"
                            style={{
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'space-between'
                            }}
                          >
                            <span style={{fontWeight: 'bold'}}>
                              {isPrepaid ? 'Maximum Number of billing Iterations: ' : 'Maximum Number of Orders: '}
                            </span>{' '}
                            {subscriptionEntities?.billingPolicy?.maxCycles ? subscriptionEntities?.billingPolicy?.maxCycles : '-'}
                            <Button
                              color="link"
                              title="Edit Max Cycle"
                              className="btn-icon btn-icon-only btn-pill"
                              active
                              onClick={() => setIsEditMaxCycles(true)}
                            >
                              <i className="lnr lnr-pencil btn-icon-wrapper"></i>
                            </Button>
                          </div>
                        )}
                        {subscriptionEntities?.discounts?.edges?.length > 0 ? (
                          subscriptionEntities?.discounts?.edges?.map(discount => {
                            return (
                              <>
                                <div
                                  class="d-flex align-items-center justify-content-space-between flex-wrap">
                                  <p>
                                    <b>Discount Coupon Applied: </b>
                                    <ul>
                                      <li>
                                        Code: {discount?.node?.title} @{' '}
                                        {discount?.node?.value?.percentage || discount?.node?.value?.amount?.amount}
                                        {discount?.node?.value?.percentage ? '% ' : discount?.node?.value?.amount?.currencyCode}
                                        {discount.node?.targetType == "SHIPPING_LINE" ? 'SHIPPING' : ''}
                                      </li>
                                      {discount?.node?.recurringCycleLimit && (
                                        <li>Recurring
                                          Limit: {discount?.node?.recurringCycleLimit}</li>
                                      )}
                                    </ul>
                                  </p>
                                  <Button
                                    color="danger"
                                    className="ml-auto primary btn btn-primary d-flex align-items-center"
                                    onClick={() => removeDiscount(contractId, discount?.node?.id, shopName)}
                                  >
                                    {!removeDiscountInProgress &&
                                      <i className="lnr lnr-trash btn-icon-wrapper"></i>}
                                    {removeDiscountInProgress &&
                                      <div className="appstle_loadersmall"/>}
                                  </Button>
                                </div>
                              </>
                            );
                          })
                        ) : (
                          <></>
                        )}
                              <>
                            <div
                              class="d-flex align-items-center justify-content-space-between">
                              <Input onInput={e => setDiscountCopoun(e.target.value)}
                                     placeholder="Apply Discount Coupon"/>
                              <Button
                                color="success"
                                style={{marginLeft: '13px'}}
                                className="primary btn btn-primary d-flex"
                                onClick={() => addDiscountCode(contractId, discountCopoun, shopName)}
                              >
                                {!removeDiscountInProgress && (
                                  <i style={{fontSize: '20px'}}
                                     className="pe-7s-diskette btn-icon-wrapper"></i>
                                )}
                                {removeDiscountInProgress &&
                                  <div className="appstle_loadersmall"/>}
                              </Button>
                            </div>
                            <div style={{display: 'flex', justifyContent: 'flex-end'}}>
                              Create a discount code:&nbsp;
                              <span
                                onClick={toggle}
                                style={{
                                  color: '#13b5ea',
                                  textDecoration: 'underline',
                                  cursor: 'pointer'
                                }}
                              >
                                click here
                              </span>
                            </div>
                            <AddDiscountCode showModal={showModalFlag} toggle={toggle}
                                             shopName={shopName} contractId={contractId}/>
                          </>
                      </div>
                      <br/>
                      <div style={detailborderstyle}>
                        <b className={"order-note-header"}>Order Note: </b>
                        <address>{subscriptionContractDetailsEntity?.orderNote}</address>
                        <Button
                          className="btn-icon btn-icon-only pl-0"
                          color="link"
                          title="Update Order Note"
                          onClick={toggleUpdateOrderNoteModal}
                        >
                          <i className="lnr lnr-pencil btn-icon-wrapper"></i> Update Order
                          Note
                        </Button>
                      </div>
                      <UpdateOrderNoteModal
                        isUpdating={updatingSubscriptionContractDetails}
                        modaltitle="Update Order Note"
                        confirmBtnText="Update"
                        cancelBtnText="Cancel"
                        initialOrderNote={subscriptionContractDetailsEntity}
                        isUpdateOrderNoteOpenFlag={isUpdateOrderNoteOpenFlag}
                        toggleUpdateOrderNoteModal={toggleUpdateOrderNoteModal}
                        updateOrderNoteMethod={subscriptionContractDetailsModel => updateOrderNoteMethod(subscriptionContractDetailsModel)}
                      />
                      <br/>
                      <div style={detailborderstyle}>
                        <b className={"note-attribute-header"}>Note Attributes: </b>
                        {subscriptionEntities.customAttributes.map((item, index) => {
                          return (
                            <div>
                              <span>{item.key}:</span>
                              <span>{item.value}</span>
                            </div>
                          );
                        })}

                        <div>
                          <Button
                            className="btn-icon btn-icon-only pl-0"
                            color="link"
                            title="Update Note Attribute"
                            onClick={toggleUpdateAttributeModal}
                          >
                            <i className="lnr lnr-pencil btn-icon-wrapper"></i> Update Note
                            Attributes
                          </Button>
                        </div>
                      </div>
                      <UpdateSubscriptionAttributesModal
                        isUpdating={updatingAttribute}
                        modaltitle="Update attributes"
                        confirmBtnText="Update"
                        cancelBtnText="Cancel"
                        subscriptionEntities={subscriptionEntities}
                        isUpdateAttributeOpenFlag={isUpdateAttributeOpenFlag}
                        toggleUpdateAttributeModal={toggleUpdateAttributeModal}
                        updateSubscriptionsAttributesMethod={customAttributes => updateSubscriptionsAttributesMethod(customAttributes)}
                      />
                      <br/>
                      {subscriptionEntities?.status?.toLowerCase() === 'cancelled' &&
                      subscriptionContractDetailsEntity?.cancellationFeedback ? (
                        <>
                          <br/>
                          <div style={detailborderstyle}>
                            <b>Cancellation feedback: </b>
                            <div>{subscriptionContractDetailsEntity?.cancellationFeedback}</div>
                            {subscriptionContractDetailsEntity.cancellationNote ? <><b>Cancellation Feedback Note: </b>
                              <p>{subscriptionContractDetailsEntity.cancellationNote}</p></> : ''}
                          </div>
                        </>
                      ) : (
                        <></>
                      )}
                    </Col>
                    {/*Subscription order operations end */}

                    {/*Subscription customer address, shipping details, billing, payment*/}
                    <Col md="3">
                      <div style={detailborderstyle}>
                        <b className={"customer-info-header"}>Customer Info: </b>
                        <address>
                          {
                            <>
                              {' '}
                              Name: {subscriptionContractDetailsEntity?.customerName ? subscriptionContractDetailsEntity?.customerName : ''}
                            </>
                          }
                          <br/>
                          {
                            <>
                              {' '}
                              Email:{' '}
                              {subscriptionContractDetailsEntity?.customerEmail ? subscriptionContractDetailsEntity?.customerEmail : ''}
                            </>
                          }
                            {subscriptionEntities?.deliveryMethod?.address?.phone  || subscriptionContractDetailsEntity?.phone ?
                             <>
                            <br/>
                             {' '}
                              Phone:{' '}
                             {  subscriptionContractDetailsEntity?.phone ? subscriptionContractDetailsEntity?.phone  :  subscriptionEntities?.deliveryMethod?.address?.phone  }
                             </>
                             : ""
                             }
                          <br/>
                          {subscriptionEntities?.customer?.id && (
                            <>
                              Id:{' '}
                              <Link
                                to={`/dashboards/customers/${subscriptionEntities?.customer?.id?.split('/')?.pop()}/edit`}>
                                {subscriptionEntities?.customer?.id?.split('/')?.pop()}
                              </Link>
                            </>
                          )}
                        </address>
                        <Button
                          className="btn-icon btn-icon-only pl-0"
                          color="link"
                          title="Update shipping address"
                          onClick={toggleUpdateCustomerInfoModal}
                        >
                          <i className="lnr lnr-pencil btn-icon-wrapper"></i> Update Customer
                          Info
                        </Button>
                      </div>
                      <UpdateCustomerInfoModel
                        isUpdating={updatingCustomerInfo}
                        modaltitle="Update customer Info"
                        confirmBtnText="Update"
                        cancelBtnText="Cancel"
                        initialCustomerInfo={{
                          customerId: subscriptionContractDetailsEntity?.customerId,
                          customerEmail: subscriptionContractDetailsEntity?.customerEmail,
                          customerFirstName: subscriptionContractDetailsEntity?.customerFirstName,
                          customerLastName: subscriptionContractDetailsEntity?.customerLastName
                        }}
                        isUpdateCustomerInfoOpenFlag={isUpdateCustomerInfoOpenFlag}
                        toggleUpdateCustomerInfoModal={toggleUpdateCustomerInfoModal}
                        updateCustomerInfoMethod={customerModel => updateCustomerInformation(customerModel)}
                      />

                      <br/>
                      <div style={detailborderstyle}>
                        {subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodShipping" ? (
                          <>
                            <b className={"shipping-address-header"}>Delivery Type: Standard Shipping</b>
                            <address>
                              <b className={"shipping-address-header"}>Shipping Address:</b><br/>
                              {' '}
                              {subscriptionEntities?.deliveryMethod?.address?.company ? <span>Company: {subscriptionEntities?.deliveryMethod?.address?.company}<br/></span> : ' '}
                              {subscriptionEntities?.deliveryMethod?.address?.name} <br/>
                              {subscriptionEntities?.deliveryMethod?.address?.address1}
                              <br/>
                              {subscriptionEntities?.deliveryMethod?.address?.city} ,{' '}
                              {subscriptionEntities?.deliveryMethod?.address?.province} -{' '}
                              {subscriptionEntities?.deliveryMethod?.address?.zip} <br />
                              {''}
                              {subscriptionEntities?.deliveryMethod?.address?.phone ? <span>Phone: {subscriptionEntities?.deliveryMethod?.address?.phone}<br/></span> : ' '}
                            </address>
                          </>
                        ) : subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodLocalDelivery" ? (
                          <>
                            <b className={"shipping-address-header"}>Delivery Type: Local Shipping</b>
                            <address>
                              <b className={"shipping-address-header"}>Shipping Address:</b><br/>
                              {' '}
                              {subscriptionEntities?.deliveryMethod?.address?.company ? <span>Company: {subscriptionEntities?.deliveryMethod?.address?.company}<br/></span> : ' '}
                              {subscriptionEntities?.deliveryMethod?.address?.name} <br/>
                              {subscriptionEntities?.deliveryMethod?.address?.address1}
                              <br/>
                              {subscriptionEntities?.deliveryMethod?.address?.city} ,{' '}
                              {subscriptionEntities?.deliveryMethod?.address?.province} -{' '}
                              {subscriptionEntities?.deliveryMethod?.address?.zip} <br />
                              {''}
                              {subscriptionEntities?.deliveryMethod?.address?.phone ? <span>Phone: {subscriptionEntities?.deliveryMethod?.address?.phone}<br/></span> : ' '}

                            </address>
                          </>
                        ) : subscriptionEntities?.deliveryMethod?.__typename === "SubscriptionDeliveryMethodPickup" ? (
                          <>
                            <b className={"shipping-address-header"}>Delivery Type: Local pickup</b>
                            <address>
                              <b className={"shipping-address-header"}>Pickup Address:</b><br/>
                              {' '}
                              {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.address1}
                              <br/>
                              {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.city} ,{' '}
                              {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.province} -{' '}
                              {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.zip} <br />
                              {''}
                              {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.phone ? <span>Phone: {subscriptionEntities?.deliveryMethod?.pickupOption?.location?.address?.phone}<br/></span> : ' '}
                            </address>
                          </>
                        ) : (
                          <p className="text-muted">Not Available</p>
                        )}

                        {<Button
                          className="btn-icon btn-icon-only pl-0"
                          color="link"
                          title="Update shipping address"
                          onClick={toggleUpdateShippingModal}
                        >
                          <i className="lnr lnr-pencil btn-icon-wrapper"></i> Update Shipping
                          Address
                        </Button>}
                      </div>

                      {/* UPDATE SHIPPING Address POPUP MODEL */}

                      <UpdateShippingAddressModel
                        isUpdating={updatingShippingAddress}
                        modaltitle="Update Shipping Address"
                        confirmBtnText="Update"
                        cancelBtnText="Cancel"
                        initialShippingAddress={subscriptionEntities?.deliveryMethod?.address}
                        isUpdateShippingOpenFlag={isUpdateShippingOpenFlag}
                        toggleShippingModal={toggleUpdateShippingModal}
                        updateShippingAddressMethod={addressModel => updateShippingAddressMethod(addressModel)}
                        subscriptionEntities={subscriptionEntities}
                      />
                      {/* UPDATE SHIPPING Address POPUP MODEL END*/}
                      <br/>
                      <div style={detailborderstyle}>
                        <b className={"billing-address-header"}>Billing Address: </b>
                        {paymentData && paymentData != null && (
                          <address>
                            {paymentData?.name} <br/>
                            {paymentData?.billingAddress?.address1} <br/>
                            {paymentData?.billingAddress?.city} , {paymentData?.billingAddress?.province} -{' '}
                            {paymentData?.billingAddress?.zip}
                          </address>
                        )}
                      </div>
                      <br/>
                      <div style={detailborderstyle}>
                        <b className={"payment-info-header"}>Payment info: </b>
                        <br/>
                        {!paymentData ? (
                          ' There is no payment method associated with this contract. It might have been revoked. Please create new contract for this customer. '
                        ) : paymentData != null && paymentData?.__typename === 'CustomerCreditCard' ? (
                          <address>
                            {paymentData?.brand ? paymentData?.brand?.toUpperCase() : 'Credit Card'}
                            <br/>
                            {'Ending with ' + paymentData?.lastDigits}
                            <br/>
                            {moment(paymentData?.expiryMonth, 'MM')?.format('MMMM')} {paymentData?.expiryYear}
                          </address>
                        ) : paymentData != null && paymentData?.__typename === 'CustomerShopPayAgreement' ? (
                          <b>
                            <Payment/> Shop Pay
                          </b>
                        ) : paymentData != null && paymentData?.__typename === 'CustomerPaypalBillingAgreement' ? (
                          <b>
                            <Payment/> Paypal Payment
                          </b>
                        ) : (
                          <b>
                            <Payment/> Unknown
                          </b>
                        )}
                        {paymentData && paymentData?.__typename !== 'CustomerShopPayAgreement' && (
                          <Button
                            className="btn-icon btn-icon-only pl-0"
                            color="link"
                            title="Update Payment Info"
                            onClick={() => editPaymentInfo()}
                          >
                            <i
                              className="lnr lnr-pencil btn-icon-wrapper"></i> {updatingPayment ? 'Processing..' : 'Update Payment Info'}
                          </Button>
                        )}
                        {paymentData && paymentData?.__typename === 'CustomerShopPayAgreement' && <a href="https://help.shop.app/hc/en-us/articles/4412203886996-How-do-I-manage-my-subscription-orders-with-Shop-Pay-" target="_blank">Click here to know more.</a>}
                      </div>
                    </Col>
                    {/*Subscription customer address, shipping details, billing, payment*/}
                  </Row>
                </FeatureAccessCheck>
                <hr></hr>

                <div id="accordion" className="accordion-wrapper mb-3">
                  {
                    <Fragment>
                      {isPrepaid && (
                        <Card className="main-card pt-3">
                          <CardHeader
                            style={{backgroundColor: '#eee'}}
                            onClick={() => toggleAccordion(0)}
                            aria-expanded={accordion[1]}
                            aria-controls="collapseOne"
                          >
                            <i className="header-icon lnr-arrow-up-circle icon-gradient bg-plum-plate"> </i>
                            {'Upcoming Fulfillments'}
                            <span
                              style={{
                                ...forward_arrow_icon,
                                transform: accordion[1] ? 'rotate(90deg)' : ''
                              }}
                            >
                              <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                            </span>
                          </CardHeader>
                          <Collapse isOpen={accordion[0]} data-parent="#accordion"
                                    id="collapseOne" aria-labelledby="headingOne">
                            <FeatureAccessCheck
                              hasAnyAuthorities={'enableSubscriptionManagement'}
                              upgradeButtonText="Upgrade to manage subscription"
                              tooltip="To access update shipping feature, upgrade your plan"
                              designType={'LOCK'}
                            >
                              <CardBody>
                                {/* UPCOMING FULFILLMENT SECTION */}
                                {isPrepaid && (
                                  <div
                                    style={{
                                      display: 'flex',
                                      justifyContent: 'flex-end'
                                    }}
                                  >
                                    <FormGroup
                                      style={{
                                        width: '300px',
                                        textAlign: 'right'
                                      }}
                                    >
                                      <Label>Select Order</Label>
                                      <Input type="select"
                                             onChange={event => selectOrderIdChangeIdChangeHandler(event)}>
                                        {pastOrderIds.map(id => (
                                          <option value={id}>{id}</option>
                                        ))}
                                      </Input>
                                    </FormGroup>
                                  </div>
                                )}
                                {!fulfillmentLoading &&
                                  isPrepaid &&
                                  fulfillmentOrderEntity?.fulfillmentOrders?.edges
                                    .sort((a, b) => new Date(a.node?.fulfillAt) - new Date(b.node?.fulfillAt))
                                    .map((fulfillmentData, index) => {
                                      // let flag = [];
                                      // if (fulfillmentData?.node) {
                                      //   fulfillmentData?.node?.lineItems?.edges?.forEach(item => {
                                      //     if (item?.node?.lineItem?.contract) {
                                      //       flag.push(true);
                                      //     }
                                      //   });
                                      // }
                                      // if (flag.length) {
                                        return (
                                          <>
                                            <UpcomingOrder
                                              isAttemptBillingDisplayedOnce={false}
                                              retryBillingAttempt={id => updateAttemptBilling(id)}
                                              skipShipment={skipShipment}
                                              isPrepaid={isPrepaid}
                                              isFulfillment={true}
                                              upcomingOneTimeVariants={upcomingOneTimeVariantList}
                                              upcomingSwapProductList={!index ? undefined : upcomingSwapProduct[index - 1]}
                                              ordData={fulfillmentData?.node}
                                              shopName={shopName}
                                              subscriptionEntities={subscriptionEntities}
                                              updatingSkipOrder={props.updatingSkipOrder}
                                              updatingBillingAttempt={props.updatingBillingAttempt}
                                              getSubscriptionEntity={getSubscriptionEntity}
                                              contractId={contractId}
                                              getSubscriptionContractDetailsByContractId={getSubscriptionContractDetailsByContractId}
                                              getUpcomingOrderEntityList={getUpcomingOrderEntityList}
                                            />
                                          </>
                                        );
                                      // }
                                    })}
                                {fulfillmentLoading && (
                                  <div
                                    style={{
                                      display: 'flex',
                                      justifyContent: 'center'
                                    }}
                                  >
                                    <Loader type="line-scale"/>
                                  </div>
                                )}
                              </CardBody>
                            </FeatureAccessCheck>
                          </Collapse>
                        </Card>
                      )}
                      <Card className="main-card pt-3">
                        <CardHeader
                          style={{backgroundColor: '#eee'}}
                          onClick={() => toggleAccordion(4)}
                          aria-expanded={accordion[4]}
                          aria-controls="collapseFour"
                        >
                          <i className="header-icon lnr-history icon-gradient bg-plum-plate"> </i>
                          Past Orders
                          <span
                            style={{
                              ...forward_arrow_icon,
                              transform: accordion[4] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                          </span>
                        </CardHeader>
                        <Collapse isOpen={accordion[4]} data-parent="#accordion"
                                  id="collapseFour" aria-labelledby="headingOne">
                          <CardBody>
                            {failedOrderLoading ? (
                              <div className="loader-container-inner text-center">
                                <div className="text-center">
                                  <Loader type="line-scale-party"/>
                                </div>
                                <h6 className="mt-3">Please wait while we load the
                                  data.</h6>
                              </div>
                            ) : (
                              <div>
                                {subFailedOrderEntities &&
                                  subFailedOrderEntities.length > 0 &&
                                  subFailedOrderEntities?.map((ordData, index) => {
                                    return (
                                      <PastOrder
                                        updatingBillingAttempt={props.updatingBillingAttempt}
                                        retryBillingAttempt={id => updateAttemptBillingEntity(id, shopName, contractId)}
                                        isPrepaid={isPrepaid}
                                        ordData={ordData}
                                        shopName={shopName}
                                        subscriptionEntities={subscriptionEntities}
                                      />
                                    );
                                  })}
                                <Row style={{textAlign: 'center'}}>
                                  <Col md={12}>
                                    <br/>
                                    <div
                                      style={{
                                        display: 'flex',
                                        justifyContent: 'center'
                                      }}
                                    >
                                      <Pagination
                                        activePage={pastOrderActivePage}
                                        itemsCountPerPage={pastOrderItemsPerPage}
                                        totalItemsCount={pastOrderTotalRowData}
                                        // pageRangeDisplayed={5}
                                        onChange={handlePaginationPastOrder}
                                      />
                                    </div>
                                    <JhiItemCount
                                      page={pastOrderActivePage}
                                      total={pastOrderTotalRowData}
                                      itemsPerPage={pastOrderItemsPerPage}
                                    />
                                  </Col>
                                </Row>
                              </div>
                            )}
                          </CardBody>
                        </Collapse>
                      </Card>

                      <Card className="main-card pt-3">
                        <CardHeader
                          style={{backgroundColor: '#eee'}}
                          onClick={() => toggleAccordion(1)}
                          aria-expanded={accordion[1]}
                          aria-controls="collapseOne"
                        >
                          <i className="header-icon lnr-arrow-up-circle icon-gradient bg-plum-plate"> </i>
                          Upcoming Orders
                          <span
                            style={{
                              ...forward_arrow_icon,
                              transform: accordion[1] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                          </span>
                        </CardHeader>
                        <Collapse isOpen={accordion[1]} data-parent="#accordion"
                                  id="collapseOne" aria-labelledby="headingOne">
                          <FeatureAccessCheck
                            hasAnyAuthorities={'enableSubscriptionManagement'}
                            upgradeButtonText="Upgrade to manage subscription"
                            tooltip="To access update shipping feature, upgrade your plan"
                            designType={'LOCK'}
                          >
                            <CardBody>
                              {subUpcomingOrderEntities &&
                                subUpcomingOrderEntities.length > 0 &&
                                subUpcomingOrderEntities
                                  ?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate))
                                  .map((ordData, index) => {
                                    if (shopInfo.carryForwardLastOrderNote) {
                                      if (!ordData.orderNote) {
                                        if (index == 0) {
                                          ordData.orderNote = subscriptionContractDetailsEntity?.orderNote;
                                        } else {
                                          ordData.orderNote = subUpcomingOrderEntities[index - 1].orderNote;
                                        }
                                      }
                                    }
                                    return (
                                      <UpcomingOrder
                                        isAttemptBillingDisplayedOnce={isAttemptBillingDisplayedOnce == index}
                                        retryBillingAttempt={id => updateAttemptBilling(id, shopName, contractId)}
                                        skipShipment={skipShipment}
                                        setBillingIsAttempted={setBillingIsAttempted}
                                        isPrepaid={isPrepaid}
                                        isFulfillment={false}
                                        upcomingOneTimeVariants={upcomingOneTimeVariantList}
                                        upcomingSwapProductList={!isPrepaid ? (!index ? undefined : upcomingSwapProduct[index - 1]) : upcomingSwapProduct[index] }
                                        ordData={ordData}
                                        shopName={shopName}
                                        subscriptionEntities={subscriptionEntities}
                                        updatingSkipOrder={props.updatingSkipOrder}
                                        updatingBillingAttempt={props.updatingBillingAttempt}
                                        contractId={contractId}
                                        getSubscriptionEntity={getSubscriptionEntity}
                                        getSubscriptionContractDetailsByContractId={getSubscriptionContractDetailsByContractId}
                                        getUpcomingOrderEntityList={getUpcomingOrderEntityList}
                                      />
                                    );
                                  })}
                            </CardBody>
                          </FeatureAccessCheck>
                        </Collapse>
                      </Card>
                      <Card className="main-card pt-3">
                        <CardHeader
                          className="b-radius-0"
                          style={{backgroundColor: '#eee'}}
                          onClick={() => toggleAccordion(2)}
                          aria-expanded={accordion[2]}
                          aria-controls="collapseTwo"
                        >
                          <i className="header-icon lnr-cart icon-gradient bg-plum-plate"> </i> Payment
                          Details
                          <span
                            style={{
                              ...forward_arrow_icon,
                              transform: accordion[2] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                          </span>
                        </CardHeader>
                        <Collapse isOpen={accordion[2]} data-parent="#accordion"
                                  id="collapseTwo">
                          <FeatureAccessCheck
                            hasAnyAuthorities={'enableSubscriptionManagement'}
                            upgradeButtonText="Upgrade to manage subscription"
                            tooltip="To access update shipping feature, upgrade your plan"
                            designType={'LOCK'}
                          >
                            <CardBody>
                              {!paymentData ? (
                                ' Payment details not available '
                              ) : paymentData?.__typename === 'CustomerCreditCard' ? (
                                <div className="grid-menu grid-menu-3col">
                                  <Row className="no-gutters">
                                    <Col xl="4" sm="6">
                                      <div
                                        className="btn-icon-vertical btn-square btn-transition text-center "
                                        outline color="primary">
                                        {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                        <span style={{fontSize: '15px'}}>
                                          {' '}
                                          <b>Payment Method Type</b>
                                        </span>
                                        <br></br>
                                        {paymentData?.__typename === 'CustomerCreditCard' ? 'Credit Card' : paymentData?.__typename}
                                      </div>
                                    </Col>
                                    <Col xl="4" sm="6">
                                      <div
                                        className="btn-icon-vertical btn-square btn-transition text-center text-capitalize"
                                        outline
                                        color="primary"
                                      >
                                        {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                        <span style={{fontSize: '15px'}}>
                                          {' '}
                                          <b>Card Type</b>
                                        </span>
                                        <br></br>
                                        {paymentData?.brand}
                                      </div>
                                    </Col>
                                    <Col xl="4" sm="6">
                                      <div
                                        className="btn-icon-vertical btn-square btn-transition text-center text-capitalize"
                                        outline
                                        color="primary"
                                      >
                                        {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                        <span style={{fontSize: '15px'}}>
                                          {' '}
                                          <b>Card Holder Name</b>
                                        </span>
                                        <br></br>
                                        {paymentData?.name}
                                      </div>
                                    </Col>
                                    <Col xl="4" sm="6">
                                      <div
                                        className="btn-icon-vertical btn-square btn-transition text-center"
                                        outline color="primary">
                                        {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                        <span style={{fontSize: '15px'}}>
                                          {' '}
                                          <b>Card Last 4 Digit</b>
                                        </span>
                                        <br></br>
                                        {paymentData?.lastDigits}
                                      </div>
                                    </Col>
                                    <Col xl="4" sm="6">
                                      <div
                                        className="btn-icon-vertical btn-square btn-transition text-center"
                                        outline color="primary">
                                        {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                        <span style={{fontSize: '15px'}}>
                                          {' '}
                                          <b>Card Expiry</b>
                                        </span>
                                        <br></br>
                                        {moment(paymentData?.expiryMonth, 'MM')?.format('MMMM')} {paymentData?.expiryYear}
                                      </div>
                                    </Col>
                                    <Col xl="4" sm="6"></Col>
                                  </Row>
                                </div>
                              ) : (
                                <Row>
                                  <Col>
                                    <div
                                      className="btn-icon-vertical btn-square btn-transition text-center "
                                      outline color="primary">
                                      {/* <i className="lnr-license btn-icon-wrapper"> </i> */}
                                      <span style={{fontSize: '15px'}}>
                                        {' '}
                                        <b>Payment Method Type</b>
                                      </span>
                                      {!paymentData ? (
                                        ' - '
                                      ) : paymentData != null && paymentData?.__typename === 'CustomerCreditCard' ? (
                                        <address>{paymentData?.brand ? paymentData?.brand?.toUpperCase() : 'Credit Card'}</address>
                                      ) : paymentData != null && paymentData?.__typename === 'CustomerShopPayAgreement' ? (
                                        <b>
                                          <Payment/> Shop Pay
                                        </b>
                                      ) : paymentData != null && paymentData?.__typename === 'CustomerPaypalBillingAgreement' ? (
                                        <b>
                                          <Payment/> Paypal Payment
                                        </b>
                                      ) : (
                                        <b>
                                          <Payment/> Unknown
                                        </b>
                                      )}
                                      {/* <Payment/> Paypal */}
                                    </div>
                                  </Col>
                                </Row>
                              )}
                              <div className="d-block text-center">
                                {updatePaymentSuccess && (
                                  <p>
                                    <br></br>
                                    <Alert
                                      color="success"> {replaceVariablesValue(customerPortalSettingEntity?.updatePaymentMessageV2, {
                                        'customer_email_id': subscriptionContractDetailsEntity?.customerEmail || ''
                                      })}</Alert>
                                  </p>
                                )}
                                <MySaveButton
                                  onClick={() => editPaymentInfo()}
                                  text="Update Payment Info"
                                  updating={updatingPayment}
                                  updatingText={'Processing'}
                                  className="btn-info"
                                >
                                  Update Payment Info
                                </MySaveButton>
                              </div>
                            </CardBody>
                          </FeatureAccessCheck>
                        </Collapse>
                      </Card>

                      <Card className="main-card pt-3">
                        <CardHeader
                          style={{backgroundColor: '#eee'}}
                          onClick={() => toggleAccordion(3)}
                          aria-expanded={accordion[3]}
                          aria-controls="collapseThree"
                        >
                          <i className="header-icon lnr-trash icon-gradient bg-plum-plate"> </i> Cancel
                          Subscription
                          <span
                            style={{
                              ...forward_arrow_icon,
                              transform: accordion[3] ? 'rotate(90deg)' : ''
                            }}
                          >
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                          </span>
                        </CardHeader>
                        <Collapse isOpen={accordion[3]} data-parent="#accordion"
                                  id="collapseThree">
                          <FeatureAccessCheck
                            hasAnyAuthorities={'enableSubscriptionManagement'}
                            upgradeButtonText="Upgrade to manage subscription"
                            tooltip="To access update shipping feature, upgrade your plan"
                            designType={'LOCK'}
                          >
                            <CardBody>
                              <div className="d-block text-center">
                                <MySaveButton
                                  onClick={() => {
                                    setCancelSubscriptionProcess(!cancelSubscriptionProcess)
                                  }}
                                  text="Cancel Subscription"
                                  addBuffer={!removeBuffer}
                                  updating={updating}
                                  updatingText={'Processing'}
                                  className="btn-danger"
                                >
                                  Cancel Subscription
                                </MySaveButton>
                              </div>
                            </CardBody>
                          </FeatureAccessCheck>
                        </Collapse>
                      </Card>
                    </Fragment>
                  }
                  <Card className="main-card pt-3">
                    <CardHeader
                      style={{backgroundColor: '#eee'}}
                      onClick={() => toggleAccordion(5)}
                      aria-expanded={accordion[5]}
                      aria-controls="collapseThree"
                    >
                      {/*<ListAlt className="header-icon"/>
                           Activity Log (Beta) &nbsp;
                          <span style={{...forward_arrow_icon, transform: accordion[5] ? 'rotate(90deg)' : ''}}>
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                          </span>*/}
                      <i className="header-icon lnr-menu-circle icon-gradient bg-plum-plate"> </i> Activity
                      Logs
                      <span
                        style={{
                          ...forward_arrow_icon,
                          transform: accordion[3] ? 'rotate(90deg)' : ''
                        }}
                      >
                            <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)"/>
                      </span>
                    </CardHeader>
                    <Collapse isOpen={accordion[5]} data-parent="#accordion" id="collapseThree">
                      <FeatureAccessCheck
                        hasAnyAuthorities={'accessSubscriptionActivityLogs'}
                        upgradeButtonText="Upgrade your plan"
                        tooltip="To access update shipping feature, upgrade your plan"
                        designType={'LOCK'}
                      >
                        <ErrorBoundary>
                          <SubscriptionActivityList subscriptionContractID={contractId}
                                                    subscriptionEntities={subscriptionEntities}/>
                        </ErrorBoundary>
                      </FeatureAccessCheck>
                    </Collapse>
                  </Card>
                </div>
              </Fragment>
            )}
          </CardBody>
        </Card>
        <SweetAlert
          title="Success"
          confirmButtonColor=""
          show={showOneTimeSuccessAlert}
          text="Products Added to the contract. One time products added will be visible under upcoming order section."
          type="success"
          onConfirm={() => setShowOneTimeSuccessAlert(false)}
        />
        <SweetAlert
          title="Success"
          confirmButtonColor=""
          show={billingIsAttempted}
          text="Products Billing is Attemted."
          type="success"
          onConfirm={() => setBillingIsAttempted(false)}
        />
        <Modal isOpen={isEditQuantity} toggle={() => setIsEditQuantity(old => !old)}>
          <ModalHeader>Edit Quantity</ModalHeader>
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
            <Button color="link" onClick={() => setIsEditQuantity(old => !old)}>
              Cancel
            </Button>
            <MySaveButton onClick={() => saveQuantity()} updating={isEditQuantityInProgress}/>
          </ModalFooter>
        </Modal>
      </Fragment>
    )
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
  updatingCustomerInfo: state.subscriptionContractDetails.updatingCustomerInfo,
  updateSuccessSubscriptionContractDetails: state.subscriptionContractDetails.updateSuccess,
  removeDiscountInProgress: state.subscription.removeDiscountInProgress,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  upcomingOrderDetailsEntity: state.subscriptionContractDetails.upcomingOrders,
  failedOrderLoading: state.subscriptionBillingAttempt.failedOrderLoading,
  totalFailedBillingAttemptItems: state.subscriptionBillingAttempt.totalFailedBillingAttemptItems,
  sellingPlanData: state.subscriptionGroup.sellingPlanData,
  splitContractUpdating: state.subscriptionContractDetails.updating,
  splitContractUpdated: state.subscriptionContractDetails.splitContractUpdated,
  splitContractDetails: state.subscriptionContractDetails.splitContractDetails,
  contracts: state.subscriptionContractDetails.contracts,
  subscriptionListEntities: state.subscriptionContractDetails.entities,
  updatingAttribute: state.subscriptionContractDetails.updatingAttribute
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
  getEmailSetting,
  splitSubscriptionContract,
  reset,
  updateCustomerInfo,
  updateSubscriptionsAttributes
};

export default connect(mapStateToProps, mapDispatchToProps)(MySubscriptionDetail);

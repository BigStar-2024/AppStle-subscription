import React, {useState, useEffect} from 'react';
import { PencilAltIcon, PencilIcon } from '@heroicons/react/20/solid';
import {DateTime} from 'luxon';
import TailwindModal from './TailwindModal';
import { DayPicker } from 'react-day-picker';
import PopupMessaging from './PopupMessaging';
import moment from 'moment';
import Axios from 'axios'
import {toast} from 'react-toastify';
import { getProductOptions, getProducts } from 'app/entities/fields-render/data-product.reducer';
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import { connect, useSelector } from 'react-redux';
import { isFreeProduct, isOneTimeProduct } from 'app/shared/util/subscription-utils';

import {Button, Input, Label, FormGroup, FormFeedback, Row, Col} from 'reactstrap';
import { formatPrice, getCustomerIdentifier } from 'app/shared/util/customer-utils';
import TailwindLoader from './Loader';
import Client from 'shopify-buy/index.unoptimized.umd';
import Loader from './Loader';

const instance = Axios.create();

function BandBox(props) {
    const {color,
      message,
      customerEntity,
      customerPortalSettingEntity,
      subscriptionEntities,
      getCustomerPortalEntity,
      contractId,
      productIds,
      isPrepaid,
      activeOneTimeProducts,
      isProductsListLoading,
      productsData,
      upcomingOrderId,
      getProducts,
      loading,
      subscriptionContractFreezeStatus,
      checkIfPreventCancellationBeforeDays,
      cancellationManagementEntity,
      isSwapProductModalOpen = false,
      updating,
      updateContractStatus,
      updatingContractStatus,
      deleteCustomerEntity,
      globalDiscounts,
      addDiscountCode,
      subUpcomingOrderEntities
     } = props
    const [isRescheduleModalOpen, setIsRescheduleModalOpen] = useState(false);
    const [showRescheduleMessaging, setShowRescheduleMessaging] = useState(false);
    const [rescheduleSuccess, setRescheduleSuccess] = useState(false);
    const [rescheduleError, setRescheduleError] = useState(false);
    const [updateInProgress, setUpdateInProgress] = useState(false);
    const [editMode, setEditMode] = useState(false);
    const [locale, setLocale] = useState(null);
    const [selectedDate, setSelectedDate] = useState(null);


    // let [updateInProgress, setUpdateInProgress] = useState(false);
    // const [editMode, setEditMode] = useState(false)
    const [isCountValid, setCountValid] = useState(true)
    const [selectedIntervalCount, setSelectedIntervalCount] = useState(subscriptionEntities?.billingPolicy?.intervalCount)
    const [selectedInterval, setSelectedInterval] = useState(subscriptionEntities?.billingPolicy?.interval)
    let [countInputBlurred, setCountInputBlurred] = useState(false);
    const [billingFrequencies, setBillingFrequencues] = useState([]);
    const [selectedContractSellingPlan, setSelectedContractSellingPlan] = useState(null)
    const [deliveryFrequencies, setDeliveryFrequencies] = useState([])
    const [isOpen, setIsOpen] = useState(false);

    const [showMessaging, setShowMessaging] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(false);

    const [cursor, setCursor] = useState(null);
    const [next, setNext] = useState(false);
    const [searchValue, setSearchValue] = useState('');
    const [collection, setCollection] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [infoMessage, setInfoMessage] = useState(false);
    const [isMoreProductLoading, setIsMoreProductLoading] = useState(false);
    const [sellingPlanIds, setSellingPlanIds] = useState([]);
    const [updatedCollection, setUpdatedCollection] = useState();
    const [purchaseOption, setPurchaseOption] = useState("SUBSCRIBE")
    const [selectedOnetimeProduct, setSelectedOnetimeProduct] = useState({})

    const [isAddProductModalOpen, setIsAddProductModalOpen] = useState(false)
    const [showMessagingAddProduct, setShowMessagingAddProduct] = useState(false);
    const [successAddProduct, setSuccessAddProduct] = useState(false);
    const [errorAddProduct, setErrorAddProduct] = useState(false);


    const [isCancelSubscriptionModalOpen, setIsCancelSubscriptionModalOpen] = useState(false);
    const [cancellationfeedback, setCancellationfeedback] = useState('');
    const [cancellationfeedbackValid, setCancellationfeedbackValid] = useState(true);
    const [showMessagingCancelSubscription, setShowMessagingCancelSubscription] = useState(false);
    const [successCancelSubscription, setSuccessCancelSubscription] = useState(false);
    const [errorCancelSubscription, setErrorCancelSubscription] = useState(false);
    const [hasCancellationDiscountCode, setHasCancellationDiscountCode] = useState(false);
    const [applyDiscountInProgress, setApplyDiscountInProgress] = useState(false);
    const [errorMessagePopUp, setErrorMessagePopUp] = useState('');
    const [cancellationTextAreaFeedback, setCancellationTextAreaFeedback] = useState('');
    const [pauseSubscriptionBtn, setPauseSubscriptionBtn] = useState(false);
    const [cancelSubscriptionBtn, setCancelSubscriptionBtn] = useState(false);

    const [isResumeSubscriptionModalOpen, setIsResumeSubscriptionModalOpen] = useState(false);
    const [showMessagingActivateSubscription, setShowMessagingActivateSubscription] = useState(false);
    const [successActivateSubscription, setSuccessActivateSubscription] = useState(false);
    const [errorActivateSubscription, setErrorActivateSubscription] = useState(false);

    let [isOpenSkipOrder, setIsOpenSkipOrder] = useState(false);
    const [skipOrderError, setSkipOrderError] = useState(null);
    const [skipShipmentInProgress, setSkipShipmentInProgress] = useState(false);
    const [showMessagingSkipOrder, setShowMessagingSkipOrder] = useState(false);
    const [successSkipOrder, setSuccessSkipOrder] = useState(false);
    const [errorSkipOrder, setErrorSkipOrder] = useState(false);

    const [validContractId, setValidContractId] = useState([]);
    const options = {
        autoClose: 500,
        position: toast.POSITION.BOTTOM_CENTER
      };

      let frequencyIntervalTranslate = {
        "week": customerPortalSettingEntity?.weekText,
        "day": customerPortalSettingEntity?.dayText,
        "month": customerPortalSettingEntity?.monthText,
        "year": customerPortalSettingEntity?.yearText,
      };
      const getFrequencyTitle = (interval) => {
        return frequencyIntervalTranslate[interval?.toLowerCase(interval)] ? frequencyIntervalTranslate[interval?.toLowerCase()] : interval;
      }
      const frequencyOptions = [
        {key: "DAY", title: customerPortalSettingEntity?.dayText},
        {key: "WEEK", title: customerPortalSettingEntity?.weekText},
        {key: "MONTH", title: customerPortalSettingEntity?.monthText},
        {key: "YEAR", title: customerPortalSettingEntity?.yearText},
      ];

      const customFrequencyOptions = [
        {key: "DAY", title: customerPortalSettingEntity?.dayText},
        {key: "WEEK", title: customerPortalSettingEntity?.weekText},
        {key: "MONTH", title: customerPortalSettingEntity?.monthText},
      ]

      const range = (min, max) => [...Array(max - min + 1).keys()].map(i => i + min);

      const getCustomIntervals = () => {
        let customIntervals = [];
        if (selectedInterval === "DAY") {
          customIntervals = range(1, 100);
        } else if (selectedInterval === "WEEK") {
          customIntervals = range(1, 16);
        } else if (selectedInterval === "MONTH") {
          customIntervals = range(1, 4);
        }
        return customIntervals;
      }


      useEffect(() => {
        setSelectedIntervalCount(1);
      }, [selectedInterval])


      useEffect(() => {
        Axios
          .get(`api/subscription-customers-detail/valid/${getCustomerIdentifier()}`)
          .then(res => {
            setValidContractId(res?.data?.sort(function (a, b) {
              if (a.status < b.status) {
                return -1;
              }
              if (a.status > b.status) {
                return 1;
              }
              return 0;
            }));
          })
          .catch(err => {
            console.log(err)
          });
      }, []);

    useEffect(() => {
        const importLocaleFile = async () => {
          const localeFormShop = window?.navigator?.userLanguage || window?.navigator?.language || window?.Shopify?.locale || 'en-US';
          const localeToSet = await import(`date-fns/locale/${localeFormShop}/index.js`);
          setLocale(localeToSet.default);
        };
        importLocaleFile();
      }, []);

      useEffect(() => {
        if (productIds && productIds?.length) {
          let tempProductIds = productIds.map(prd => `productIds=${prd}`)
          Axios.get(`/api/data/products-selling-plans?${tempProductIds.join("&")}`)
            .then(data => {
              let billingFrequencies = [];
              let deliveryFrequencies = [];
              data?.data?.forEach(opt => {
                opt?.sellingPlanGroups?.edges.forEach(edge => {
                  edge?.node?.sellingPlans.edges.forEach(sellingPlanEdge => {
                    let biilingFreq = {};
                    let deliveryFreq = {};
                    biilingFreq["name"] = sellingPlanEdge?.node?.billingPolicy?.intervalCount + ' ' + getFrequencyTitle(sellingPlanEdge?.node?.billingPolicy?.interval);
                    biilingFreq["value"] = sellingPlanEdge?.node?.billingPolicy?.intervalCount + ' ' + sellingPlanEdge?.node?.billingPolicy?.interval;
                    deliveryFreq["name"] = sellingPlanEdge?.node?.deliveryPolicy?.intervalCount + ' ' + getFrequencyTitle(sellingPlanEdge?.node?.deliveryPolicy?.interval);
                    deliveryFreq["value"] = sellingPlanEdge?.node?.deliveryPolicy?.intervalCount + ' ' + sellingPlanEdge?.node?.deliveryPolicy?.interval;
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
      }, [productIds])

    const resetRescheduleModal = () => {
        setIsRescheduleModalOpen(false);
        setShowRescheduleMessaging(false);
        setRescheduleSuccess(false);
        setRescheduleError(false);
    };
    const rescheduleSubscriptionHandler = async () => {
        let results = await updateDate();
        if (results) {
          setShowRescheduleMessaging(true);
          if (results?.status === 200) {
            setRescheduleSuccess(true);
            setRescheduleError(false);
          } else {
            setRescheduleSuccess(false);
            setRescheduleError(true);
          }
        }
    };

    const cancelSubscription = async values => {
      setCancelSubscriptionBtn(true);
      let results = '';
      if (cancellationManagementEntity?.cancellationType === 'CUSTOMER_RETENTION_FLOW') {
        if (cancellationfeedback || cancellationTextAreaFeedback) {
          setCancellationfeedbackValid(true);
          results = await deleteCustomerEntity(contractId, {
            cancellationFeedback: cancellationfeedback,
            cancellationTextAreaFeedback: cancellationTextAreaFeedback
          })
            .then(res => setIsCancelSubscriptionModalOpen(false))
            .catch(error => setErrorMessagePopUp(error?.response?.data?.detail));
        } else {
          setCancellationfeedbackValid(false);
          return;
        }
      } else {
        results = await deleteCustomerEntity(contractId, null)
          .then(res => res)
          .catch(error => setErrorMessagePopUp(error?.response?.data?.detail));
      }
      if (results) {
        setShowMessagingCancelSubscription(true);
        if (results?.value?.status === 204) {
          setSuccessCancelSubscription(true);
          setErrorCancelSubscription(false);
        } else {
          setSuccessCancelSubscription(false);
          setErrorCancelSubscription(true);
          setCancelSubscriptionBtn(false);
        }
      }
    };

    const resetModalCancelSubscription = () => {
      setIsCancelSubscriptionModalOpen(false);
      setShowMessagingCancelSubscription(false);
      setSuccessCancelSubscription(false);
      setErrorCancelSubscription(false);
      setCancellationfeedback('');
      setCancellationfeedbackValid(true);
      setApplyDiscountInProgress(false);
      setErrorMessagePopUp('');
      setCancellationTextAreaFeedback('');
      setCancelSubscriptionBtn(false);
      setPauseSubscriptionBtn(false);
    };

    const onDateChange = date => {
        setSelectedDate(date);
      };

    const updateDate = async () => {
        setUpdateInProgress(true);

        const prevDate = moment(subscriptionEntities?.nextBillingDate).toDate();
        const newDate = new Date(selectedDate);
        newDate.setHours(prevDate.getHours());
        newDate.setMinutes(prevDate.getMinutes());
        newDate.setSeconds(prevDate.getSeconds());

        const requestUrl = `api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${newDate.toISOString()}&isExternal=true`;
        console.log('requestUrl', requestUrl);
        return Axios.put(requestUrl)
          .then(res => {
            console.log(res);
            props.setNextOrderDateChangedResponce(res.data);
            getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            toast.success('Contract Updated', options);
            setEditMode(!editMode);
            return res;
          })
          .catch(err => {
            setUpdateInProgress(false);
            toast.error(err.response.data.message, options);
            return res;
          });
      };

      const resetModal = () => {
        setIsOpen(false);
        setShowMessaging(false);
        setSuccess(false);
        setError(false);
      }

      const resetAddProductModal = () => {
        setIsAddProductModalOpen(false);
        setShowMessagingAddProduct(false);
        setSuccessAddProduct(false);
        setErrorAddProduct(false);
      }

      const updateFrequency = async () => {
        let results = await update();
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
      }

      const skipShipment = async () => {
        setSkipOrderError('');
        setSkipShipmentInProgress(true);
        let results = await props.skipShipment(
          (subUpcomingOrderEntities?.sort((a, b) => new Date(a.node?.billingDate) - new Date(b.node?.billingDate)))[0]?.id,
          isPrepaid);
        if (results?.response?.data?.message) {
          setSkipOrderError(results?.response?.data?.message);
        }
        setSkipShipmentInProgress(false);
        if (results) {
          setShowMessagingSkipOrder(true);
          if (results?.status === 200) {
            setSuccessSkipOrder(true);
            setErrorSkipOrder(false);
          } else {
            setSuccessSkipOrder(false);
            setErrorSkipOrder(true);
          }
        }
      };

      const resetModalSkipOrder = () => {
        setIsOpenSkipOrder(false);
        setShowMessagingSkipOrder(false);
        setSuccessSkipOrder(false);
        setErrorSkipOrder(false);
      };

      const update = () => {
            setUpdateInProgress(true);
            const requestUrl = `api/subscription-contracts-update-billing-interval?contractId=${contractId}&interval=${selectedInterval}&intervalCount=${selectedIntervalCount}&isExternal=true`;
            return Axios
              .put(requestUrl)
              .then(res => {
                props.setNextOrderDateChangedResponce(res.data);
                getCustomerPortalEntity(contractId);
                setUpdateInProgress(false);
                toast.success('Contract Updated', options);
                setEditMode(!editMode)
                return res;
              })
              .catch(err => {
                setUpdateInProgress(false);
                toast.error(err.response.data.message, options);
                return err;
              });
        };

      const countChangeHandler = value => {
        setSelectedIntervalCount(value);
        if (!countInputBlurred) return;
        if (validateNumber(value)) {
          setCountValid(false);
        } else {
          setCountValid(true);
        }
      };
      const countBlurHandler = value => {
        setSelectedIntervalCount(value);
        setCountInputBlurred(true);
        if (validateNumber(value)) {
          setCountValid(false);
        } else {
          setCountValid(true);
        }
      };


      const selectProduct = (prdData) => {
        setSelectedOnetimeProduct(prdData);
        setIsAddProductModalOpen(true);
      }


      useEffect(() => {
        setCollection([]);
        setUpdatedCollection([]);
        setIsLoading(true);
        setInfoMessage(false);
        if (subscriptionEntities?.lines?.edges?.length) {
          let sellingPlans = subscriptionEntities?.lines?.edges?.map(line => {
            return line?.node?.sellingPlanId;
          });
          if (sellingPlans?.length) {
            setSellingPlanIds([...sellingPlans]);
          }
          loadProducts(
            null,
            false,
            searchValue,
            contractId,
            customerPortalSettingEntity?.productSelectionOption,
            purchaseOption,
            purchaseOption === 'SUBSCRIBE' && !isSwapProductModalOpen ? [...new Set(sellingPlans)] : []
          );
        }
      }, [purchaseOption, subscriptionEntities, customerPortalSettingEntity]);

      useEffect(() => {
        let sellingPlans = subscriptionEntities?.lines?.edges?.map(line => {
          return line?.node?.sellingPlanId;
        });
        if (sellingPlans?.length) {
          setSellingPlanIds([...sellingPlans]);
        }
      }, [subscriptionEntities]);

      useEffect(() => {
        if (productsData?.pageInfo != {}) {
          setCursor(productsData?.pageInfo?.cursor);
          setNext(productsData?.pageInfo?.hasNextPage);
        }

        if (productsData?.productHandleData) {
          fetchShopifyProducts(productsData?.productHandleData);
        }
        setIsMoreProductLoading(false);
      }, [productsData]);

      const handleSeeMore = (cursor, next) => {
        if (next && cursor) {
          setIsMoreProductLoading(true);
          loadProducts(
            cursor,
            next,
            searchValue,
            contractId,
            customerPortalSettingEntity?.productSelectionOption,
            purchaseOption,
            purchaseOption === 'SUBSCRIBE' && !isSwapProductModalOpen ? [...new Set(sellingPlanIds)] : []
          );
        }
      };

      const fetchShopifyProducts = async productHandleData => {
        if (productHandleData || productsData?.productHandleData?.length > 0) {
          setIsLoading(true);
          let productsArray = [];
          if (productHandleData) {
            productsArray = [
              ...Object.keys(productHandleData).map(key => {
                if (productHandleData?.[key]?.['handle']) {
                  return {
                    ...productHandleData[key]
                  };
                } else {
                  return {
                    handle: productHandleData[key]
                  };
                }
              })
            ];
          } else {
            setIsLoading(false);
            setInfoMessage(true);
            return;
          }

          if (!productsArray.length) {
            setIsLoading(false);
            setInfoMessage(true);
            return;
          }

          let resultsData = [];
          let globalIndex = 0;
          for await (let product of productsArray) {
            globalIndex = globalIndex + 1;
            var prdItem = {};
            const productvariantUrl = `${`https://${customerPortalSettingEntity.shop}` || `${location.origin}`}/products/${product.handle}.js`;
            let productResponse = null;
            try {
              delete instance.defaults.headers.common['Authorization'];
              productResponse = await instance.get(productvariantUrl);
            } catch (err) {
              console.log('Failed to fetched products from shop, may be due to store is password protected !');
            }
            if (productResponse) {
              let productvariants = productResponse.data;
              prdItem.imgSrc = productvariants?.featured_image ? `https:${productvariants?.featured_image}` : productvariants?.image?.src;
              prdItem.currencyCode = productvariants?.currencyCode;
              prdItem.prdHandleName = productvariants?.handle;
              prdItem.title = productvariants?.title;
              prdItem.requires_selling_plan = productvariants?.requires_selling_plan;
              prdItem.variants = [];
              prdItem.id = productvariants?.id;
              productvariants?.variants?.forEach(function(variant, i) {
                let isVariantValid = true;
                if (sellingPlanIds?.indexOf(null) === -1 && purchaseOption === 'SUBSCRIBE') {
                  let filteredSellingPlan = sellingPlanIds.map(plan => {
                    return Number(plan?.split('/').pop());
                  });
                  if (customerPortalSettingEntity?.productSelectionOption === 'PRODUCTS_FROM_ALL_PLANS') {
                    if (!variant?.selling_plan_allocations?.length) {
                      isVariantValid = false;
                    }
                  } else if (customerPortalSettingEntity?.productSelectionOption === 'PRODUCTS_FROM_CURRENT_PLAN') {
                    let hasSellingPlan = false;
                    variant?.selling_plan_allocations?.forEach(item => {
                      if (filteredSellingPlan?.indexOf(item?.selling_plan_id) !== -1) {
                        hasSellingPlan = true;
                      }
                    });
                    if (!hasSellingPlan) {
                      isVariantValid = false;
                    }
                  }
                }

                if (isVariantValid) {
                  var item = {};
                  if (productvariants.variants.length === 1) {
                    item.title = productvariants?.title;
                    item.price = productvariants?.price;
                  } else if (productvariants.variants.length > 1) {
                    item.title = `${variant?.title}`;
                    item.price = variant?.price;
                  }
                  item.id = `gid://shopify/ProductVariant/${variant?.id}`;
                  item.uniqueId = variant?.id;
                  item.selling_plan_allocations = variant?.selling_plan_allocations;

                  if (purchaseOption === 'SUBSCRIBE') {
                    if (variant?.available) {
                      prdItem.variants.push(item);
                    } else if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                      prdItem.variants.push(item);
                    }
                  } else {
                    if (variant?.available) {
                      prdItem.variants.push(item);
                    } else {
                      if (customerPortalSettingEntity?.includeOutOfStockProduct) {
                        prdItem.variants.push(item);
                      }
                    }
                  }
                }
              });
              resultsData.push(prdItem);

              let finalData = [...collection, ...resultsData].filter(
                (value, index, self) => index === self.findIndex(t => t?.prdHandleName === value?.prdHandleName)
              );
              setCollection(old => [...finalData]);
            }
            if (productsArray?.length === globalIndex) {
              setIsLoading(false);
              setInfoMessage(false);
            }
          }
        } else {
          setIsLoading(false);
          setInfoMessage(true);
        }
      };

      const loadProducts = (cursor, next, search, contractId, productSelectionOption, purchaseOption, sellingPlanIds) => {
        getProducts(
          `cursor=${cursor}&next=${next}&search=${encodeURIComponent(search)}&contractId=${contractId}&productSelectionOption=${
            purchaseOption === 'ONE_TIME' ? 'ALL_PRODUCTS' : productSelectionOption
          }&purchaseOption=${purchaseOption}&sellingPlanIds=${sellingPlanIds}`
        );
      };

      useEffect(() => {
        if (collection?.length) {
          if (purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag) {
            //Exclude subscription-only products from being added as a one time product, if purcahse option is one time and flag is on then dont allow subscription only product,
            setUpdatedCollection(collection.filter(item => !item.requires_selling_plan));
          } else {
            setUpdatedCollection(collection);
          }
        }
      }, [collection, customerPortalSettingEntity]);

      const addProduct = async () => {
        setUpdateInProgress(true);
        let selectProduct = {...selectedOnetimeProduct}
        const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${contractId}&quantity=1&variantId=${selectProduct?.variants[0]?.uniqueId}&isOneTimeProduct=true`;
        return Axios.put(requestUrl)
          .then(async res => {
            await getCustomerPortalEntity(contractId);
            setUpdateInProgress(false);
            return res;
          })
          .catch(async err => {
            await getCustomerPortalEntity(contractId);
                setUpdateInProgress(false);
                return err;
          });
      };

      const addItems = async () => {
        let results = await addProduct();
        if (results) {
          setShowMessagingAddProduct(true);
          if (results?.status === 200) {
            setSuccessAddProduct(true);
            setErrorAddProduct(false);
          } else {
            setSuccessAddProduct(false);
            setErrorAddProduct(true);
          }
        }
      };

      const  handleSubscriptionChange = async(cid) => {
        props?.history?.push(`/subscriptions/${cid}/detail`)
      }

      const pauseSubscription = async values => {
        setPauseSubscriptionBtn(true);
        let results = await updateContractStatus(
          subscriptionEntities?.id?.split('/').pop(),
          'PAUSED',
          cancellationManagementEntity?.pauseDurationCycle
        );
        if (results) {
          setShowMessagingCancelSubscription(true);
          if (results?.status === 200) {
            setSuccessCancelSubscription(true);
            setErrorCancelSubscription(false);
          } else {
            setSuccessCancelSubscription(false);
            setErrorCancelSubscription(true);
            setShowMessagingCancelSubscription(true);
            setPauseSubscriptionBtn(false);
          }
        }
      };

      const applyDiscount = async () => {
        setApplyDiscountInProgress(true);
        let results = '';
        const requestUrl = `/api/subscription-contracts-add-discount?${new URLSearchParams({
          discountTitle: 'cancel' + contractId,
          discountType: 'PERCENTAGE',
          contractId: contractId,
          percentage: customerPortalSettingEntity?.discountPercentageOnCancellation,
          recurringCycleLimit: customerPortalSettingEntity?.discountRecurringCycleLimitOnCancellation
        }).toString()}`;
        results = await axios.put(requestUrl);

        if (results) {
          setShowMessagingCancelSubscription(true);
          if (results?.status === 200) {
            setSuccessCancelSubscription(true);
            setErrorCancelSubscription(false);
          } else {
            setSuccessCancelSubscription(false);
            setErrorCancelSubscription(true);
          }
        }
        await getCustomerPortalEntity(contractId);
        setApplyDiscountInProgress(false);
      };

      const subscriptionAction = async (status) => {
        let results = await updateContractStatus(subscriptionEntities?.id?.split('/').pop(), status);
        if (results) {
            setShowMessagingActivateSubscription(true);
            if (results?.status === 204) {
                setSuccessActivateSubscription(true);
                setErrorActivateSubscription(false);
            } else {
                setSuccessActivateSubscription(false);
                setErrorActivateSubscription(true);
            }
        }
    }


    const resetModalActivateSubscription = () => {
        setIsResumeSubscriptionModalOpen(false);
        setShowMessagingActivateSubscription(false);
        setSuccessActivateSubscription(false);
        setErrorActivateSubscription(false);
    }

    return (
      <>
      {!loading && <>

        <div className='as-grid lg:as-grid-cols-12 as-gap-4'>
        <div className='as-subscription-action lg:as-col-span-3'>
          <div className="as-appreciation-text as-text-xs as-p-2 as-bg-[#f3f3f5]">
            <div class="as-flex as-customer-info">
              <div className='as-text-2xl as-text-[#1E4E79] as-mr-2'>{customerEntity?.firstName?.split("")?.splice(0,1)}{customerEntity?.lastName?.split("").splice(0,1)}</div>
              <div>{customerEntity?.firstName}, you have been a Bandbox member since {new Date(subscriptionEntities?.createdAt).toLocaleDateString()} and a music nerd since your were born. Thanks for going on your musical adventure with us!</div>
            </div>

          </div>
          <div className='as-actions-wrapper as-mt-4'>
            <div className='as-p-2 as-border-[#1E4E79] as-border-2 as-bg-[#f3f3f5] as-min-h-[234px]'>
                <div>
                  <h3 className='as-text-base as-text-[#1E4E79] as-text-left'>MEMBERSHIP ID</h3>
                  <div className='as-flex as-justify-between as-mt-2 as-text-xs'>
                    <select onChange={(event) => handleSubscriptionChange(event.target.value)} value={contractId} class="as-mt-1 as-block as-w-full as-rounded-md as-border as-border-gray-300 as-bg-white as-py-2 as-px-3 as-shadow-sm focus:as-border-[#1e4e79] focus:as-outline-none focus:as-ring-[#1e4e79] sm:as-text-sm">
                    {validContractId?.map(item => {
                      return <option value={item?.subscriptionContractId}>{item?.subscriptionContractId}</option>
                    })}
                  </select>
                  </div>
                </div>
                <div>
                  <h3 className='as-text-base as-text-[#1E4E79] as-text-left as-mt-4'>TERM</h3>
                  <div className='as-flex as-justify-between as-mt-2 as-text-xs'>
                    <p className=''>{customerPortalSettingEntity?.everyLabelTextV2} {' '}
                      {subscriptionEntities?.billingPolicy?.intervalCount} {' '} {subscriptionEntities?.billingPolicy?.interval}</p>
                    {subscriptionEntities?.status == "ACTIVE" && <div className='as-flex as-cursor-pointer as-mr-4'>
                      <div className='as-flex as-items-center as-text-[#1E4E79]' onClick={() => setIsCancelSubscriptionModalOpen(true)}>Edit<div className='as-ml-1'><PencilIcon className='as-w-4 as-h-4'/></div></div>
                    </div>}
                  </div>
                </div>
                {subscriptionEntities.status == "ACTIVE"  ? <>
                  <div className='as-mt-4'>
                    <h3 className='as-text-base as-text-[#1E4E79] as-text-left'>NEXT PAYMENT</h3>
                    <div className='as-flex as-justify-between as-mt-2 as-text-xs'>
                      <p className=''>{DateTime.fromISO(subscriptionEntities?.nextBillingDate).toFormat(customerPortalSettingEntity?.dateFormat)}</p>
                      <div className='as-flex as-cursor-pointer as-mr-4'>
                        <div className='as-flex as-items-center as-text-[#1E4E79]' onClick={() => setIsOpenSkipOrder(true)}>Edit<div className='as-ml-1'><PencilIcon className='as-w-4 as-h-4'/></div></div>
                      </div>
                    </div>
                  </div>
                  <div className='as-mt-4'>
                    <h3 className='as-text-base as-text-[#1E4E79] as-text-left'>REMINDER</h3>
                    <div className='as-flex as-justify-between as-mt-2 as-text-xs'>
                      <p className=''><sup>*</sup>Choose an album <span className='as-underline'>before the last day of the month</span> or you will receive the album that has been selected for you.</p>
                    </div>
                  </div>
                </> : <>
                <div className='as-mt-4'>
                    <h3 className='as-text-base as-text-[#1E4E79] as-text-left'>STATUS</h3>
                    <div className='as-flex as-justify-between as-mt-2 as-text-xs'>
                      <p className=''>{subscriptionEntities.status}</p>
                    </div>
                  </div>
                </>}

                <div className='as-mt-4'>
                  {subscriptionEntities.status == "PAUSED" &&
                  <button
                      onClick={() => setIsResumeSubscriptionModalOpen(true)}
                      disabled={subscriptionContractFreezeStatus || checkIfPreventCancellationBeforeDays(customerPortalSettingEntity?.preventCancellationBeforeDays, subscriptionEntities?.nextBillingDate)}
                      type="button"
                      class="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-green-900 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-green-900 as-bg-transparent hover:as-bg-green-900 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-green-700 disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-green-900 disabled:as-text-white as-button as-button--resume">
                          {customerPortalSettingEntity?.resumeSubscriptionText}
                  </button>}
                </div>

            </div>
          </div>
        </div>
        <div className='as-subscription-product lg:as-col-span-9 as-grid as-gap-4 lg:as-ml-10 as-text-left'>
          {subscriptionEntities?.lines?.edges?.map((prd, index) => {
            return (
              prd?.node &&
              prd?.node != null && (
                  <SubscriptionItem
                  prd={prd}
                  subscriptionEntities={subscriptionEntities}
                  contractId={contractId}
                  customerPortalSettingEntity={customerPortalSettingEntity}
                  getCustomerPortalEntity={getCustomerPortalEntity}
                  key={prd?.id}
                  />
              )
            );
          })}
        </div>

      </div>
          <TailwindModal
          open={isRescheduleModalOpen}
          setOpen={resetRescheduleModal}
          actionMethod={rescheduleSubscriptionHandler}
          actionButtonText={!showRescheduleMessaging ? customerPortalSettingEntity?.updateChangeOrderBtnTextV2 : ''}
          updatingFlag={updateInProgress}
          modalTitle={customerPortalSettingEntity?.rescheduleText || 'Reschedule'}
          className="as-model-reschedule"
          success={rescheduleSuccess}
        >
          {showRescheduleMessaging && <PopupMessaging showSuccess={rescheduleSuccess} showError={rescheduleError}/>}

          {!showRescheduleMessaging && (
            <div className="as-text-sm as-flex as-justify-center as-items-center">
              <DayPicker
                locale={locale}
                mode="single"
                selected={selectedDate}
                onSelect={date => onDateChange(date)}
                fromDate={new Date()}
                disabled={date => {
                  var enabledDays = customerPortalSettingEntity?.datePickerEnabledDaysV2.split(",").map(item => parseInt(item))
                  if (enabledDays.indexOf(date.getDay()) === -1) {
                    return true
                  } else {
                    return false
                  }
                }}
              />
            </div>
          )}
        </TailwindModal>
        <TailwindModal open={isOpen} setOpen={resetModal} actionMethod={updateFrequency}
                     actionButtonText={!showMessaging ? customerPortalSettingEntity?.updateFreqBtnText : ''}
                     updatingFlag={updateInProgress}
                     modalTitle={customerPortalSettingEntity?.editDeliveryInternalText || "Edit Delivery Interval"}
                     className="as-model-edit-delivery"
                     success={success}
      >
        {showMessaging && <PopupMessaging
          showSuccess={success}
          showError={error}
        />}
        {!showMessaging && <div className="as-text-sm as-text-gray-500">
          <div>
            <>
              <div>
                <div>
                  <p>
                    <div>
                      <div className="as-grid as-gap-4 as-grid-cols-12">
                        {false && <>
                          <div className="as-col-span-5">
                            <Input
                              value={selectedIntervalCount}
                              invalid={!isCountValid}
                              type="number"
                              onInput={event => countChangeHandler(event.target.value)}
                              onBlur={event => countBlurHandler(event.target.value)}
                              onChange={event => countChangeHandler(event.target.value)}
                              className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5 sm:as-text-sm as-border-gray-300 as-rounded-md"
                            />
                          </div>
                          <div className="as-col-span-7">
                            <Input
                              type="select"
                              className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                              value={selectedInterval}
                              onChange={event => {
                                setSelectedInterval(event.target.value);
                              }}
                            >
                              {frequencyOptions.map(opt => {
                                return (
                                  <option key={opt.key} value={opt?.key}>
                                    {opt?.title}
                                  </option>
                                );
                              })}
                            </Input>
                          </div>
                        </>}
                        {true &&
                          <div className="as-col-span-12">
                            <Input
                              type="select"
                              className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                              value={selectedContractSellingPlan}
                              onChange={event => {
                                setSelectedContractSellingPlan(event.target.value);
                                if (event.target.value) {
                                  countChangeHandler((event.target.value).split(" ")[0])
                                  setSelectedInterval((event.target.value).split(" ")[1]);
                                } else {
                                  countChangeHandler('')
                                  setSelectedInterval('');
                                }

                              }}
                            >
                              <option
                                value="">{customerPortalSettingEntity?.pleaseSelectText || "Please select"}</option>
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
                          </div>}
                      </div>
                    </div>
                  </p>
                </div>
              </div>
            </>
          </div>
        </div>}
      </TailwindModal>
      <TailwindModal open={isResumeSubscriptionModalOpen} setOpen={resetModalActivateSubscription} actionMethod={() => subscriptionAction("ACTIVE")} actionButtonText={!showMessagingActivateSubscription ? customerPortalSettingEntity?.confirmCommonText || "Confirm" : ""} updatingFlag={updatingContractStatus} modalTitle={customerPortalSettingEntity?.resumeSubscriptionText} className="as-model-resume-subscription" success={successActivateSubscription}>
        {showMessagingActivateSubscription && <PopupMessaging
            showSuccess={successActivateSubscription}
            showError={errorActivateSubscription}
        />}
        {!showMessagingActivateSubscription && <div className="as-text-sm as-text-gray-500">
            <p className="as-text-gray-500 as-text-sm">{customerPortalSettingEntity?.areyousureCommonMessageText || "Are you sure?"}</p>
        </div> }
    </TailwindModal>
      <TailwindModal open={isAddProductModalOpen} setOpen={resetAddProductModal} actionMethod={addItems}
                     actionButtonText={!showMessagingAddProduct ? 'ADD' : ''}
                     updatingFlag={updateInProgress}
                     modalTitle={"Add Product"}
                     className="as-model-edit-delivery"
                     success={successAddProduct}
      >
        {showMessagingAddProduct && <PopupMessaging
          showSuccess={successAddProduct}
          showError={errorAddProduct}
        />}
        {!showMessagingAddProduct && <div className="as-text-sm as-text-gray-500">
          <div>
            <>
              <div>
                <div md={8}>
                  <p>
                    <div>
                      <div className="">
                        Do you want to add "{selectedOnetimeProduct?.title}" for this month's shipment?
                      </div>
                    </div>
                  </p>
                </div>
              </div>
            </>
          </div>
        </div>}
      </TailwindModal>
      <TailwindModal
        open={isCancelSubscriptionModalOpen}
        setOpen={resetModalCancelSubscription}
        actionMethod={cancelSubscription}
        actionButtonText={
          !showMessagingCancelSubscription
            ? !subscriptionContractFreezeStatus &&
              !checkIfPreventCancellationBeforeDays(
                customerPortalSettingEntity?.preventCancellationBeforeDays,
                subscriptionEntities?.nextBillingDate
              ) &&
              cancellationManagementEntity.cancellationType !== 'CANCELLATION_INSTRUCTIONS'
              ? !isPrepaid
                ? customerPortalSettingEntity?.cancelSubscriptionPayAsYouGoButtonText ||
                  customerPortalSettingEntity?.confirmCommonText ||
                  'Confirm'
                : customerPortalSettingEntity?.cancelSubscriptionPrepaidButtonText ||
                  customerPortalSettingEntity?.confirmCommonText ||
                  'Confirm'
              : ''
            : ''
        }
        updatingFlag={updating}
        modalTitle={customerPortalSettingEntity?.cancelAccordionTitle}
        secondaryActionButtonText={
          !showMessagingCancelSubscription
            ? customerPortalSettingEntity?.offerDiscountOnCancellation && !hasCancellationDiscountCode
              ? customerPortalSettingEntity?.discountMessageOnCancellation
              : ''
            : ''
        }
        secondaryActionMethod={applyDiscount}
        secondaryActionUpdatingFlag={applyDiscountInProgress}
        className="as-model-cancel-subscription"
        success={successCancelSubscription}
        cancellationType={cancellationManagementEntity?.cancellationType}
        pauseSubscription={pauseSubscription}
        pauseSubscriptionBtn={pauseSubscriptionBtn}
        cancelSubscriptionBtn={cancelSubscriptionBtn}
      >
        {showMessagingCancelSubscription && <PopupMessaging showSuccess={successCancelSubscription} showError={errorCancelSubscription} errorMessage={errorMessagePopUp} />}
        {!showMessagingCancelSubscription && (
          <div className="as-text-sm as-text-gray-500">
            <div>
              <div>
                {(cancellationManagementEntity.cancellationType === 'CANCEL_IMMEDIATELY' ||
                  cancellationManagementEntity.cancellationType === 'CUSTOMER_RETENTION_FLOW') && (
                  <>
                    {!subscriptionContractFreezeStatus &&
                      !checkIfPreventCancellationBeforeDays(
                        customerPortalSettingEntity?.preventCancellationBeforeDays,
                        subscriptionEntities?.nextBillingDate
                      ) &&
                      cancellationManagementEntity.cancellationType === 'CUSTOMER_RETENTION_FLOW' && (
                        <div className="cancellationReasonSelectWrapper">
                          <label htmlFor="cancellationReason" className="as-block as-text-sm as-font-medium as-text-gray-700">
                            {customerPortalSettingEntity?.selectCancellationReasonLabelText || `Select Cancellation Reason*`}
                          </label>
                          <select
                            name="cancellationReason"
                            invalid={!cancellationfeedbackValid}
                            required
                            onChange={event => setCancellationfeedback(event.target.value)}
                            className="as-mt-2 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                          >
                            <option value="">{customerPortalSettingEntity?.selectCancellationReasonLabelText || 'Select reason'}</option>
                            {JSON.parse(cancellationManagementEntity?.cancellationReasonsJSON).map(option => {
                              return <option value={option?.cancellationReason}>{option?.cancellationReason}</option>;
                            })}
                          </select>
                          {!cancellationfeedbackValid && (
                            <p class="as-mt-2 as-text-sm as-text-red-600">
                              {customerPortalSettingEntity?.selectCancellationReasonRequiredMsg || 'please select cancellation reason'}
                            </p>
                          )}
                          <div className="custom-cancellation-textarea">
                            <div className="as-flex as-justify-between as-mt-4">
                              <label for="message" class="as-block as-text-sm as-font-medium as-text-gray-700">
                                {customerPortalSettingEntity?.cancellationReasonTextV2 || 'Cancellation Reason'} (optional)
                              </label>{' '}
                              <span className="as-text-xs">{cancellationTextAreaFeedback?.length}/1000</span>
                            </div>

                            <textarea
                              id="message"
                              rows="4"
                              class="as-block  as-w-full as-text-sm as-text-gray-900 as-bg-gray-50 as-rounded-lg as-border as-border-gray-300 focus:as-ring-blue-500 focus:as-border-blue-500 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:focus:as-border-blue-500"
                              placeholder="Your cancellation reason..."
                              value={cancellationTextAreaFeedback}
                              maxLength="1000"
                              onChange={e => setCancellationTextAreaFeedback(e.target.value)}
                            ></textarea>
                          </div>
                        </div>
                      )}
                    {subscriptionContractFreezeStatus ? (
                      <p className="as-text-gray-500 as-text-sm">
                        {customerPortalSettingEntity?.subscriptionContractFreezeMessageV2 ||
                          'Your subscription contract is frozen by your shop owner.'}
                      </p>
                    ) : (
                      checkIfPreventCancellationBeforeDays(
                        customerPortalSettingEntity?.preventCancellationBeforeDays,
                        subscriptionEntities?.nextBillingDate
                      ) && (
                        <p className="as-text-gray-500 as-text-sm">
                          {customerPortalSettingEntity?.preventCancellationBeforeDaysMessage?.replaceAll(
                            '{{preventDays}}',
                            `${customerPortalSettingEntity?.preventCancellationBeforeDays}`
                          ) ||
                            `You can not pause/cancel the subscription before the ${customerPortalSettingEntity?.preventCancellationBeforeDays} days from your next order date.`}
                        </p>
                      )
                    )}

                    {!subscriptionContractFreezeStatus &&
                      !checkIfPreventCancellationBeforeDays(
                        customerPortalSettingEntity?.preventCancellationBeforeDays,
                        subscriptionEntities?.nextBillingDate
                      ) &&
                      cancellationManagementEntity.cancellationType === 'CANCEL_IMMEDIATELY' && (
                        <>
                          {!isPrepaid && (
                            <p
                              className="as-text-gray-500 as-text-sm"
                              dangerouslySetInnerHTML={{
                                __html:
                                  customerPortalSettingEntity?.cancelSubscriptionConfirmPayAsYouGoText ||
                                  customerPortalSettingEntity?.deleteConfirmationMsgTextV2 ||
                                  'Are you sure?'
                              }}
                            />
                          )}
                          {isPrepaid && (
                            <p
                              className="as-text-gray-500 as-text-sm"
                              dangerouslySetInnerHTML={{
                                __html:
                                  customerPortalSettingEntity?.cancelSubscriptionConfirmPrepaidText ||
                                  customerPortalSettingEntity?.deleteConfirmationMsgTextV2 ||
                                  'Are you sure?'
                              }}
                            />
                          )}
                        </>
                      )}
                  </>
                )}

                {cancellationManagementEntity.cancellationType === 'CANCEL_AFTER_PAUSE' && (
                  <>
                    <div className="cancellationReasonSelectWrapper">
                      <div className="custom-cancellation-textarea">
                        <div className="as-flex as-justify-between as-mt-4">
                          <label for="message" class="as-block as-text-sm as-font-medium as-text-gray-700">
                            {(
                              cancellationManagementEntity?.pauseInstructionsText ||
                              `We certainly don't want you to cancel your subscription. How about we apply a temporary pause on the subscription of {{pauseDurationCycle}} number of cycles. After that, you can reach back to us to re-activate the subscription or you can activate the subscription from your customer portal.`
                            ).replace('{{pauseDurationCycle}}', cancellationManagementEntity?.pauseDurationCycle)}{' '}
                          </label>
                        </div>
                      </div>
                    </div>
                  </>
                )}

                {cancellationManagementEntity.cancellationType === 'CANCELLATION_INSTRUCTIONS' && (
                  <div dangerouslySetInnerHTML={{ __html: cancellationManagementEntity?.cancellationInstructionsText }}></div>
                )}
              </div>
            </div>
          </div>
        )}
      </TailwindModal>
      <TailwindModal
        open={isOpenSkipOrder}
        setOpen={resetModalSkipOrder}
        actionMethod={() => skipShipment(props?.ordData?.id, props?.isPrepaid)}
        actionButtonText={
          !showMessagingSkipOrder
            ? props?.isPrepaid
              ? customerPortalSettingEntity?.confirmSkipFulfillmentBtnText || 'Confirm Skip Fulfillment'
              : customerPortalSettingEntity?.confirmSkipOrder || 'Confirm Skip Order'
            : ''
        }
        updatingFlag={skipShipmentInProgress}
        modalTitle={
          props?.isPrepaid
            ? customerPortalSettingEntity?.skipFulfillmentButtonText || 'Skip Fulfillment'
            : customerPortalSettingEntity?.skipOrderButtonText || 'Skip Order'
        }
        className="as-model-skip-order"
        success={successSkipOrder}
      >
        {showMessagingSkipOrder && <PopupMessaging showSuccess={successSkipOrder} showError={errorSkipOrder} errorMessage={skipOrderError} />}
        {!showMessagingSkipOrder && (
          <div className="as-text-sm as-text-gray-500">
            <div>
              <p className="as-text-gray-500 as-text-sm"> {customerPortalSettingEntity?.frequencyChangeWarningTitle || 'Are you sure?'}</p>
            </div>
          </div>
        )}
      </TailwindModal>
      {subscriptionEntities?.status == "ACTIVE" && <div className='as-bandbox-add-product-wrapper as-py-6 as-mt-6 as-relative'>
        <h3 className='as-mt-8 as-mb-4 as-text-2xl as-text-white as-text-left as-add-product-section-header'>ADD AN EXTRA ALBUM TO THIS MONTH'S SHIPMENT</h3>
        <div
                className={`as-mt-2 as-pt-5 as-grid as-gap-4 md:as-grid-cols-2 lg:as-grid-cols-4 sm:as-pb-6 as-overflow-y-scroll`}
              >

                {updatedCollection &&
                  updatedCollection?.length > 0 &&
                  updatedCollection?.map((prdData, index) => {
                    var modifiedPrdData = JSON.parse(JSON.stringify(prdData));
                    modifiedPrdData.variants.forEach(varData => {
                      var sellingPrice = varData?.price;
                      varData.appstleSellingPrice = sellingPrice;
                      varData.appleSellingPriceWithCurrency = formatPrice(sellingPrice);
                    });
                    return (
                      modifiedPrdData?.variants?.length > 0 &&
                      !customerPortalSettingEntity?.disAllowVariantIdsForOneTimeProductAdd?.includes(
                        modifiedPrdData?.variants[0]?.uniqueId
                      ) &&
                      (purchaseOption === 'ONE_TIME' && customerPortalSettingEntity?.allowOnlyOneTimeProductOnAddProductFlag
                        ? !modifiedPrdData?.variants[0]?.requires_selling_plan
                        : true) && (
                        <AddProductItem
                          customerPortalSettingEntity={customerPortalSettingEntity}
                          modifiedPrdData={modifiedPrdData}
                          selectProduct={selectProduct}
                        />
                      )
                    );
                  })}
        </div>
        {!isLoading && !isProductsListLoading && next ? (
              <div className="as-flex as-justify-center">
                <button
                  type="button"
                  onClick={() => handleSeeMore(cursor, next)}
                  class="as-rounded-full font-bold as-inline-flex as-items-center as-justify-center as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-text-[#1e4e79] as-text-base as-font-medium as-bg-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-view-more-products"
                >
                  {isMoreProductLoading ? (
                    <TailwindLoader size="4" />
                  ) : (
                    customerPortalSettingEntity?.seeMoreProductBtnTextV2 || 'See More...'
                  )}
                </button>
              </div>
            ) : null}
            {isLoading || isProductsListLoading ? (
            <div className="as-flex as-justify-center as-mt-4">
              <TailwindLoader size="10" />
            </div>
          ) : (
            infoMessage && searchValue && <p className="as-text-gray-500 as-text-sm">{noProductDataMessage}</p>
          )}
      </div>}
      </>}
      {loading && (
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
      )}

      </>
  )
}

function AddProductItem({customerPortalSettingEntity, modifiedPrdData, selectProduct}) {
  const [prdData, setPrdData] = useState(null)

  useEffect(() => {
    getProductDetailById(modifiedPrdData?.id)
  }, [modifiedPrdData])


  const getProductDetailById = () => {
    const client = connection('df5c6665c5edff602daa7a24499fc5e8');
    const productQuery = client.graphQLClient.query(root => {
        root.addConnection(
          'products',
          {
            args: {
              first: 8,
              query: `id:${modifiedPrdData?.id}`
            }
          },
          product => {
            product.addField('metafield', { args: { namespace:"custom", key: 'artist_name' }  }, Metafield => {
              Metafield.add('id');
              Metafield.add('key');
              Metafield.add('namespace');
              Metafield.add('value');
            });
            product.add('title');
            // product.add('variants')
          }
        );
      });

      client.graphQLClient.send(productQuery).then(({ model, data, ...props }) => {
        if (!window['products']) {
          window['products'] = {}
        }
        const { products } = model;
        setPrdData(products["0"]);
        window['products'][modifiedPrdData?.id] = products["0"];
      });
    }

    const connection = storeFrontAccesKey => {
      const domain = 'bandbox-rocks.myshopify.com';
      return Client.buildClient({
          domain: domain,
          storefrontAccessToken: storeFrontAccesKey
      });
    };
  return (
    <>
    <div className="as-bg-white as-overflow-hidden as-card as-product-card as-flex as-flex-col">
      <div className="as-flex as-justify-between as-bg-[#f3f3f5] as-h-80">
        <img src={modifiedPrdData?.imgSrc} className=" as-h-full" />
      </div>
      <div className="as-mb-3 as-flex as-justify-between as-items-start as-p-2">
        <div>
        <h4
          className="as-add-product-title as-text-sm as-font-bold as-text-[#1e4e79]"
        >
          {prdData?.title}
        </h4>
        <p className='as-text-xs subtitle-artist-name as-mt-2 as-text-[#423a3f]'>{prdData?.metafield?.value}</p>
        <p className='as-text-xs priceWrapper as-mt-3'>
          {(modifiedPrdData?.variants[0]?.selling_plan_allocations[0]?.price !== modifiedPrdData?.variants[0]?.selling_plan_allocations[0]?.compare_at_price) && <span className='compareAtPrice as-mr-2 as-line-through'>{modifiedPrdData?.variants[0]?.appleSellingPriceWithCurrency}</span>}
          <span className='sellingPrice as-text-red-600 '>{formatPrice(modifiedPrdData?.variants[0]?.selling_plan_allocations[0]?.price)}</span>
        </p>
        <p className='as-text-xs member-price-text as-mt-2 as-text-[#423a3f] as-font-bold'>MEMBER PRICE</p>
        </div>

        <button
            type="button"
            onClick={() => selectProduct(modifiedPrdData)}
            class="as-text-sm as-ml-4 as-self-start as-rounded-full font-bold as-inline-flex as-items-center as-justify-center as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-[#1e4e79] as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary as-product-card-button"
          >
            {("ADD" || customerPortalSettingEntity?.addProductButtonTextV2)}
          </button>
      </div>
    </div>
    </>
  )
}

function SubscriptionItem({prd, subscriptionEntities, contractId, customerPortalSettingEntity, getCustomerPortalEntity}) {
  const [prdData, setPrdData] = useState(null)
  const [isSwap, setIsSwap] = useState(true)

  const [isDeleteProductModalOpen, setIsDeleteProductModalOpen] = useState(false);
  const [deleteInProgress, setDeleteInProgress] = useState(false);

  const [showDeleteProductMessaging, setShowDeleteProductMessaging] = useState(false);
  const [deleteProductSuccess, setDeleteProductSuccess] = useState(false);
  const [deleteProductError, setDeleteProductError] = useState(false);
  let [deleteDiscountFirst, setDeleteDiscountFirst] = useState(false);
  let [isSwapURLGenerationInProgress, setIsSwapUrlGenerationInProgress] = useState(false);

  const deleteProduct = async removeDiscount => {
    setDeleteInProgress(true);
    const requestUrl = `api/subscription-contracts-remove-line-item?contractId=${contractId}&lineId=${prd?.node?.id}&isExternal=true&removeDiscount=${removeDiscount != null ? removeDiscount : false}`;
    return Axios
      .put(requestUrl)
      .then(async res => {
        await getCustomerPortalEntity(contractId);
        setDeleteInProgress(false);
        return res;
      })
      .catch(err => {
        console.log('Error in deleting product');
        setDeleteInProgress(false);
        setIsDeleteProductModalOpen(false);
        setDeleteDiscountFirst(true);
        return err;
      });
  };

  const deleteProductHandler = async removeDiscount => {
    let results = await deleteProduct(removeDiscount);
    if (results) {
      setShowDeleteProductMessaging(true);
      if (results?.status === 200) {
        setDeleteProductSuccess(true);
        setDeleteProductError(false);
        setDeleteDiscountFirst(false);
      } else {
        setDeleteProductSuccess(false);
        setDeleteProductError(true);
      }
    }
  };

  const resetDeleteProductModal = () => {
    setIsDeleteProductModalOpen(false);
    setShowDeleteProductMessaging(false);
    setDeleteProductSuccess(false);
    setDeleteProductError(false);
    setDeleteDiscountFirst(false);
  };

  useEffect(() => {
    if (!window?.['products']?.[prd?.node?.productId?.split("/")?.pop()]) {
      getProductDetailById(prd?.node?.id)
    } else {
      setPrdData({...(window?.['products']?.[prd?.node?.productId?.split("/")?.pop()])});
    }
    // getProductDetailById(prd?.node?.id)
    if(prd?.node?.id && prd?.node?.customAttributes?.length) {
      prd?.node?.id && prd?.node?.customAttributes?.map(attributeLine => {
        if (attributeLine?.key === "_max-cycles" || isOneTimeProduct(prd)) {
          setIsSwap(false)
        }
      })
    } else {
      setIsSwap(true)
    }

  }, [prd])

  const getProductDetailById = () => {
    const client = connection('df5c6665c5edff602daa7a24499fc5e8');
    const productQuery = client.graphQLClient.query(root => {
        root.addConnection(
          'products',
          {
            args: {
              first: 8,
              query: `id:${prd?.node?.productId?.split("/").pop()}`
            }
          },
          product => {
            product.addField('metafield', { args: { namespace:"custom", key: 'artist_name' }  }, Metafield => {
              Metafield.add('id');
              Metafield.add('key');
              Metafield.add('namespace');
              Metafield.add('value');
            });
            product.add('title');
          }
        );
      });

      client.graphQLClient.send(productQuery).then(({ model, data, ...props }) => {
        if (!window['products']) {
          window['products'] = {}
        }
        const { products } = model;
        setPrdData(products["0"]);
        window['products'][prd?.node?.productId?.split("/").pop()] = products["0"];
      });
    }

    const connection = storeFrontAccesKey => {
      const domain = 'bandbox-rocks.myshopify.com';
      return Client.buildClient({
          domain: domain,
          storefrontAccessToken: storeFrontAccesKey
      });
    };

    const getSwapUrl = () => {
      let requestUrl = `api/subscription-contracts/contract-external/${contractId}?isExternal=true`;
      let isSwapProducturl = '';
      setIsSwapUrlGenerationInProgress(true)
      Axios.get(requestUrl)
      .then(res => {
        res?.data?.lines?.edges.forEach(prd => {
          if(prd?.node?.id) {
            if (!isOneTimeProduct(prd) && !isFreeProduct(prd)) {
              isSwapProducturl = `https://bandboxrocks.com/collections/exclusivecatalog?action=swap&lineId=${encodeURIComponent(prd?.node?.id)}&contractId=${contractId}`
            }
          }
        });

        if (isSwapProducturl) {
          window.location.href = isSwapProducturl;
        } else {
          setIsSwapUrlGenerationInProgress(false)
        }
      }).catch(err => {
        setIsSwapUrlGenerationInProgress(false)
      })
    };


  return (
    <>
      <div className="as-grid md:as-grid-cols-12 as-gap-4  as-bg-[#f3f3f5]" style={isSwap ? {order: "-1"} : {}}>
        <div className='as-flex as-items-center as-product-img-holder as-col-span-7 md:as-col-span-5 as-px-6'>
        <img
          src={
            prd?.node?.productId == null
              ? 'https://ik.imagekit.io/mdclzmx6brh/Appstle-Logo-1200-X-1200-with-SM1_d-vRjqUqmfN.jpg?ik-sdk-version=javascript-1.4.3&updatedAt=1638225565660'
              : prd?.node?.variantImage?.transformedSrc
          }
          alt=""
          className="as-product-image"
        />
        </div>
        <div className='as-p-6 as-col-span-7 as-flex as-justify-center as-flex-col as-items-center lg:as-items-stretch'>

          <h4 className='as-text-xl as-text-[#1E4E79] as-uppercase as-text-left'>
            {isSwap ? `YOUR ${new Date(subscriptionEntities?.nextBillingDate).toLocaleString('default', { month: 'long' })} Bandbox` : `${new Date(subscriptionEntities?.nextBillingDate).toLocaleString('default', { month: 'long' })} ONE TIME PURCHASE`}
          </h4>
        {prdData?.title && (
          <>
          <h3
            className={`as-text-xl as-font-bold as-text-gray-900 as-mt-6 as-product-artist as-text-left`}>
            {prdData?.metafield?.value}
          </h3>
          <h1
            className='as-text as-mt-3 as-product-title'>
            {prdData?.title}
          </h1>
          {/* <p className='as-text-xs as-mt-3'>God loves Ugly (BANDBOX EXCLUSIVES RED/BLACK HAND POUR - Ships November)</p> */}
          </>
        )}
        {subscriptionEntities?.status == "ACTIVE" && <>
        {isSwap ? <button
          onClick={getSwapUrl}
          class="lg:as-self-start as-mt-10 as-rounded-full font-bold as-inline-flex as-items-center as-justify-center as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-[#1e4e79] as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary"
          style={isSwapURLGenerationInProgress ? {width: "164px", height: "38px"} : {}}
        >
          {isSwapURLGenerationInProgress ? <Loader size="5" /> : "SWAP THIS ALBUM"}
        </button> : (<button onClick={() => setIsDeleteProductModalOpen(true)} className="lg:as-self-start as-mt-10 as-rounded-full font-bold as-inline-flex as-items-center as-justify-center as-border as-border-transparent as-shadow-sm as-px-4 as-py-2 as-bg-[#1e4e79] as-text-base as-font-medium as-text-white hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 sm:as-w-auto sm:as-text-sm disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-600 disabled:as-text-white as-button as-button--primary">
            REMOVE THIS ALBUM
          </button>)}
        </>}

        </div>
      </div>
      <TailwindModal
        open={isDeleteProductModalOpen}
        setOpen={resetDeleteProductModal}
        actionMethod={() => deleteProductHandler(customerPortalSettingEntity?.removeDiscountCodeAutomatically)}
        actionButtonText={!showDeleteProductMessaging ? customerPortalSettingEntity?.confirmCommonText || 'Confirm' : ''}
        updatingFlag={deleteInProgress}
        modalTitle={customerPortalSettingEntity?.deleteProductTitleText || 'Delete Product'}
        className="as-model-delete-product-confirmation"
        success={deleteProductSuccess}
      >
        {showDeleteProductMessaging &&
          <PopupMessaging showSuccess={deleteProductSuccess} showError={deleteProductError}/>}
        {!showDeleteProductMessaging && (
          <div className="as-text-sm as-text-gray-500">
            <div>
              <p
                className="as-text-gray-500 as-text-sm as-confirmation-message">{customerPortalSettingEntity?.deleteConfirmationMsgTextV2 || 'Are you sure?'}</p>
            </div>
          </div>
        )}
      </TailwindModal>
    </>

  )
}

const mapStateToProps = storeState => ({
  productOptions: storeState.prdVariant.prdVariantOptions,
  productsData: storeState.product.productOptions,
  subscriptionEntity: storeState.subscription.entity,
  sellingPlanData: storeState.subscriptionGroup.sellingPlanData,
  currentCycle: storeState.subscriptionGroup.currentCycle,
  currentCycleLoaded: storeState.subscriptionGroup.currentCycleLoaded,
  customerPortalSettingLoaded: storeState.customerPortalSettings.loading,
  isProductsListLoading: storeState.product.loading
});

const mapDispatchToProps = {
  getProductOptions,
  getPrdVariantOptions,
  getProducts
};

export default connect(mapStateToProps, mapDispatchToProps)(BandBox);

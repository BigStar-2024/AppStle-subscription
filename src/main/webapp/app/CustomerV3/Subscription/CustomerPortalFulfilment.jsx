import React, { useEffect, useState } from 'react';
import { connect, useSelector } from 'react-redux';
import { formatPrice } from 'app/shared/util/customer-utils';
import 'react-datepicker/dist/react-datepicker.css';
import { customerPortalupdateEntity as updateEntity } from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { deleteOneOffProducts, getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { DateTime } from 'luxon';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import { DayPicker } from 'react-day-picker';
import { toast } from 'react-toastify';
import Axios from 'axios';
import { isOneTimeProduct } from 'app/shared/util/subscription-utils';
import moment from 'moment';
import DeliveryScheduledDate from './DeliveryScheduledDate';
import RescheduleOrderDateModal from './RescheduleOrderDateModal';


const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};
const CustomerPortalFulfilment = ({
  updateEntity,
  deleteOneOffProducts,
  getCustomerPortalEntity,
  oneoffDeleteInProgress,
  indexKey,
  subscriptionContractFreezeStatus,
  checkIfPreventCancellationBeforeDays,
  activeOneTimeProducts,
  setNextOrderDateChangedResponce,
  ...props
}) => {
  const customerPortalSettingEntity = useSelector(state => state.customerPortalSettings.entity);
  let [handleAlert, setHandleAlert] = useState({ show: false, data: null });
  let [isOpen, setIsOpen] = useState(false);
  // custom variable
  let frequencyIntervalTranslate = {
    week: customerPortalSettingEntity?.weekText,
    day: customerPortalSettingEntity?.dayText,
    month: customerPortalSettingEntity?.monthText,
    year: customerPortalSettingEntity?.yearText
  };
  const getFrequencyTitle = interval => {
    return frequencyIntervalTranslate[interval.toLowerCase(interval)] ? frequencyIntervalTranslate[interval.toLowerCase()] : interval;
  };

  let [updateInProgress, setUpdateInProgress] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null);
  const [skipOrderError, setSkipOrderError] = useState(null);
  const [skipShipmentInProgress, setSkipShipmentInProgress] = useState(false);
  const [nextOrderDataModal, setNextOrderDataModal] = useState(false);
  const [showNextOrderDateMessaging, setShowNextOrderDateMessaging] = useState(false);
  const [nextOrderDateSuccess, setNextOrderDateSuccess] = useState(false);
  const [nextOrderDateError, setNextOrderDateError] = useState(false);
  const [locale, setLocale] = useState(null);
  const [showMessaging, setShowMessaging] = useState(false);
  const [success, setSuccess] = useState(false);
  const [billingAttemtId, setBillingAttemtId]= useState(null)
  const [selectedDateForNextOrder, setSelectedDateForNextOrder] = useState(false);
  const [showNextOrderDateError, setShowNextOrderDateError] = useState(false);
  const [nextOrderData, setNextOrderData] = useState(null)
  const [hideUpdateButton, setHideUpdateButton] = useState(false)

  const [rescheduleFirstUpcomingOrder, setRescheduleFirstUpcomingOrder] = useState(false);
  
  const [error, setError] = useState(false);
  useEffect(() => {
    const importLocaleFile = async () => {
      const localeFormShop = window?.navigator?.userLanguage || window?.navigator?.language || window?.Shopify?.locale || 'en-US';
      const localeToSet = await import(`date-fns/locale/${localeFormShop}/index.js`);
      setLocale(localeToSet.default);
    };
    importLocaleFile();
  }, []);
  async function updateDate() {
    setUpdateInProgress(true);
    props.ordData.billingDate = selectedDateForNextOrder.toISOString();
    await updateEntity(props.ordData);
    setHideUpdateButton(true)
    setUpdateInProgress(false);
    setEditMode(!editMode);
  }

  

  // const deleteOneTimeVariants = async (variantId, contractId, billingID) => {
  //   await deleteOneOffProducts(variantId, contractId, billingID, window?.appstle_api_key);
  // }

  const skipShipment = async () => {
    setSkipOrderError('');
    setSkipShipmentInProgress(true);
    let results = await props.skipShipment(props?.ordData?.id, props?.isFulfillment);
    if (results?.response?.data?.message) {
      setSkipOrderError(results?.response?.data?.message);
    }
    setSkipShipmentInProgress(false);
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

  const nextOrderDateupdate = async () => {
    setUpdateInProgress(true);
    const requestUrl = `api/subscription-billing-attempts`;
    console.log('nextOrderData', {...nextOrderData,billingDate: selectedDateForNextOrder.toISOString() });
    return Axios.put(requestUrl, {...nextOrderData,billingDate: selectedDateForNextOrder.toISOString() })
      .then(res => {
        console.log(res,'res')

        getCustomerPortalEntity(res.data.contractId).then(response => {
          updateDate()
          setNextOrderDateSuccess(true);
          setNextOrderDateError(false);
          setShowNextOrderDateMessaging(true);
          setUpdateInProgress(false);
          setHideUpdateButton(true);
        });
        // setUpdateInProgress(false);
        toast.success('Next order date updated', options);
        setEditMode(!editMode);
        return res;
      })
      .catch(err => {
        setUpdateInProgress(false);
        setShowNextOrderDateError(err.response.data.message)
        toast.error(err.response.data.message, options);
        return res;
      });
  };
  const nextOrderDateReschedule = async () => {
    setUpdateInProgress(true);
    const requestUrl = `api/subscription-contract-details/subscription-fulfillment/reschedule?fulfillmentId=${props?.ordData?.id}&deliveryDate=${selectedDateForNextOrder.toISOString()}`;
    // console.log('nextOrderData', {...nextOrderData,billingDate: selectedDateForNextOrder.toISOString() });
    return Axios.put(requestUrl)
      .then(res => {
        console.log(res,'res')

          getCustomerPortalEntity(props?.contractId).then(response => {
          updateDate()
          setNextOrderDateSuccess(true);
          setNextOrderDateError(false);
          setShowNextOrderDateMessaging(true);
          setUpdateInProgress(false);
          setHideUpdateButton(true);
        });
        // setUpdateInProgress(false);
        toast.success('Next order date updated', options);
        setEditMode(!editMode);
        return res;
      })
      .catch(err => {
        setUpdateInProgress(false);
        setShowNextOrderDateError(err.response.data.message)
        toast.error(err.response.data.message, options);
        return res;
      });
  };
  const nextOrderDateSubscriptionHandler = async () => {
    let results = props?.isFulfillment ? await nextOrderDateReschedule() : await nextOrderDateupdate();
    if (results) {
      if (results?.status !== 200) {
        setNextOrderDateSuccess(false);
        setNextOrderDateError(true);
      }
    }
  };
  const onDateChangeHandler = date => {
    setSelectedDateForNextOrder(date);
  };
const resetNextOrderDateModal = () => {
  setNextOrderDataModal(false);
   setNextOrderDateSuccess(false);
   setNextOrderDateError(false);
   setHideUpdateButton(false)
   setLocale(null);
   setShowMessaging(false);
   setShowNextOrderDateMessaging(false);
   setShowNextOrderDateError(false);

}
  const resetModal = () => {
    setIsOpen(false);
    setShowMessaging(false);
    setSuccess(false);
    setError(false);
  };

  let customDisplayTextShop = ["funkyfood.com.au"];
  return (
    <>
      <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-my-8">
        <div className="as-px-4 as-py-5 sm:as-px-6 as-flex as-justify-between as-flex-col md:as-flex-row">
          <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-flex as-items-center as-justify-between md:as-justify-start">
          <span className="as-billing-date">
              {props.isFulfillment ? (
                <>
                  {(customDisplayTextShop.includes(Shopify?.shop) && window?.top?.customDisplayTextEnabled) ? (
                    <DeliveryScheduledDate billingDate={props.ordData?.fulfillAt} dateFormat={customerPortalSettingEntity?.dateFormat} displayText={'Delivery scheduled for {{fromDate}} - {{toDate}}, payment due {{billingDate}}.'} />
                  ) : (
                    <> {DateTime.fromISO(props.ordData?.fulfillAt).toFormat(customerPortalSettingEntity?.dateFormat)}</>
                  )}
                </>
              ) : (
                <>
                  {(customDisplayTextShop.includes(Shopify?.shop) && window?.top?.customDisplayTextEnabled) ? (
                    <DeliveryScheduledDate billingDate={props.ordData?.billingDate} dateFormat={customerPortalSettingEntity?.dateFormat} displayText={'Delivery scheduled for {{fromDate}} - {{toDate}}, payment due {{billingDate}}.'}  />
                  ) : (
                    <>{DateTime.fromISO(props.ordData?.billingDate).toFormat(customerPortalSettingEntity?.dateFormat)}</>
                  )}
                </>
              )}
            </span>
            {props.ordData.status === 'QUEUED' || props.ordData.status === 'SCHEDULED' ? (
              <span className="as-bg-yellow-100 as-text-yellow-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-status">
                {customerPortalSettingEntity?.queueBadgeTextV2}
              </span>
            ) : props.ordData.status === 'OPEN' ? (
              <span className="as-bg-indigo-100 as-text-indigo-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-status">
                {customerPortalSettingEntity?.openBadgeText}
              </span>
            ) : (
              <span className="as-bg-indigo-100 as-text-indigo-800 as-text-xs as-font-medium as-mx-2 as-px-2.5 as-py-0.5 as-rounded-full as-status">
                {props?.isFulfillment
                  ? props.ordData.status === 'CLOSED'
                    ? customerPortalSettingEntity?.closeBadgeText
                    : customerPortalSettingEntity?.fulfilledText || 'fulfilled'
                  : props.ordData.status === 'SKIPPED' && customerPortalSettingEntity?.skipBadgeTextV2}
              </span>
            )}
          </h3>

          {/* <p className="as-mt-1 as-max-w-2xl as-text-sm as-text-gray-900"><span className="as-font-medium">{customerPortalSettingEntity?.orderFrequencyTextV2}</span>: <span>{customerPortalSettingEntity?.everyLabelTextV2}</span> {props.subscriptionEntities?.billingPolicy.intervalCount} {' '} <span>{getFrequencyTitle(props.subscriptionEntities.billingPolicy.interval)}</span></p> */}
          <div className='as-flex as-ml-auto as-items-center as-justify-around md:as-justify-start as-mt-2 md:as-mt-0'>
          {props?.isFulfillment &&
            customerPortalSettingEntity?.enableSkipFulFillment &&
            (props.ordData.status == 'QUEUED' || props.ordData.status == 'SCHEDULED') && (
              <p className="as-text-sm as-text-blue-500 as-cursor-pointer as-skip-order" onClick={() => setIsOpen(true)}>
                {props?.isFulfillment
                  ? customerPortalSettingEntity?.skipUpcomingFulfillmentButtonText || 'Skip Fulfillment'
                  : customerPortalSettingEntity?.skipOrderButtonText || 'Skip Order'}
              </p>
            )}
            {!checkIfPreventCancellationBeforeDays(customerPortalSettingEntity?.preventCancellationBeforeDays, props.ordData?.billingDate) &&
            customerPortalSettingEntity?.showShipment &&
            (props.ordData.status == 'QUEUED' || props.ordData.status == 'SCHEDULED') &&
            !props?.isFulfillment && (
              <p className="as-text-sm as-text-blue-500 as-cursor-pointer as-skip-order" onClick={() => setIsOpen(true)}>
                {props?.isFulfillment
                  ? customerPortalSettingEntity?.skipUpcomingFulfillmentButtonText || 'Skip Fulfillment'
                  : customerPortalSettingEntity?.skipOrderButtonText || 'Skip Order'}
              </p>
            )}
            {(indexKey !== 0 && customerPortalSettingEntity?.changeNextOrderDate && props.ordData.status !== 'SKIPPED' && props.ordData.status !== 'CLOSED') ? 
              <div className='as-ml-4'>
                <p 
                  onClick={() => {setNextOrderData(props.ordData),setBillingAttemtId(props?.ordData?.id), setNextOrderDataModal(true)}}
                  class="as-text-sm as-text-blue-500 as-cursor-pointer as-upcoming-reschedule">
                  {customerPortalSettingEntity?.rescheduleText}
                </p>
            </div>: null}
            {(indexKey === 0 && customerPortalSettingEntity?.changeNextOrderDate && props.ordData.status !== 'SKIPPED' && props.ordData.status !== 'CLOSED') ? 
              <div className='as-ml-4'>
                <p 
                  onClick={() => setRescheduleFirstUpcomingOrder(true)}
                  class="as-text-sm as-text-blue-500 as-cursor-pointer as-upcoming-reschedule">
                  {customerPortalSettingEntity?.rescheduleText}
                </p>
            </div>: null}
          </div>
        </div>
        <div className="as-border-t as-border-b as-border-gray-200 as-px-4 as-py-5 sm:as-px-6">
          <div className="as-grid as-gap-4 lg:as-grid-cols-2">
            {props?.ordData?.lineItems?.edges.map(
                  contractItem =>
                    contractItem.node.productId !== null && (!isOneTimeProduct(contractItem) || (props?.isUpcomingNextOrder && isOneTimeProduct(contractItem))) && (
                      <>
                        <div className="as-grid as-grid-cols-4 as-gap-4 as-items-center">
                          <div className="as-col-span-1">
                            <img src={contractItem.node?.image?.url} alt="" className="as-w-full" />
                          </div>
                          <div className="as-col-span-3">
                            {contractItem?.node?.productTitle && (
                              <p
                                className={`as-text-sm as-text-gray-800 as-mb-2 as-product-title`}
                              >
                                {contractItem?.node?.productTitle}
                              </p>
                            )}
                            {props.isFulfillment ? (
                              <p className="as-text-xs as-text-gray-500 as-next-order-date as-flex">
                                <span className="as-font-medium as-text-gray-600 as-label">
                                  {customerPortalSettingEntity?.nextDeliveryDate}<span className='colon-symbol'>:</span> 
                                </span>
                                &nbsp;{' '}
                                <span className="as-value">
                                  {DateTime.fromISO(props.ordData?.fulfillAt).toFormat(customerPortalSettingEntity?.dateFormat)}
                                </span>
                              </p>
                            ) : (
                              <p className="as-text-xs as-text-gray-500 as-next-order-date as-flex">
                                <span className="as-font-medium as-text-gray-600 as-label">
                                  {customerPortalSettingEntity?.nextOrderDateLbl}<span className='colon-symbol'>:</span> 
                                </span>
                                &nbsp;
                                <span className="as-value">
                                  {DateTime.fromISO(props.ordData?.billingDate).toFormat(customerPortalSettingEntity?.dateFormat)}
                                </span>
                               

                              </p>
                            )}
                            {contractItem?.node?.variantTitle &&
                              contractItem?.node?.variantTitle !== '-' &&
                              contractItem?.node?.variantTitle !== 'Default Title' && (
                                <p className="as-text-xs as-text-gray-500 as-variant">
                                  <span className="as-font-medium as-text-gray-600 as-label">
                                    {customerPortalSettingEntity?.variantLbl || 'Variant'}<span className='colon-symbol'>:</span>{' '}
                                  </span>{' '}
                                  <span className="as-value">{contractItem?.node?.variantTitle}</span>
                                </p>
                              )}
                            {contractItem?.node?.quantity && (
                              <p className="as-text-xs as-text-gray-500 as-quantity">
                               <span className="as-font-medium as-text-gray-600 as-label">
                                  {' '}
                                  {customerPortalSettingEntity?.quantityLbl || 'Quantity'}<span className='colon-symbol'>:</span>
                                </span>{' '}
                                <span className="as-value">{contractItem?.node?.quantity}</span>
                              </p>
                            )}
                            {isOneTimeProduct(contractItem) && <p className="as-text-xs as-text-gray-500">
                            {customerPortalSettingEntity?.oneTimePurchaseOnlyText || 'Added as one time purchase only'}
                            </p>}
                            {/* {contractItem?.node?.currentPrice?.amount && <p className="as-text-xs as-text-gray-500"><span className='as-font-medium as-text-gray-600'>Price:</span> {parseFloat(contractItem?.node?.currentPrice?.amount).toFixed(2)} {contractItem?.node?.currentPrice?.currencyCode}</p>} */}
                          </div>
                        </div>
                      </>
                    )
                )}
          </div>
        </div>
      </div>
      <TailwindModal
          open={nextOrderDataModal}
          setOpen={resetNextOrderDateModal}
          actionMethod={nextOrderDateSubscriptionHandler}
          updatingFlag={updateInProgress}
          actionButtonText={!hideUpdateButton ? (customerPortalSettingEntity?.updateChangeOrderBtnTextV2 || 'Update') : null}
          modalTitle={customerPortalSettingEntity?.rescheduleText || 'Reschedule'}
          className="as-model-reschedule"
          success={nextOrderDateSuccess}
        >
          {showNextOrderDateMessaging && <PopupMessaging showSuccess={nextOrderDateSuccess} showError={nextOrderDateError} errorMessage={showNextOrderDateError} />}

          {!showNextOrderDateMessaging && (
            <div className="as-text-sm as-flex as-justify-center as-items-center">
              <DayPicker
                locale={locale}
                mode="single"
                selected={selectedDateForNextOrder}
                onSelect={date => onDateChangeHandler(date)}
                fromDate={moment().add(1, 'days').toDate()}
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
      <TailwindModal
        open={isOpen}
        setOpen={resetModal}
        actionMethod={() => skipShipment(props?.ordData?.id, props?.isFulfillment)}
        actionButtonText={
          !showMessaging
            ? props?.isFulfillment
              ? customerPortalSettingEntity?.confirmSkipFulfillmentBtnText || 'Confirm Skip Fulfillment'
              : customerPortalSettingEntity?.confirmSkipOrder || 'Confirm Skip Order'
            : ''
        }
        updatingFlag={skipShipmentInProgress}
        modalTitle={
          props?.isFulfillment
            ? customerPortalSettingEntity?.skipFulfillmentButtonText || 'Skip Fulfillment'
            : customerPortalSettingEntity?.skipOrderButtonText || 'Skip Order'
        }
        className="as-model-skip-order"
        success={success}
      >
        {showMessaging && <PopupMessaging showSuccess={success} showError={error} errorMessage={skipOrderError} />}
        {!showMessaging && (
          <>
            {(!subscriptionContractFreezeStatus && !checkIfPreventCancellationBeforeDays(
              customerPortalSettingEntity?.preventCancellationBeforeDays,
              props.ordData?.billingDate
            )) && <div className="as-text-sm as-text-gray-500">
            <div>
              <p className="as-text-gray-500 as-text-sm"> {customerPortalSettingEntity?.frequencyChangeWarningTitle || 'Are you sure?'}</p>
            </div>
          </div>}
          {subscriptionContractFreezeStatus ? (
            <p className="as-text-gray-500 as-text-sm">
              {customerPortalSettingEntity?.subscriptionContractFreezeMessageV2 ||
                'Your subscription contract is frozen by your shop owner.'}
            </p>
          ) : (
            checkIfPreventCancellationBeforeDays(
              customerPortalSettingEntity?.preventCancellationBeforeDays,
              props.ordData?.billingDate
            ) && (
              <p className="as-text-gray-500 as-text-sm">
                {customerPortalSettingEntity?.preventCancellationBeforeDaysMessage?.replaceAll(
                  '{{preventDays}}',
                  `${customerPortalSettingEntity?.preventCancellationBeforeDays}`
                ) ||
                  `You can not pause/cancel/skip the subscription before the ${customerPortalSettingEntity?.preventCancellationBeforeDays} days from your next order date.`}
              </p>
            )
          )}
          </>
        )}
      </TailwindModal>
      <RescheduleOrderDateModal
        subscriptionEntity={props.subscriptionEntities}
        isOpen={rescheduleFirstUpcomingOrder}
        setIsOpen={setRescheduleFirstUpcomingOrder}
      />
    </>
  );
};

const mapStateToProps = state => ({
  // updatingBillingAttempt: state.subscriptionBillingAttempt.updatingBillingAttempt
  oneoffDeleteInProgress: state.subscription.oneoffDeleteInProgress
});

const mapDispatchToProps = {
  updateEntity,
  deleteOneOffProducts,
  getCustomerPortalEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerPortalFulfilment);

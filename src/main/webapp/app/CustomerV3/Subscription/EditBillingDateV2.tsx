import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import 'react-datepicker/dist/react-datepicker.css';
import { toast } from 'react-toastify';
import { DateTime } from 'luxon';
import { updateAttemptBillingEntity } from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';

import 'react-day-picker/dist/style.css';
import Loader from './Loader';
import DeliveryScheduledDate from './DeliveryScheduledDate';
import OrderNowModal from './OrderNowModal';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { checkIfPreventCancellationBeforeDays } from 'app/shared/util/customer-utils';
import { BillingAttemptStatus } from 'app/shared/model/enumerations/billing-attempt-status.model';
import SkipOrderModal from './SkipOrderModal';
import RescheduleOrderDateModal from './RescheduleOrderDateModal';
import { ISubscription } from 'app/shared/model/subscription.model';

interface EditBillingDateProps {
  subscriptionEntity?: ISubscription & {[key: string]: any}
}

const EditBillingDate = (props: EditBillingDateProps) => {

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)
  const shopName = useSelector((state: IRootState) => state.shopInfo.entity.shop);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const subFailedOrderEntities = useSelector((state: IRootState) => state.subscriptionBillingAttempt.failedBillingAttempt.slice());
  const dispatch = useDispatch();

  const [isOrderNowModalOpen, setIsOrderNowModalOpen] = useState(false);
  const [isSkipOrderModalOpen, setIsSkipOrderModalOpen] = useState(false);
  const [isRescheduleModalOpen, setIsRescheduleModalOpen] = useState(false);
  const [loaderLoading, setLoaderLoading] = useState(false);

  const shouldDoCO2YouCustomizations = shopName?.includes('co2-you.myshopify.com');
  const shouldDoFunkyFoodCustomizations = shopName.includes('funkyfood2.myshopify.com') && (window?.top as any)?.customDisplayTextEnabled;

  function getFromDate(lastOrderDate, daysToAdd) {
    let orderDate = new Date(lastOrderDate);
    let currentDayNumber = orderDate.getDay();
    let additionalDaysToAdd = daysToAdd;
    if (currentDayNumber >= 5) {
      additionalDaysToAdd = additionalDaysToAdd + (7 - currentDayNumber);
    }
    return new Date(orderDate.setDate(orderDate.getDate() + additionalDaysToAdd));
  };

  async function reAttemptBilling(id, contractId, shopName) {
    setLoaderLoading(true);
    if (id) {
      let result = await updateAttemptBillingEntity(id, shopName, contractId)(dispatch);
      if (result) {
        if (result.value.status === 200) {
          setLoaderLoading(false);
          toast.success('Order placed successfully', {
            autoClose: 500,
            position: toast.POSITION.BOTTOM_CENTER,
          });
        }
      }
    }
  };

  return (
    <>
      <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-space-y-4 as-card as-edit-billing">
        <div className="as-space-y-2 as-edit-billing_info">
          <div className="as-flex as-justify-between">
            <p className="as-text-sm as-text-gray-500 as-card_title as-edit-billing_title">{customerPortalSettingsEntity?.nextOrderText}</p>
            {!checkIfPreventCancellationBeforeDays(
              customerPortalSettingsEntity?.preventCancellationBeforeDays,
              subscriptionEntity?.nextBillingDate
            ) && 
              customerPortalSettingsEntity?.changeNextOrderDate && (
                <p
                  onClick={() => setIsRescheduleModalOpen(true)}
                  className="as-cursor-pointer as-text-sm as-text-blue-500 as-cta as-card_cta as-edit-billing_cta"
                >
                  {customerPortalSettingsEntity?.rescheduleText || 'Reschedule'}
                </p>
              )}
          </div>
          {!shouldDoCO2YouCustomizations && (
            <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-card_data as-edit-billing_data">
              {DateTime.fromISO(subscriptionEntity?.nextBillingDate).toFormat(customerPortalSettingsEntity?.dateFormat)}
            </h3>
          )}
          {shouldDoCO2YouCustomizations && (
            <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-card_data as-edit-billing_data">
              {DateTime.fromISO(getFromDate(subscriptionEntity?.nextBillingDate, 1).toISOString()).toFormat(
                customerPortalSettingsEntity?.dateFormat
              )}
            </h3>
          )}
        </div>
        {shouldDoFunkyFoodCustomizations && (
          <div className="as-space-y-2 as-edit-billing_info">
            <div className="as-flex as-justify-between">
              <p className="as-text-sm as-text-gray-500 as-card_title as-delivery-window-title">Delivery Window</p>
            </div>
            <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-card_data as-edit-delivery-window-data">
              <DeliveryScheduledDate
                billingDate={subscriptionEntity?.nextBillingDate}
                dateFormat={customerPortalSettingsEntity?.dateFormat}
                displayText={'{{fromDate}} - {{toDate}}'}
              />
            </h3>
          </div>
        )}
        <div className="as-space-y-2 as-edit-billing_buttons">
          {customerPortalSettingsEntity?.allowOrderNow && (
            <button
              type="button"
              disabled={!customerPortalSettingsEntity?.allowOrderNow || subFailedOrderEntities?.[0]?.status === 'PROGRESS'}
              onClick={() => setIsOrderNowModalOpen(true)}
              className="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary as-edit-billing_primary-button disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-900 as-order-now-btn"
            >
              {customerPortalSettingsEntity?.orderNowText || ' Order Now'}
            </button>
          )}
          {customerPortalSettingsEntity?.showShipment && (
            <button
              type="button"
              disabled={
                !customerPortalSettingsEntity?.showShipment ||
                checkIfPreventCancellationBeforeDays(
                  customerPortalSettingsEntity?.preventCancellationBeforeDays,
                  subscriptionEntity?.nextBillingDate
                )
              }
              onClick={() => setIsSkipOrderModalOpen(true)}
              className="as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-indigo-600  hover:as-bg-indigo-600 hover:as-text-white as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-indigo-600 as-bg-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--secondary as-edit-billing_secondary-button disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-900 as-skip-order-btn"
            >
              {customerPortalSettingsEntity?.skipOrderButtonText}
            </button>
          )}
        </div>
        {subFailedOrderEntities?.[0]?.status === BillingAttemptStatus.FAILURE && (
          <div>
            <p className="as-text-sm as-text-gray-500 as-card_title as-edit-billing_title">
              {customerPortalSettingsEntity.pastOrderFailedTextV2 || 'Your past order is failed on'}{' '}
              {DateTime.fromISO(subFailedOrderEntities?.[0]?.billingDate).toFormat(customerPortalSettingsEntity?.dateFormat)}{' '}
            </p>

            <button
              type="button"
              disabled={!customerPortalSettingsEntity?.allowOrderNow}
              onClick={() =>
                reAttemptBilling(
                  subFailedOrderEntities?.[0]?.id,
                  subFailedOrderEntities?.[0]?.contractId,
                  subFailedOrderEntities?.[0]?.shop
                )
              }
              className="as-w-full as-mt-4 as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-border as-border-transparent as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500 as-button as-button--primary as-edit-billing_primary-button disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-indigo-900 as-order-now-btn"
            >
              {loaderLoading ? <Loader size={6} /> : customerPortalSettingsEntity?.pastOrderTryAgainButtonTextV2 || ' Try Again'}
            </button>
          </div>
        )}
      </div>
      <OrderNowModal
        subscriptionEntity={subscriptionEntity}
        isOpen={isOrderNowModalOpen}
        setIsOpen={open => setIsOrderNowModalOpen(open)}
      />
      <SkipOrderModal
        subscriptionEntity={subscriptionEntity}
        isOpen={isSkipOrderModalOpen}
        setIsOpen={setIsSkipOrderModalOpen}
      />
      <RescheduleOrderDateModal
        subscriptionEntity={subscriptionEntity}
        isOpen={isRescheduleModalOpen}
        setIsOpen={setIsRescheduleModalOpen}
      />
    </>
  );
};
export default EditBillingDate;

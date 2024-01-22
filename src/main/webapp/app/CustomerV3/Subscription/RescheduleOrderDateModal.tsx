import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import moment from 'moment';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { DayPicker } from 'react-day-picker';
import PopupMessaging from './PopupMessaging';
import TailwindModal from './TailwindModal';
import { IRootState } from 'app/shared/reducers';
import Axios from 'axios';
import swal from 'sweetalert';

interface RescheduleOrderDateModalProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const RescheduleOrderDateModal = (props: RescheduleOrderDateModalProps) => {
  const { isOpen, setIsOpen } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)
  const shopName = useSelector((state: IRootState) => state.shopInfo.entity.shop);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const lastBillingDate = useSelector((state: IRootState) => state.subscriptionBillingAttempt.failedBillingAttempt?.[0]?.billingDate);
  const dispatch = useDispatch();

  const [locale, setLocale] = useState(null)
  const [isUpdating, setIsUpdating] = useState(false);
  const [selectedDate, setSelectedDate] = useState<Date>(null);
  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message?: string }>(null);

  const contractId = subscriptionEntity?.id?.split?.("/")?.pop?.()

  useEffect(() => {
    setSelectedDate(moment(subscriptionEntity?.nextBillingDate).toDate());
  }, [subscriptionEntity]);

  useEffect(() => {
    const importLocaleFile = async () => {
      const localeFormShop = (window?.navigator as any)?.userLanguage || window?.navigator?.language || window?.Shopify?.locale || 'en-US';
      const localeToSet = await import(`date-fns/locale/${localeFormShop}/index.js`);
      setLocale(localeToSet.default);
    };
    importLocaleFile();
  }, []);

  const shouldDoCO2YouCustomizations = shopName?.includes('co2-you.myshopify.com');

  const { fromDate, toDate } = ((): { fromDate: Date; toDate: Date } => {
    if (shouldDoCO2YouCustomizations) {
      const lastOrderDate = lastBillingDate ? new Date(lastBillingDate) : new Date();
      const fromDate = getFromDate(lastOrderDate, 2);
      const toDate = new Date(fromDate);
      toDate.setMonth(fromDate.getMonth() + 4);
      //const toDate = new Date(fromDateInital.setMonth(fromDateInital.getMonth() + 4));

      return { fromDate, toDate };
    }

    return {
      fromDate: moment()
        .add(1, 'days')
        .toDate(),
      toDate: null,
    };
  })();

  function getIsDateDisabled(date: Date): boolean {
    if (shouldDoCO2YouCustomizations) {
      if (date.getDay() > 5) {
        // Saturday
        return true;
      } else if (date.getDay() == 0) {
        // Sunday
        return true;
      } else if (date.getDate() == 25 && date.getMonth() == 11) {
        // Christmas
        return true;
      } else if (date.getDate() == 1 && date.getMonth() == 0) {
        // New Year's Day
        return true;
      }
    }

    if (subscriptionEntity?.billingPolicy?.anchors?.length) {
      let anchor = subscriptionEntity?.billingPolicy?.anchors[0];
      if (anchor?.type === 'MONTHDAY') {
        return date.getDate() != anchor.day;
      } else if (anchor?.type === 'WEEKDAY') {
        let day = date.getDay() === 0 ? 7 : date.getDay();
        return day != anchor.day;
      }
    } else if (customerPortalSettingsEntity?.datePickerEnabledDaysV2) {
      let enabledDays = customerPortalSettingsEntity?.datePickerEnabledDaysV2.split(',').map((day: string) => parseInt(day));
      return enabledDays.indexOf(date.getDay()) === -1;
    }
  }

  async function rescheduleSubscriptionHandler() {
    setIsUpdating(true);
    const updateAction = shouldDoCO2YouCustomizations ? updateNextOrderDate : updateDate;
    updateAction().then(results => {
      if (results?.status === 200) {
        setResponseInfo({ success: true });
      } else {
        setResponseInfo({ success: false });
      }
    }).catch(err => setResponseInfo({ success: false, message: err.toString() }));
  }

  const updateNextOrderDate = async () => {
    const nextOrder = getNextOrderDate(selectedDate);
    const requestUrl = `api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${nextOrder.toISOString()}&isExternal=true`;
    return checkAnchorDay(requestUrl, nextOrder);
  };

  const updateDate = async () => {
    const prevDate = moment(subscriptionEntity?.nextBillingDate).toDate();
    const newDate = new Date(selectedDate);
    newDate.setHours(prevDate.getHours());
    newDate.setMinutes(prevDate.getMinutes());
    newDate.setSeconds(prevDate.getSeconds());

    const requestUrl = `api/subscription-contracts-update-billing-date?contractId=${contractId}&nextBillingDate=${newDate.toISOString()}&isExternal=true`;
    return checkAnchorDay(requestUrl, newDate);
  };

  const getNextOrderDate = (date: Date) => {
    const daysToSubtract = date.getDay() == 1 ? 3 : 1;
    return new Date(date.setDate(date.getDate() - daysToSubtract));
  };

  async function checkAnchorDay(requestUrl: string, nextBillingDate: Date) {
    return Axios.get(
      `api/subscription-contracts-is-overwrite-anchor-day?contractId=${contractId}&nextBillingDate=${nextBillingDate.toISOString()}`
    ).then(async res => {
      if (res.data) {
        return swal({
          title: 'Warning',
          text: 'This action will override anchor day',
          icon: 'warning',
          buttons: [
            customerPortalSettingsEntity?.cancelButtonTextV2 || 'Cancel',
            customerPortalSettingsEntity?.confirmCommonText || 'Confirm',
          ],
          dangerMode: true,
        }).then((result: any) => {
          if (result) {
            return updateNextOrder(requestUrl);
          }
        });
      } else {
        return updateNextOrder(requestUrl);
      }
    });
  }

  const updateNextOrder = async (requestUrl: string) => {
    return Axios.put(requestUrl)
      .then(res => {
        dispatch(getCustomerPortalEntity(contractId));
        return res;
      })
      .catch(err => err);
  };

  function getFromDate(lastOrderDate: Date, daysToAdd: number) {
    const orderDate = new Date(lastOrderDate);
    const currentDayNumber = orderDate.getDay();
    const additionalDaysToAdd = currentDayNumber >= 5 ? daysToAdd + (7 - currentDayNumber) : daysToAdd;
    return new Date(orderDate.setDate(orderDate.getDate() + additionalDaysToAdd));
  }

  function resetModal() {
    setResponseInfo(null);
  }

  return (
    //@ts-ignore
    <TailwindModal
      open={isOpen}
      setOpen={setIsOpen}
      actionMethod={rescheduleSubscriptionHandler}
      actionButtonText={!responseInfo ? customerPortalSettingsEntity?.updateChangeOrderBtnTextV2 : ''}
      updatingFlag={isUpdating}
      modalTitle={customerPortalSettingsEntity?.rescheduleText || 'Reschedule'}
      className="as-model-reschedule"
      afterClose={resetModal}
      success={responseInfo?.success}
    >
      {!!responseInfo && (
        <PopupMessaging
          showSuccess={responseInfo!.success}
          showError={!responseInfo!.success}
          successMessage={responseInfo?.message}
          errorMessage={responseInfo?.message}
        />
      )}

      {!responseInfo && (
        <>
          <div className="as-text-sm as-flex as-justify-center as-items-center">
            <DayPicker
              locale={locale} 
              mode="single"
              selected={selectedDate}
              onSelect={date => setSelectedDate(date)}
              fromDate={fromDate}
              toDate={toDate}
              disabled={getIsDateDisabled}
            />
          </div>
          {customerPortalSettingsEntity?.reschedulingPolicies && 
            <div className='as-text-sm as-text-gray-500 as-py-3 as-border-t as-rescheduling-policies-text' dangerouslySetInnerHTML={{__html: customerPortalSettingsEntity?.reschedulingPolicies}} />
          }
        </>
      )}
    </TailwindModal>
  );
};

export default RescheduleOrderDateModal;

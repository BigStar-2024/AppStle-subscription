import React, { useState, ChangeEvent, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import axios from 'axios';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import { ISubscription } from 'app/shared/model/subscription.model';
import { getIsPrepaid } from 'app/shared/util/subscription-utils';
import swal from 'sweetalert';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';

interface EditFrequencyModalProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => any;
}

const EditFrequencyModal = (props: EditFrequencyModalProps) => {
  const { isOpen, setIsOpen } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const shopName = useSelector((state: IRootState) => state.shopInfo.entity.shop);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const dispatch = useDispatch();

  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message?: string }>(null);
  const [isUpdating, setIsUpdating] = useState(false);
  const [selectedIntervalCount, setSelectedIntervalCount] = useState(subscriptionEntity?.billingPolicy?.intervalCount);
  const [selectedInterval, setSelectedInterval] = useState(subscriptionEntity?.billingPolicy?.interval);
  const [selectedSellingPlan, setSelectedSellingPlan] = useState(null);
  const [isCountValid, setIsCountValid] = useState(true);
  const [billingFrequencies, setBillingFrequencies] = useState([]);
  const [deliveryFrequencies, setDeliveryFrequencies] = useState([]);

  const isPrepaid = getIsPrepaid(subscriptionEntity);
  const contractId = subscriptionEntity?.id?.split?.('/')?.pop?.();

  const shouldDoCO2YouCustomizations = shopName?.includes('co2-you.myshopify.com');

  useEffect(() => {
    const productIds = subscriptionEntity?.lines?.edges?.map((product: any, _: any) => {
      return product?.node?.productId?.split?.('/')?.pop();
    });

    if (!productIds) return;

    const productIdsUrlParams = productIds.map((id: any) => `productIds=${id}`).join('&');

    axios.get(`/api/data/products-selling-plans?${productIdsUrlParams}`).then((data: any) => {
      const billingFrequencies: any[] = [];
      const deliveryFrequencies: any[] = [];

      data?.data?.forEach((option: any) => {
        option?.sellingPlanGroups?.edges.forEach((edge: any) => {
          edge?.node?.sellingPlans.edges.forEach((sellingPlanEdge: any) => {
            const planBillingFrequency = {
              name: `${sellingPlanEdge?.node?.name} ${
                customerPortalSettingsEntity?.showSellingPlanIntervalInEditFrequencyV2 === 'true'
                  ? `(${sellingPlanEdge?.node?.billingPolicy?.intervalCount} ${sellingPlanEdge?.node?.billingPolicy?.interval})`
                  : ''
              }`,
              value: sellingPlanEdge?.node?.id?.split('/').pop(),
              intervalCount: sellingPlanEdge?.node?.billingPolicy?.intervalCount,
              interval: sellingPlanEdge?.node?.billingPolicy?.interval,
            };
            if (!billingFrequencies.some(billingFreq => (billingFreq?.interval === planBillingFrequency.interval && billingFreq?.intervalCount === planBillingFrequency.intervalCount) )) {
              billingFrequencies.push(planBillingFrequency);
            }

            const planDeliveryFrequency = {
              name: `${sellingPlanEdge?.node?.name} ${
                customerPortalSettingsEntity?.showSellingPlanIntervalInEditFrequencyV2 === 'true'
                  ? `(${sellingPlanEdge?.node?.billingPolicy?.intervalCount} ${sellingPlanEdge?.node?.billingPolicy?.interval})`
                  : ''
              }`,
              value: sellingPlanEdge?.node?.id?.split('/').pop(),
              intervalCount: sellingPlanEdge?.node?.deliveryPolicy?.intervalCount,
              interval: sellingPlanEdge?.node?.deliveryPolicy?.interval,
            };
            if (!deliveryFrequencies.some(deliveryFreq => (deliveryFreq?.interval === planDeliveryFrequency.interval && deliveryFreq?.intervalCount === planDeliveryFrequency.intervalCount))) {
              deliveryFrequencies.push(planDeliveryFrequency);
            }
          });
        });
      });
      setBillingFrequencies(billingFrequencies);
      setDeliveryFrequencies(deliveryFrequencies);
    });
  }, [subscriptionEntity]);

  const frequencyOptions = [
    { key: 'DAY', title: customerPortalSettingsEntity?.dayText },
    { key: 'WEEK', title: customerPortalSettingsEntity?.weekText },
    { key: 'MONTH', title: customerPortalSettingsEntity?.monthText },
    { key: 'YEAR', title: customerPortalSettingsEntity?.yearText },
  ];

  const getCustomIntervals = () => {
    const range = (min: number, max: number) => [...Array(max - min + 1).keys()].map(i => i + min);

    let customIntervals = [];
    if (selectedInterval === 'DAY') {
      customIntervals = range(1, 100);
    } else if (selectedInterval === 'WEEK') {
      customIntervals = range(1, 16);
    } else if (selectedInterval === 'MONTH') {
      customIntervals = range(1, 4);
    }
    return customIntervals;
  };

  function handleIntervalCountChange(event: ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    const count = event.target.value;
    setSelectedIntervalCount(count);
    if (!count.trim() || isNaN(parseInt(count.trim())) || parseInt(count) < 1) {
      setIsCountValid(false);
    } else {
      setIsCountValid(true);
    }
  }

  function handleIntervalChange(event: ChangeEvent<HTMLSelectElement>) {
    const interval = event.target.value;

    setSelectedInterval(interval);
  }

  function handleSellingPlanChange(event: ChangeEvent<HTMLSelectElement>) {
    const sellingPlan = event.target.value;
    setSelectedSellingPlan(sellingPlan);
  }

  async function updateFrequency() {
    const confirmed = await confirmUpdate();
    if (!confirmed) return;

    setIsUpdating(true);

    const selectedPlanDetails = selectedSellingPlan
      ? (isPrepaid ? deliveryFrequencies : billingFrequencies).find(freq => {
          return freq?.value == selectedSellingPlan;
        })
      : null;

    const interval = selectedPlanDetails?.interval ?? selectedInterval;
    const intervalCount = selectedPlanDetails?.intervalCount ?? selectedIntervalCount;

    const overrideAnchorDay = await confirmOverrideAnchorDay(interval, intervalCount);
    if (!overrideAnchorDay) {
      setIsUpdating(false);
      return;
    }
    await updateBillingInterval()
      .then(result => {
        if (result?.status === 200) {
          setResponseInfo({ success: true });
        } else {
          setResponseInfo({ success: false });
        }
      })
      .catch(_err => {
        setResponseInfo({ success: false });
      });

    setIsUpdating(false);
  }

  async function confirmUpdate(): Promise<boolean> {
    return await swal({
      title: customerPortalSettingsEntity?.frequencyChangeWarningTitle || 'Are you sure?',
      text: customerPortalSettingsEntity?.frequencyChangeWarningDescription,
      icon: 'warning',
      buttons: [customerPortalSettingsEntity?.cancelButtonTextV2 || 'Cancel', customerPortalSettingsEntity?.confirmCommonText || 'Confirm'],
      dangerMode: true,
    }).then(value => {
      return !!value;
    });
  }

  async function confirmOverrideAnchorDay(interval: any, intervalCount: any): Promise<boolean> {
    return await axios
      .get(
        `api/subscription-contracts-is-overwrite-anchor-day?contractId=${contractId}&interval=${interval}&intervalCount=${intervalCount}`
      )
      .then(results => {
        if (!results?.data) {
          return true;
        }
        return swal({
          title: 'Warning',
          text: 'This action will override anchor day',
          icon: 'warning',
          buttons: [
            customerPortalSettingsEntity?.cancelButtonTextV2 || 'Cancel',
            customerPortalSettingsEntity?.confirmCommonText || 'Confirm',
          ],
          dangerMode: true,
        }).then(selectedOption => {
          return !!selectedOption;
        });
      });
  }

  async function updateBillingInterval() {
    let requestUrl = '';
    if (customerPortalSettingsEntity?.showSellingPlanFrequencies) {
      requestUrl = `api/subscription-contracts-update-frequency-by-selling-plan?contractId=${contractId}&sellingPlanId=${selectedSellingPlan}`;
    } else {
      requestUrl = `api/subscription-contracts-update-billing-interval?contractId=${contractId}&interval=${selectedInterval}&intervalCount=${selectedIntervalCount}&isExternal=true`;
    }

    return await axios
      .put(requestUrl)
      .then(res => {
        dispatch(getCustomerPortalEntity(contractId));
        return res;
      })
      .catch(err => {
        return err;
      });
  }

  function resetModal() {
    setIsOpen(false);
    setResponseInfo(null);
    setIsUpdating(false);
  }

  return (
    <>
      {/*
      //@ts-ignore suppress warning of missing props */}
      <TailwindModal
        open={isOpen}
        setOpen={setIsOpen}
        afterClose={resetModal}
        actionMethod={updateFrequency}
        actionButtonText={!responseInfo ? customerPortalSettingsEntity?.updateFreqBtnText : ''}
        actionButtonInValid={!isCountValid}
        updatingFlag={isUpdating}
        modalTitle={customerPortalSettingsEntity?.editDeliveryInternalText || 'Edit Delivery Interval'}
        className="as-model-edit-delivery"
        success={responseInfo?.success}
      >
        {!!responseInfo && (
          <PopupMessaging
            showSuccess={responseInfo.success}
            showError={!responseInfo.success}
            successMessage={responseInfo?.message}
            errorMessage={responseInfo?.message}
          />
        )}
        {!responseInfo && (
          <>
            <div className="as-text-sm as-text-gray-500">
              <div className="as-grid as-gap-4 as-grid-cols-12">
                {!shouldDoCO2YouCustomizations && (
                  <>
                    {!customerPortalSettingsEntity?.showSellingPlanFrequencies && (
                      <>
                        <div className="as-col-span-5">
                          <input
                            value={selectedIntervalCount}
                            type="number"
                            onChange={handleIntervalCountChange}
                            className={`
                              as-block as-w-full as-p-2.5 sm:as-text-sm as-border-gray-300 as-rounded-md
                              ${
                                !isCountValid
                                  ? 'as-border-red-500 as-border-2 focus:as-ring-red-500 focus:as-border-red-500'
                                  : 'focus:as-ring-indigo-500 focus:as-border-indigo-500'
                              }
                            `}
                          />
                        </div>
                        <div className="as-col-span-7">
                          <select
                            className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                            value={selectedInterval ?? ''}
                            onChange={handleIntervalChange}
                          >
                            {frequencyOptions.map(opt => {
                              return (
                                <option key={opt.key} value={opt?.key}>
                                  {opt?.title}
                                </option>
                              );
                            })}
                          </select>
                        </div>
                      </>
                    )}
                    {customerPortalSettingsEntity?.showSellingPlanFrequencies && (
                      <div className="as-col-span-12">
                        <select
                          className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                          value={selectedSellingPlan ?? ''}
                          onChange={handleSellingPlanChange}
                        >
                          <option value="">{customerPortalSettingsEntity?.pleaseSelectText || 'Please select'}</option>
                          {!isPrepaid
                            ? billingFrequencies?.map((opt, index) => {
                                return (
                                  <option key={index} value={opt?.value}>
                                    {opt?.name}
                                  </option>
                                );
                              })
                            : deliveryFrequencies?.map((opt, index) => {
                                return (
                                  <option key={index} value={opt?.value}>
                                    {opt?.name}
                                  </option>
                                );
                              })}
                        </select>
                      </div>
                    )}
                  </>
                )}
                {shouldDoCO2YouCustomizations && (
                  <>
                    <div className="as-col-span-5">
                      <select
                        className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                        value={selectedIntervalCount}
                        onChange={handleIntervalCountChange}
                      >
                        {getCustomIntervals()?.map(opt => {
                          return (
                            <option key={opt} value={opt}>
                              {opt}
                            </option>
                          );
                        })}
                      </select>
                    </div>
                    <div className="as-col-span-7">
                      <select
                        className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
                        value={selectedInterval}
                        onChange={handleIntervalChange}
                      >
                        {frequencyOptions
                          .filter(opt => opt.key !== 'YEAR')
                          .map(opt => {
                            return (
                              <option key={opt.key} value={opt?.key}>
                                {opt?.title}
                              </option>
                            );
                          })}
                      </select>
                    </div>
                  </>
                )}
              </div>
            </div>
          </>
        )}
      </TailwindModal>
    </>
  );
};

export default EditFrequencyModal;

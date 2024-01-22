import React, { useState } from 'react';
import { ISubscription } from 'app/shared/model/subscription.model';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import EditFrequencyModal from './EditFrequencyModal';

interface EditFrequencyProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
}

const EditFrequency = (props: EditFrequencyProps) => {

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );

  const [isModalOpen, setIsModalOpen] = useState(false);

  const frequencyTitle = (() => {
    const frequencyIntervalTranslate: { [key: string]: string } = {
      week: customerPortalSettingsEntity?.weekText,
      day: customerPortalSettingsEntity?.dayText,
      month: customerPortalSettingsEntity?.monthText,
      year: customerPortalSettingsEntity?.yearText,
      weeks: customerPortalSettingsEntity?.weeksTextV2,
      days: customerPortalSettingsEntity?.daysTextV2,
      months: customerPortalSettingsEntity?.monthsTextV2,
      years: customerPortalSettingsEntity?.yearsTextV2,
    };

    const interval: string = subscriptionEntity?.billingPolicy?.interval;
    const intervalCount = parseInt(subscriptionEntity?.billingPolicy?.intervalCount);
    const intervalText = intervalCount > 1 ? `${interval}s` : interval;
    return frequencyIntervalTranslate?.[intervalText.toLowerCase()] ?? intervalText;
  })();

  return (
    <>
      <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-frequency">
        <div className="as-flex as-justify-between as-mb-2">
          <p className="as-text-sm as-text-gray-500 as-card_title as-edit-frequency_title">
            {customerPortalSettingsEntity?.orderFrequencyTextV2}
          </p>

          {subscriptionEntity.status == 'ACTIVE' && customerPortalSettingsEntity?.changeOrderFrequency && (
            <p
              className="as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-card_cta as-edit-frequency_cta"
              onClick={() => setIsModalOpen(true)}
            >
              {customerPortalSettingsEntity?.editFrequencyBtnTextV2 || 'Edit'}
            </p>
          )}
        </div>

        <h3 className="as-text-lg as-leading-6 as-font-medium as-text-gray-900 as-pt-3 as-card_data as-edit-frequency_data">
          {customerPortalSettingsEntity?.everyLabelTextV2} {subscriptionEntity?.billingPolicy?.intervalCount} {frequencyTitle}
        </h3>
      </div>
      <EditFrequencyModal subscriptionEntity={subscriptionEntity} isOpen={isModalOpen} setIsOpen={setIsModalOpen} />
    </>
  );
};

export default EditFrequency;

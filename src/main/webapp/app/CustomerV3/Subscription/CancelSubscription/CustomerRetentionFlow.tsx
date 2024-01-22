import React, { useEffect, useState, useMemo, ChangeEvent } from 'react';
import { IRootState } from 'app/shared/reducers';
import { useSelector } from 'react-redux';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import CancellationReason, { CancellationReasonAction } from 'app/shared/model/cancellation-reason.model';
import { ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';

interface CustomerRetentionFlowProps {
  actionToHandle: CancellationReasonAction;
  subscriptionContractDetails: ISubscriptionContractDetails;
  onSetInfo?: (retentionInfo: { reason: CancellationReason; feedback: string }) => void;
}

const CustomerRetentionFlow = (props: CustomerRetentionFlowProps) => {
  const {
    onSetInfo = (_info: { reason: CancellationReason; feedback: string }) => {},
    actionToHandle,
    subscriptionContractDetails,
  } = props;

  const cancellationManagementEntity = useSelector((state: IRootState) => state.cancellationManagement.entity);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );

  const [selectedCancellationReason, setSelectedCancellationReason] = useState<CancellationReason>(null);
  const [cancellationFeedback, setCancellationFeedback] = useState<string>('');

  const cancellationReasons = useMemo<CancellationReason[]>(() => {
    try {
      return JSON.parse(cancellationManagementEntity.cancellationReasonsJSON) ?? [];
    } catch (error) {
      console.log(error);
    }
    return [];
  }, [cancellationManagementEntity]);

  useEffect(() => {
    onSetInfo({ reason: selectedCancellationReason, feedback: cancellationFeedback });
  }, [selectedCancellationReason, cancellationFeedback]);

  const promptText = (() => {
    const defaultDiscountMessage =
      "We don't want to see you go! We would like like to offer you a {{discountAmount}}% discount. Would you like to accept this discount instead of cancelling?";

    const discountMessageOnCancellation: string = (
      (customerPortalSettingsEntity?.discountMessageOnCancellation as string) || defaultDiscountMessage
    )
      .replace(/{{discountAmount}}/g, selectedCancellationReason?.cancellationDiscount?.toString())
      .replace(/{{cycleLimit}}/g, parseInt(customerPortalSettingsEntity?.discountRecurringCycleLimitOnCancellation).toString())
      .replace(
        /{{cycleDuration}}/g,
        `${subscriptionContractDetails?.billingPolicyInterval.toLowerCase()}${
          parseInt(customerPortalSettingsEntity.discountRecurringCycleLimitOnCancellation) > 1 ? 's' : ''
        }`
      );

    switch (actionToHandle) {
      case CancellationReasonAction.DISCOUNT:
        return discountMessageOnCancellation;
      case CancellationReasonAction.SWAP:
        return (
          customerPortalSettingsEntity?.swapMessageOnCancellation ||
          "We don't want to see you go! Would you prefer to swap out the products in your subscription?"
        );
      case CancellationReasonAction.GIFT:
        return (
          customerPortalSettingsEntity?.giftMessageOnCancellation ||
          "We don't want to see you go! Would you prefer to give the subscription as a gift by changing the shipping address?"
        );
      case CancellationReasonAction.SKIP:
        return (
          customerPortalSettingsEntity?.skipMessageOnCancellation ||
          "We don't want to see you go! How about you just skip the next order of your subscription?"
        );
      case CancellationReasonAction.PAUSE:
        return (
          customerPortalSettingsEntity?.pauseMessageOnCancellation ||
          "We don't want to see you go! Would you like to pause subscription instead of cancelling?"
        )
          .replace('{{cycleNumber}}', cancellationManagementEntity?.pauseDurationCycle?.toString())
          .replace(
            /{{cycleDuration}}/g,
            `${subscriptionContractDetails?.billingPolicyInterval.toLowerCase()}${
              parseInt(customerPortalSettingsEntity?.pauseDurationCycle) > 1 ? 's' : ''
            }`
          );
      case CancellationReasonAction.CHANGE_DATE:
        return (
          customerPortalSettingsEntity?.changeDateMessageOnCancellation ||
          "We don't want to see you go! Would you prefer to change your subscription date?"
        );
      case CancellationReasonAction.CHANGE_ADDRESS:
        return (
          customerPortalSettingsEntity?.changeAddressMessageOnCancellation ||
          "We don't want to see you go! Would you prefer to change your shipping address?"
        );
      case CancellationReasonAction.UPDATE_FREQUENCY:
        return (
          customerPortalSettingsEntity?.updateFrequencyMessageOnCancellation ||
          "We don't want to see you go! Would you prefer to change the frequency of your subscription?"
        );
      default:
        return 'Are you sure?';
    }
  })();

  function onSelectCancellationReason(event: ChangeEvent<HTMLSelectElement>) {
    const reasonIndex = event.target.value;
    setSelectedCancellationReason(cancellationReasons[reasonIndex]);

    if (!cancellationReasons[reasonIndex].cancellationPrompt) {
      setCancellationFeedback('');
    }
  }

  return /* if */ actionToHandle === CancellationReasonAction.NONE ? (
    <>
      <label htmlFor="cancellationReason" className="as-block as-mb-2 as-text-sm as-font-medium as-text-gray-700">
        {customerPortalSettingsEntity?.selectCancellationReasonLabelText || `Select Cancellation Reason*`}
      </label>
      <select
        id="cancellationReason"
        name="cancellationReason"
        required
        onChange={onSelectCancellationReason}
        className="as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-p-2.5"
        defaultValue=""
      >
        <option value="" disabled>
          {customerPortalSettingsEntity?.selectCancellationReasonPlaceholderTextV2 || 'Select Cancellation Reason'}
        </option>
        {cancellationReasons.map((reason, reasonIndex) => {
          return (
            <option key={reasonIndex} value={reasonIndex}>
              {reason.cancellationReason}
            </option>
          );
        })}
      </select>

      {selectedCancellationReason?.cancellationPrompt && (
        <>
          <label htmlFor="cancellationFeedback" className="as-block as-mt-4 as-mb-2 as-text-sm as-font-medium as-text-gray-700">
            {customerPortalSettingsEntity?.cancellationReasonTextV2 || 'Cancellation Feedback'} (optional)
            <span className="as-text-xs as-float-right as-text-gray-500">{cancellationFeedback?.length}/1000</span>
          </label>
          <textarea
            id="cancellationFeedback"
            name="cancellationFeedback"
            rows={4}
            maxLength={1000}
            placeholder={customerPortalSettingsEntity?.cancellationReasonPlaceholderV2 || 'Your cancellation reason...'}
            onChange={event => setCancellationFeedback(event.target.value)}
            className="as-block as-w-full as-text-sm as-text-gray-900 as-bg-gray-50 as-rounded-lg as-border as-border-gray-300 focus:as-ring-blue-500 focus:as-border-blue-500 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:focus:as-border-blue-500"
          />
        </>
      )}
    </>
  ) : (
    <p className="as-text-sm as-font-medium as-text-gray-700" dangerouslySetInnerHTML={{__html: promptText}} />
  );
};

export default CustomerRetentionFlow;

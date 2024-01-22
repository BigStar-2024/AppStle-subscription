import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import axios from 'axios';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';
import CancellationReason, { CancellationReasonAction } from 'app/shared/model/cancellation-reason.model';
import { IRootState } from 'app/shared/reducers';
import { getCustomerPortalEntity } from 'app/entities/subscriptions/subscription.reducer';
import { getEntity as getCancellationManagementEntity } from 'app/entities/cancellation-management/cancellation-management.reducer';
import { getEntity as getCustomerPortalSettingsEntity } from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {
  getSubscriptionFreezeStatus,
  deleteCustomerEntity as cancelSubscriptionEntity,
  addDiscountCode,
} from 'app/entities/subscriptions/subscription.reducer';
import { getCustomerSubscriptionContractDetailsByContractId } from 'app/entities/subscription-contract-details/user-subscription-contract-details.reducer';
import { updateSubscriptionContractStatus } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { CancellationTypeStatus } from 'app/shared/model/enumerations/cancellation-type-status.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import { ISubscriptionContractDetails } from 'app/shared/model/subscription-contract-details.model';
import CustomerRetentionFlow from './CancelSubscription/CustomerRetentionFlow';
import {
  skipCustomerBillingOrder,
  getUpcomingOrderEntity,
} from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import ShippingUpdateModal from './ShippingUpdateModal';
import { getIsPrepaid } from 'app/shared/util/subscription-utils';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { BillingAttemptStatus } from 'app/shared/model/enumerations/billing-attempt-status.model';
import { ISubscriptionBillingAttempt } from 'app/shared/model/subscription-billing-attempt.model';
import RescheduleOrderDateModal from './RescheduleOrderDateModal';
import EditFrequencyModal from './EditFrequencyModal';
import { checkIfPreventCancellationBeforeDays } from 'app/shared/util/customer-utils';
import SwapProductCustomer from './AddProductModelStep/SwapProductCustomer';
import SelectExistingProductModal, { SelectProductData } from './SelectExistingProduct';
import Loader from './Loader';

interface CancelSubscriptionButtonProps {
  text: string;
  onClick: () => void;
  isShowingLoader?: boolean;
}

export const CancelSubscriptionButton = (props: CancelSubscriptionButtonProps) => {
  const { text, onClick, isShowingLoader } = props;
  return (
    <button
      onClick={onClick}
      disabled={isShowingLoader}
      type="button"
      className={`as-w-full as-items-center as-px-2 as-py-1 lg:as-px-4 lg:as-py-2 as-mt-2 as-border as-border-red-600 as-rounded-md as-shadow-sm as-text-sm as-font-medium as-text-red-600 as-bg-transparent hover:as-bg-red-600 hover:as-text-white focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-red-500  disabled:as-opacity-75 disabled:as-cursor-not-allowed disabled:as-bg-red-600 disabled:as-text-white as-button as-button--cancelsub`}
    >
      {isShowingLoader ? <Loader size="6" /> : text}
    </button>
  );
};

interface CancelSubscriptionModalProps {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
  contractId: number;
  subscriptionEntity?: ISubscription & { [key: string]: any };
  subscriptionContractDetails?: ISubscriptionContractDetails;
  onUpdate?: () => void;
  onLoading?: (isLoading: boolean) => void;
  currentCycle: number;
}

export const CancelSubscriptionModal = (props: CancelSubscriptionModalProps) => {
  const { isOpen, setIsOpen, contractId, onUpdate = () => {}, onLoading = () => {} } = props;

  // Redux Global State
  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const shopName = useSelector((state: IRootState) => state.shopInfo.entity?.shop);
  const activeSubscriptionEntityContractId = useSelector((state: IRootState) =>
    parseInt(state.subscription.entity?.id?.split('/')?.pop?.())
  );
  const subscriptionContractDetails: ISubscriptionContractDetails =
    props.subscriptionContractDetails ?? useSelector((state: IRootState) => state.userSubscriptionContractDetails.entity);
  const upcomingOrderId = useSelector(
    (state: IRootState) =>
      (state.subscriptionBillingAttempt.entity as Array<ISubscriptionBillingAttempt>).find?.(
        upcomingOrder => upcomingOrder?.status === BillingAttemptStatus.QUEUED
      )?.id
  );
  const isSubscriptionFrozen =
    useSelector((state: IRootState) => state.subscription.subscriptionContractFreezeStatus) ||
    (subscriptionEntity?.billingPolicy?.minCycles &&
      props.currentCycle &&
      props.currentCycle < subscriptionEntity?.billingPolicy?.minCycles);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const cancellationManagementEntity = useSelector((state: IRootState) => state.cancellationManagement.entity);
  const dispatch = useDispatch();

  // Local State
  const [isLoading, setIsLoading] = useState(false);
  const [isPausing, setIsPausing] = useState(false);

  const [retentionInfo, setRetentionInfo] = useState<{ reason: CancellationReason; feedback: string }>({ reason: null, feedback: '' });
  const [actionToHandle, setActionToHandle] = useState<CancellationReasonAction>(CancellationReasonAction.NONE);
  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message: string }>(null);

  const [selectedProductToSwap, setSelectedProductToSwap] = useState<SelectProductData>(null);

  const [isInternalOpen, setIsInternalOpen] = useState<boolean>(false);
  const [isShippingModalOpen, setIsShippingModalOpen] = useState<boolean>(false);
  const [isRescheduleModalOpen, setIsRescheduleModalOpen] = useState<boolean>(false);
  const [isFrequencyModalOpen, setIsFrequencyModalOpen] = useState<boolean>(false);
  const [isSelectProductModalOpen, setIsSelectProductModalOpen] = useState<boolean>(false);
  const [isSwapProductModalOpen, setIsSwapProductModalOpen] = useState<boolean>(false);

  useEffect(() => {
    getInitialEntities();
  }, []);

  // When isOpen changes, make internal state reflect it after making changes
  useEffect(() => {
    if (isOpen) {
      openModal();
    } else {
      setIsInternalOpen(false);
    }
  }, [isOpen]);

  const isPrepaid = getIsPrepaid(subscriptionContractDetails);

  const isCancellationPreventedBeforeDays = checkIfPreventCancellationBeforeDays(
    customerPortalSettingsEntity?.preventCancellationBeforeDays,
    subscriptionContractDetails?.nextBillingDate
  );

  const canPause =
    cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CANCEL_AFTER_PAUSE &&
    subscriptionContractDetails?.status != 'paused';

  const isActionButtonDisabled = (() => {
    if (cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CUSTOMER_RETENTION_FLOW) {
      return !retentionInfo.reason;
    }
    return isLoading;
  })();

  const isShowingInitialView: boolean = actionToHandle === CancellationReasonAction.NONE;
  const isShowingPromptView: boolean = actionToHandle !== CancellationReasonAction.NONE;

  const hasCancellationDiscount: boolean = (() => {
    return (subscriptionEntity as any)?.discounts?.edges?.some((discountNode: any) => discountNode?.node?.title == `cancel${contractId}`);
  })();

  const buttonConfirmType = (() => {
    if (cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CUSTOMER_RETENTION_FLOW) {
      return 'RETENTION';
    }
    if (canPause) {
      return 'PAUSE';
    }
    return 'CANCEL';
  })();

  const buttonConfirmText: string = (() => {
    if (responseInfo) return '';

    if (
      isSubscriptionFrozen ||
      isCancellationPreventedBeforeDays ||
      cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CANCELLATION_INSTRUCTIONS
    ) {
      return '';
    }

    if (cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CANCEL_AFTER_PAUSE) {
      if (subscriptionContractDetails?.status != 'paused') {
        return customerPortalSettingsEntity?.acceptButtonTextV2 || 'Accept';
      } else {
        return customerPortalSettingsEntity?.noCancelTheSubscriptionButtonTextV2 || 'No, cancel the subscription';
      }
    }

    if (cancellationManagementEntity?.cancellationType === CancellationTypeStatus.CUSTOMER_RETENTION_FLOW) {
      if (isShowingPromptView) {
        return customerPortalSettingsEntity?.acceptButtonTextV2 || 'Accept';
      }
    }

    const paymentTypeText = isPrepaid
      ? customerPortalSettingsEntity.cancelSubscriptionPrepaidButtonText
      : customerPortalSettingsEntity.cancelSubscriptionPayAsYouGoButtonText;

    // TODO: Add "confirmCommonText" property to ICustomerPortalSettings
    return paymentTypeText || customerPortalSettingsEntity?.confirmCommonText || 'Confirm';
  })();

  const primaryUpdatingFlag = canPause ? isPausing : isLoading;

  const secondaryButtonText = ((): string => {
    if (responseInfo) return '';

    if (canPause || isShowingPromptView) {
      return customerPortalSettingsEntity?.noCancelTheSubscriptionButtonTextV2 || 'No, cancel the subscription';
    }

    return '';
  })();

  const secondaryButtonAction = (() => {
    if (canPause || isShowingPromptView) {
      return () => onConfirm('CANCEL');
    }

    return null;
  })();

  const secondaryUpdatingFlag = canPause && isLoading && !isPausing;

  const cancelImmediatelyText = ((): string => {
    const paymentTypeText = isPrepaid
      ? customerPortalSettingsEntity.cancelSubscriptionConfirmPrepaidText
      : customerPortalSettingsEntity.cancelSubscriptionConfirmPayAsYouGoText;

    return paymentTypeText || customerPortalSettingsEntity.deleteConfirmationMsgTextV2 || 'Are you sure?';
  })();

  const cancelAfterPauseText = (() => {
    if (subscriptionContractDetails?.status == 'paused') {
      return (
        customerPortalSettingsEntity?.subscriptionIsStillPausedText ||
        "You're subscription is still paused. Are you sure you don't want to keep it paused instead of cancelling?"
      );
    }
    return (
      cancellationManagementEntity?.pauseInstructionsText ||
      `We certainly don't want you to cancel your subscription. How about we apply a temporary pause on the subscription of {{pauseDurationCycle}} number of cycles. After that, you can reach back to us to re-activate the subscription or you can activate the subscription from your customer portal.`
    ).replace('{{pauseDurationCycle}}', cancellationManagementEntity?.pauseDurationCycle?.toString());
  })();

  const cancellationNotAllowedText: string = (() => {
    if (isSubscriptionFrozen) {
      return (
        customerPortalSettingsEntity?.subscriptionContractFreezeMessageV2 || 'Your subscription contract is frozen by your shop owner.'
      );
    }

    if (isCancellationPreventedBeforeDays) {
      return (
        customerPortalSettingsEntity?.preventCancellationBeforeDaysMessage?.replaceAll(
          '{{preventDays}}',
          `${customerPortalSettingsEntity?.preventCancellationBeforeDays}`
        ) ||
        `You can not pause/cancel the subscription before the ${customerPortalSettingsEntity?.preventCancellationBeforeDays} days from your next order date.`
      );
    }

    return 'Cannot cancel.';
  })();

  function setLoading(loading: boolean) {
    setIsLoading(loading);
    onLoading(loading);
  }

  function getInitialEntities() {
    if (!cancellationManagementEntity?.cancellationType) {
      dispatch(getCancellationManagementEntity(0));
    }
    if (!customerPortalSettingsEntity) {
      dispatch(getCustomerPortalSettingsEntity(0));
    }
  }

  async function loadEntities() {
    setLoading(true);
    if (contractId !== activeSubscriptionEntityContractId || !upcomingOrderId) {
      dispatch(
        getUpcomingOrderEntity(
          contractId.toString(),
          subscriptionContractDetails?.customerId?.toString(),
          subscriptionContractDetails?.shop
        )
      );
    }

    if (contractId !== activeSubscriptionEntityContractId) {
      getSubscriptionFreezeStatus(contractId)(dispatch);

      if (!props.subscriptionEntity) {
        //using "as any" to suppress "await has no effect" warning
        await (dispatch as any)(getCustomerPortalEntity(contractId));
      }
    }
    setLoading(false);
  }

  async function updateEntities() {
    dispatch(getCustomerPortalEntity(contractId));
    dispatch(getCustomerSubscriptionContractDetailsByContractId(contractId));
    onUpdate();
  }

  async function onConfirm(type: 'CANCEL' | 'PAUSE' | 'RETENTION' | 'ACTION') {
    setLoading(true);

    if (type === 'CANCEL') {
      await cancelSubscription();
    }

    if (type === 'PAUSE') {
      setIsPausing(true);
      await pauseSubscription();
    }

    if (type === 'RETENTION') {
      setLoading(false);
      handleRetentionAction();
      return;
    }

    if (type === 'ACTION') {
      switch (actionToHandle) {
        case CancellationReasonAction.DISCOUNT:
          await applyDiscount();
          break;
        case CancellationReasonAction.SWAP:
          openSecondaryModal('SELECT');
          return;
        case CancellationReasonAction.PAUSE:
          await pauseSubscription();
          return;
        case CancellationReasonAction.GIFT:
        case CancellationReasonAction.CHANGE_ADDRESS:
          openSecondaryModal('SHIPPING');
          return;
        case CancellationReasonAction.CHANGE_DATE:
          openSecondaryModal('RESCHEDULE');
          return;
        case CancellationReasonAction.SKIP:
          await skipNextOrder();
          break;
        case CancellationReasonAction.UPDATE_FREQUENCY:
          openSecondaryModal('FREQUENCY');
          return;
      }
    }

    updateEntities();
    setLoading(false);
    setIsPausing(false);
    setIsOpen(true);
  }

  async function cancelSubscription() {
    await cancelSubscriptionEntity(contractId, {
      cancellationFeedback: retentionInfo.reason?.cancellationReason,
      cancellationTextAreaFeedback: retentionInfo?.feedback,
    })(dispatch)
      .then(response => {
        if (response?.value?.status === 204) {
          setResponseInfo({ success: true, message: 'Success!' });
        } else {
          setResponseInfo({ success: false, message: response?.action?.payload?.response?.data?.message });
        }
      })
      .catch(error => {
        setResponseInfo({ success: false, message: error?.response?.data?.message || error?.message || error });
      });
  }

  async function pauseSubscription() {
    await updateSubscriptionContractStatus(
      contractId,
      'PAUSED',
      cancellationManagementEntity.pauseDurationCycle
    )(dispatch)
      .then(response => {
        if (response?.value?.status === 200) {
          setResponseInfo({ success: true, message: 'Success!' });
        } else {
          setResponseInfo({ success: false, message: response?.action?.payload?.response?.data?.message });
        }
        dispatch(getCustomerPortalEntity(contractId));
        setLoading(false);
      })
      .catch(error => {
        setResponseInfo({ success: false, message: error });
        setLoading(false);
      });
  }

  async function applyDiscount() {
    if (hasCancellationDiscount) return;

    const recurringCycleLimit = parseInt(customerPortalSettingsEntity?.discountRecurringCycleLimitOnCancellation)
      ? parseInt(customerPortalSettingsEntity?.discountRecurringCycleLimitOnCancellation).toString()
      : null;

    await addDiscountCode(contractId.toString(), {
      discountTitle: 'cancel' + contractId,
      discountType: 'PERCENTAGE',
      percentage: retentionInfo.reason.cancellationDiscount.toString(),
      ...(recurringCycleLimit && { recurringCycleLimit: recurringCycleLimit }),
    })(dispatch)
      .then(response => {
        if (response?.value?.status === 200) {
          setResponseInfo({ success: true, message: 'Success!' });
        } else {
          setResponseInfo({ success: false, message: response?.action?.payload?.response?.data?.message });
        }
      })
      .catch(error => setResponseInfo({ success: false, message: error }));

    if (cancellationManagementEntity?.enableDiscountEmail) {
      sendDiscountEmail();
    }
  }

  async function sendDiscountEmail() {
    axios.post('api/send-raw-email', {
      customerEmailAddress: cancellationManagementEntity?.discountEmailAddress,
      subject: 'Your customer chose a discount instead of cancelling their subscription!',
      htmlBody: `${subscriptionContractDetails.customerName} (Contract #${subscriptionContractDetails?.subscriptionContractId}) chose to apply the offered ${retentionInfo.reason?.cancellationDiscount}% discount instead of cancelling their subscription.`,
      shopName,
    });
  }

  async function skipNextOrder() {
    await skipCustomerBillingOrder(
      upcomingOrderId,
      contractId,
      subscriptionContractDetails.customerId,
      isPrepaid
    )(dispatch)
      .then(response => {
        if (response?.status === 200) {
          setResponseInfo({ success: true, message: 'Success!' });
        } else {
          setResponseInfo({ success: false, message: response?.action?.payload?.response?.data?.message });
        }
      })
      .catch(error => setResponseInfo({ success: false, message: error }));
  }

  function handleRetentionAction() {
    if (retentionInfo.reason?.cancellationAction == null || retentionInfo.reason?.cancellationAction === CancellationReasonAction.NONE) {
      onConfirm('CANCEL');
      return;
    }

    // If actionToHandle is set to none, but retentionInfo.reason.cancellationAction
    // is set to a non-none action, then we need to set the actionToHandle to handle the action
    if (isShowingInitialView) {
      if (retentionInfo.reason.cancellationAction === CancellationReasonAction.DISCOUNT && hasCancellationDiscount) {
        onConfirm('CANCEL');
        return;
      }

      setActionToHandle(retentionInfo.reason.cancellationAction);
      return;
    }

    onConfirm('ACTION');
  }

  function selectProductToSwap(productData: SelectProductData) {
    setSelectedProductToSwap(productData);
    openSecondaryModal('SWAP');
    setIsSelectProductModalOpen(false);
  }

  function openSecondaryModal(type: 'SHIPPING' | 'RESCHEDULE' | 'FREQUENCY' | 'SELECT' | 'SWAP') {
    switch (type) {
      case 'SHIPPING':
        setIsShippingModalOpen(true);
        break;
      case 'RESCHEDULE':
        setIsRescheduleModalOpen(true);
        break;
      case 'FREQUENCY':
        setIsFrequencyModalOpen(true);
        break;
      case 'SELECT':
        setIsSelectProductModalOpen(true);
        break;
      case 'SWAP':
        setIsSwapProductModalOpen(true);
        break;
    }
    setLoading(false);
    setIsPausing(false);
    setIsOpen(false);
  }

  async function openModal() {
    setIsLoading(true);
    await loadEntities();
    setIsInternalOpen(true);
    setIsLoading(false);
  }

  function reset() {
    setActionToHandle(CancellationReasonAction.NONE);
    setRetentionInfo({ reason: null, feedback: '' });
    setResponseInfo(null);
    setSelectedProductToSwap(null);
  }

  return (
    <>
      {/*
      // @ts-ignore Not using all props throws error */}
      <TailwindModal
        open={isInternalOpen}
        setOpen={setIsOpen}
        modalTitle={customerPortalSettingsEntity.cancelAccordionTitle}
        actionMethod={() => onConfirm(buttonConfirmType)}
        actionButtonText={buttonConfirmText}
        actionButtonInValid={isActionButtonDisabled}
        updatingFlag={primaryUpdatingFlag}
        secondaryActionMethod={secondaryButtonAction}
        secondaryActionButtonText={secondaryButtonText}
        secondaryActionUpdatingFlag={secondaryUpdatingFlag}
        afterClose={reset}
      >
        {/* if */ responseInfo ? (
          <PopupMessaging
            showSuccess={responseInfo.success}
            showError={!responseInfo.success}
            successMessage={null}
            errorMessage={!responseInfo.success ? responseInfo.message : ''}
          />
        ) : /* else if */ isSubscriptionFrozen || isCancellationPreventedBeforeDays ? (
          <p className="as-text-gray-500 as-text-sm" dangerouslySetInnerHTML={{__html: cancellationNotAllowedText}} />
        ) : (
          /* else */
          <>
            {cancellationManagementEntity.cancellationType === CancellationTypeStatus.CANCEL_IMMEDIATELY && (
              <p
                className="as-text-gray-500 as-text-sm"
                dangerouslySetInnerHTML={{
                  __html: cancelImmediatelyText,
                }}
              ></p>
            )}
            {cancellationManagementEntity.cancellationType === CancellationTypeStatus.CUSTOMER_RETENTION_FLOW && (
              <>
                <CustomerRetentionFlow
                  subscriptionContractDetails={subscriptionContractDetails}
                  onSetInfo={setRetentionInfo}
                  actionToHandle={actionToHandle}
                />
              </>
            )}
            {cancellationManagementEntity.cancellationType === CancellationTypeStatus.CANCELLATION_INSTRUCTIONS && (
              <p dangerouslySetInnerHTML={{ __html: cancellationManagementEntity.cancellationInstructionsText }}></p>
            )}
            {cancellationManagementEntity.cancellationType === CancellationTypeStatus.CANCEL_AFTER_PAUSE && (
              <div className="as-text-sm as-font-medium as-text-gray-700" dangerouslySetInnerHTML={{__html: cancelAfterPauseText}}/>
            )}
          </>
        )}
      </TailwindModal>
      <ShippingUpdateModal subscriptionEntity={subscriptionEntity} isOpen={isShippingModalOpen} setIsOpen={setIsShippingModalOpen} />
      <RescheduleOrderDateModal
        subscriptionEntity={subscriptionEntity}
        isOpen={isRescheduleModalOpen}
        setIsOpen={setIsRescheduleModalOpen}
      />
      <EditFrequencyModal subscriptionEntity={subscriptionEntity} isOpen={isFrequencyModalOpen} setIsOpen={setIsFrequencyModalOpen} />
      <SelectExistingProductModal
        subscriptionEntity={subscriptionEntity}
        onSelect={selectProductToSwap}
        isOpen={isSelectProductModalOpen}
        setIsOpen={setIsSelectProductModalOpen}
      />
      <SwapProductCustomer
        upcomingOrderId={upcomingOrderId?.toString()}
        lineId={selectedProductToSwap?.lineId}
        fullfillmentId={subscriptionEntity?.originOrder?.fulfillmentOrders?.edges[0]?.node?.id}
        sellingPlanIds={subscriptionEntity?.lines?.edges?.map((line: any) => {
          return line?.node?.sellingPlanId;
        })}
        shopName={shopName}
        contractId={contractId}
        subscriptionContractFreezeStatus={isSubscriptionFrozen}
        customerPortalSettingEntity={customerPortalSettingsEntity}
        isSwapProduct
        isSwapProductModalOpen={isSwapProductModalOpen}
        setIsSwapProductModalOpen={setIsSwapProductModalOpen}
      />
    </>
  );
};

interface CancelSubscriptionProps {
  contractId: number;
  subscriptionEntity?: ISubscription & { [key: string]: any };
  subscriptionContractDetails?: ISubscriptionContractDetails;
  onUpdate?: () => void;
  currentCycle: number;
}

const CancelSubscription = (props: CancelSubscriptionProps) => {
  const { contractId, subscriptionEntity, onUpdate } = props;

  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );
  const subscriptionContractDetails: ISubscriptionContractDetails =
    props.subscriptionContractDetails ?? useSelector((state: IRootState) => state.userSubscriptionContractDetails.entity);

  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const isCancellingEnabled =
    subscriptionContractDetails != null && subscriptionContractDetails?.status !== 'cancelled' && customerPortalSettingsEntity.cancelSub;

  return (
    <>
      {isCancellingEnabled && (
        <CancelSubscriptionButton
          text={customerPortalSettingsEntity?.cancelSubscriptionBtnText || 'Cancel Subscription'}
          isShowingLoader={isLoading}
          onClick={() => setIsModalOpen(true)}
        />
      )}
      <CancelSubscriptionModal
        isOpen={isModalOpen}
        setIsOpen={setIsModalOpen}
        contractId={contractId}
        subscriptionEntity={subscriptionEntity}
        subscriptionContractDetails={subscriptionContractDetails}
        onUpdate={onUpdate}
        onLoading={loading => setIsLoading(loading)}
        currentCycle={props?.currentCycle}
      />
    </>
  );
};

export default CancelSubscription;

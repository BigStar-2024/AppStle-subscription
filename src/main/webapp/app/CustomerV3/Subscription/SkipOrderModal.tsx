import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import { skipCustomerBillingOrder } from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { getIsPrepaid } from 'app/shared/util/subscription-utils';
import { getCustomerIdentifier } from 'app/shared/util/customer-utils';
import TailwindModal from './TailwindModal';
import PopupMessaging from './PopupMessaging';

interface SkipOrderModalProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const SkipOrderModal = (props: SkipOrderModalProps) => {
  const {
    isOpen,
    setIsOpen,
  } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector((state: IRootState) => state.customerPortalSettings.entity);
  const upcomingOrderId: number = useSelector((state: IRootState) => state.subscriptionBillingAttempt.entity?.[0]?.id)
  const dispatch = useDispatch();

  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message?: string }>(null);
  const [isUpdating, setIsUpdating] = useState(false);

  const contractId = subscriptionEntity?.id?.split?.("/")?.pop?.();
  const isPrepaid = getIsPrepaid(subscriptionEntity);
  const customerId = getCustomerIdentifier();

  const skipShipment = async () => {
    setIsUpdating(true);

    const orderId = isPrepaid
      ? parseInt(
          upcomingOrderId
            .toString()
            ?.split('/')
            .pop()
        )
      : upcomingOrderId;

      skipCustomerBillingOrder(orderId, contractId, customerId, isPrepaid)(dispatch).then(results => {
        if (results?.status === 200) {
          setResponseInfo({ success: true });
        } else {
          setResponseInfo({ success: false });
        }
        setIsUpdating(false);
      });
  };

  function resetModal() {
    setResponseInfo(null);
  }

  return (
    //@ts-ignore TypeScript throw errors for "missing" props
    <TailwindModal
      open={isOpen}
      setOpen={setIsOpen}
      actionMethod={skipShipment}
      actionButtonText={!responseInfo ? customerPortalSettingsEntity?.skipOrderButtonText || 'Skip Order' : ''}
      updatingFlag={isUpdating}
      modalTitle={customerPortalSettingsEntity?.skipOrderButtonText || 'Skip Order'}
      className="as-model-skip-order"
      success={responseInfo?.success}
      afterClose={resetModal}
    >
      {!!responseInfo && (
        <PopupMessaging
          showSuccess={responseInfo.success}
          showError={!responseInfo.success}
          successMessage={responseInfo.message}
          errorMessage={responseInfo.message}
        />
      )}
      {!responseInfo && (
        <div className="as-text-sm as-text-gray-500">
          <div>
            <p className="as-text-gray-500 as-text-sm">
              {customerPortalSettingsEntity?.upcomingOrderSkipAlertTextV2 || 'Are you sure that you want to skip the upcoming order?'}
            </p>
          </div>
        </div>
      )}
    </TailwindModal>
  );
};

export default SkipOrderModal;

import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { ISubscription } from 'app/shared/model/subscription.model';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { IRootState } from 'app/shared/reducers';
import ShippingUpdateModal from './ShippingUpdateModal';

interface ShippingDetailsInfoProps {
  onClickEdit: () => void,
  subscriptionEntity?: ISubscription & {[key: string]: any},
}

export const ShippingDetailsInfo = (props: ShippingDetailsInfoProps) => {
  const {
    onClickEdit,
  } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)
  const customerPortalSettingsEntity: ICustomerPortalSettings & {[key: string]: any} = useSelector((state: IRootState) => state.customerPortalSettings.entity)

  const deliveryInfo = subscriptionEntity?.deliveryMethod;

  return (
    <div className="as-bg-white as-shadow as-overflow-hidden sm:as-rounded-lg as-p-4 as-card as-edit-shipping">
      <div className="as-flex as-justify-between as-mb-2">
        <p className="as-text-sm as-text-gray-500 as-card_title as-edit-shipping_title">{customerPortalSettingsEntity?.shippingLabelText}</p>
        {customerPortalSettingsEntity?.changeShippingAddressFlag && (
          <p
            className="as-text-sm as-text-blue-500 as-cursor-pointer as-cta as-card_cta as-edit-shipping_cta"
            onClick={onClickEdit}
          >
            {customerPortalSettingsEntity?.editShippingButtonTextV2 || 'Edit'}
          </p>
        )}
      </div>

      <div className="as-card_data as-edit-shipping_data">
        {deliveryInfo?.__typename === 'SubscriptionDeliveryMethodShipping' && (
          <p className="as-text-sm as-text-gray-800 as-pt-3 as-shipping-option">
            <span className="as-text-gray-900 as-shipping-label">
              {customerPortalSettingsEntity?.shippingOptionText}
              <span className="colon-symbol">: </span>
            </span>
            <span className="as-shipping-value">{deliveryInfo?.shippingOption?.title}</span>
          </p>
        )}
        {deliveryInfo?.__typename === 'SubscriptionDeliveryMethodLocalDelivery' && (
          <p className="as-text-sm as-text-gray-800 as-pt-3 as-shipping-option">
            <span className="as-text-gray-900 as-shipping-label">
              {customerPortalSettingsEntity?.LocalshippingOptionTextV2 || 'Local shipping option'}
              <span className="colon-symbol">: </span>
            </span>
            <span className="as-shipping-value">{deliveryInfo?.localDeliveryOption?.title}</span>
          </p>
        )}
        {deliveryInfo?.__typename === 'SubscriptionDeliveryMethodPickup' && (
          <p className="as-text-sm as-text-gray-800 as-pt-3 as-shipping-option">
            <span className="as-text-gray-900 as-shipping-label">
              {customerPortalSettingsEntity?.pickupShippingOptionTextV2 || 'Pickup shipping option'}
              <span className="colon-symbol">: </span>
            </span>
            <span className="as-shipping-value">{deliveryInfo?.pickupOption?.title}</span>
          </p>
        )}
        {deliveryInfo?.__typename !== 'SubscriptionDeliveryMethodPickup' ? (
          <>
            <p className="as-text-sm as-text-gray-900 as-pt-3 as-shipping-address-title">
              {customerPortalSettingsEntity?.addressHeaderTitleText}
              <span className="colon-symbol">:</span>
            </p>
            {
              <address className="as-text-sm as-text-gray-800 as-pt-1 as-shipping-address" style={{ fontStyle: 'normal' }}>
                {!deliveryInfo?.address ? (
                  <p className="text-muted shipping-address-not-available">
                    {customerPortalSettingsEntity?.shippingAddressNotAvailableText || 'Not Available'}
                  </p>
                ) : (
                  <>
                    {deliveryInfo?.address?.name && (
                      <span className={'customer-address'}>
                        {deliveryInfo?.address?.name}
                        <br />
                      </span>
                    )}
                    {deliveryInfo?.address?.address1 && (
                      <span className={'customer-address-one'}>
                        {deliveryInfo?.address?.address1}
                        <br />
                      </span>
                    )}
                    {deliveryInfo?.address?.address2 && (
                      <span className={'customer-address-two'}>
                        {deliveryInfo?.address?.address2}
                        <br />
                      </span>
                    )}
                    {deliveryInfo?.address?.city && deliveryInfo?.address?.city}
                    {deliveryInfo?.address?.province && (
                      <span className={'customer-province'}>, {deliveryInfo?.address?.province}</span>
                    )}
                    {deliveryInfo?.address?.country && (
                      <span className={'customer-country'}>, {deliveryInfo?.address?.country} - </span>
                    )}
                    {deliveryInfo?.address?.zip && deliveryInfo?.address?.zip}
                    {deliveryInfo?.address?.phone && (
                      <span className={'customer-phone'}>
                        <br />
                        {deliveryInfo?.address?.phone}
                      </span>
                    )}
                  </>
                )}
              </address>
            }
          </>
        ) : (
          <address className="as-text-sm as-text-gray-800 as-pt-1 as-shipping-address">
            {deliveryInfo?.pickupOption?.location?.address?.address1}
            <br />
            {deliveryInfo?.pickupOption?.location?.address?.city} ,{' '}
            {deliveryInfo?.pickupOption?.location?.address?.province} -{' '}
            {deliveryInfo?.pickupOption?.location?.address?.zip}
          </address>
        )}
      </div>
    </div>
  );
};

interface ShippingDetailsProps {
  subscriptionEntity?: ISubscription & {[key: string]: any}
}

const ShippingDetails = (props: ShippingDetailsProps) => {

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity)

  const [showShippingUpdateModal, setShowShippingUpdateModal] = useState(false);

  return (
    <>
      <ShippingDetailsInfo
        onClickEdit={() => setShowShippingUpdateModal(true)}
        subscriptionEntity={subscriptionEntity}
      />
      <ShippingUpdateModal
        subscriptionEntity={subscriptionEntity}
        isOpen={showShippingUpdateModal}
        setIsOpen={isOpen => setShowShippingUpdateModal(isOpen)}
      />
    </>
  );
}

export default ShippingDetails;

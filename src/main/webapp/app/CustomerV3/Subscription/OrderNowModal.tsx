import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import { updateAttemptBillingEntity } from 'app/entities/subscription-billing-attempt/subscription-billing-attempt.reducer';
import { splitSubscriptionContract, reset } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import PopupMessaging from './PopupMessaging';
import TailwindModal from './TailwindModal';
import { useHistory } from 'react-router-dom';

interface OrderNowModalProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const OrderNowModal = (props: OrderNowModalProps) => {
  const {
    isOpen,
    setIsOpen,
  } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const shopName = useSelector((state: IRootState) => state.shopInfo.entity.shop);
  const customerPortalSettingEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector((state: IRootState) => state.customerPortalSettings.entity)
  const upcomingOrderId: number = useSelector((state: IRootState) => state.subscriptionBillingAttempt.entity?.[0]?.id)
  const updatingBillingAttempt = useSelector((state: IRootState) => state.subscriptionBillingAttempt.updatingBillingAttempt)
  const splitContractUpdating = useSelector((state: IRootState) => state.subscriptionContractDetails.updating)
  const splitContractUpdated = useSelector((state: IRootState) => state.subscriptionContractDetails.splitContractUpdated)
  const splitContractDetails = useSelector((state: IRootState) => state.subscriptionContractDetails.splitContractDetails)
  const dispatch = useDispatch();

  const [responseInfo, setResponseInfo] = useState<{ success: boolean; message?: string }>(null);
  const [shouldOrderAllProducts, setShouldOrderAllProducts] = useState(true);
  const [splitContractProducts, setSplitContractProducts] = useState<any[]>([]);

  const history = useHistory();
  const contractId = subscriptionEntity?.id?.split?.('/')?.pop?.()

  async function attemptBillingHandler() {
    if (shouldOrderAllProducts || splitContractProducts.length === subscriptionEntity?.lines?.edges?.length) {
      await updateAttemptBillingEntity(upcomingOrderId, shopName, contractId)(dispatch)
        .then((results: any) => {
          if (results?.value?.status === 200) {
            setResponseInfo({ success: true });
          }
        })
        .catch((error: any) => {
          setResponseInfo({ success: false, message: error?.response?.data?.message });
        });
    } else {
      dispatch(splitSubscriptionContract(contractId, true, true, splitContractProducts));
    }
  }

  const handleSplitContractProducts = (lineId: string | number) => {
    const newProductsIds = [...splitContractProducts];
    const index = newProductsIds.findIndex(id => id === lineId);
    if (index > -1) {
      newProductsIds.splice(index, 1);
    } else {
      newProductsIds.push(lineId);
    }
    setSplitContractProducts(newProductsIds);
  };

  useEffect(() => {
    if (splitContractUpdated && splitContractDetails && splitContractDetails.id) {
      history.push(`/subscriptions/${splitContractDetails.id.split('/').pop()}/detail`);
      resetModal();
      dispatch(reset());
    }
  }, [splitContractUpdated]);

  function resetModal() {
    setResponseInfo(null);
  }

  return (
    //@ts-ignore TailwindModal doesn't need all props
    <TailwindModal
      open={isOpen}
      setOpen={setIsOpen}
      actionMethod={attemptBillingHandler}
      actionButtonText={!responseInfo ? customerPortalSettingEntity?.orderNowText || 'Order Now' : ''}
      updatingFlag={updatingBillingAttempt || splitContractUpdating}
      modalTitle={customerPortalSettingEntity?.orderNowText || 'Order Now'}
      className="as-model-order-now"
      success={responseInfo?.success}
      afterClose={resetModal}
    >
      {!!responseInfo && (
        <PopupMessaging
          showSuccess={responseInfo?.success}
          showError={!responseInfo?.success}
          successMessage={null}
          errorMessage={responseInfo?.message}
        />
      )}
      {!responseInfo && (
        <div className="as-text-sm as-text-gray-500">
          <div>
            <p className="as-text-gray-500 as-text-sm">
              {customerPortalSettingEntity?.upcomingOrderPlaceNowAlertTextV2 ||
                'Are you sure that you want to place your upcoming order now?'}
            </p>
            <p className="as-text-gray-500 as-text-xs as-my-4">
              {customerPortalSettingEntity?.orderNowDescriptionText ||
                'Please note, once you confirm Order Now. The Subscription details will be updated within a minute or you may refresh the page after sometime. It is requested that multiple attempts of Order Now should not be placed'}
            </p>
            {subscriptionEntity?.lines?.edges?.length > 1 && customerPortalSettingEntity?.enableSplitContract && (
              <div>
                <div className="as-flex as-items-center as-mb-4">
                  <input
                    checked={shouldOrderAllProducts}
                    onChange={_value => setShouldOrderAllProducts(true)}
                    id="default-radio-1"
                    type="radio"
                    value="true"
                    name="default-radio"
                    className="as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-border-gray-300 as-focus:ring-blue-500  as-focus:ring-2"
                  />
                  <label htmlFor="default-radio-1" className="as-ml-2 as-text-sm as-font-medium as-text-gray-900">
                    Order all products.
                  </label>
                </div>
                <div className="flex items-center">
                  <input
                    checked={!shouldOrderAllProducts}
                    onChange={_value => setShouldOrderAllProducts(false)}
                    id="default-radio-2"
                    type="radio"
                    value="false"
                    name="default-radio"
                    className="as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-border-gray-300 as-focus:ring-blue-500  as-focus:ring-2"
                  />
                  <label htmlFor="default-radio-2" className="as-ml-2 as-text-sm as-font-medium as-text-gray-900">
                    Order specific products.
                  </label>
                </div>

                {!shouldOrderAllProducts && (
                  <div className="as-mt-4 as-ml-4">
                    <p className="as-text-gray-500 as-text-xs as-my-4">
                      {customerPortalSettingEntity?.splitContractMessage ||
                        'Once you confirm the split contract. A new, separate contract will be created.'}
                    </p>
                    {subscriptionEntity?.lines?.edges?.map((product: any) => {
                      return (
                        product?.node &&
                        product?.node != null &&
                        product?.node?.productId != null && (
                          <div className="as-flex as-items-center">
                            <input
                              className="!as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-rounded as-border-gray-300 as-focus:ring-blue-500 as-focus:ring-2"
                              id={product?.node?.id}
                              checked={splitContractProducts.findIndex((item: any) => item === product?.node?.id) > -1}
                              type="checkbox"
                              onChange={() => handleSplitContractProducts(product?.node?.id)}
                              style={{ width: '16px' }}
                            />
                            <label
                              htmlFor="default-checkbox"
                              className="as-ml-2 as-text-sm as-font-medium as-text-gray-900 as-flex as-items-center"
                            >
                              <img
                                src={product?.node?.variantImage?.transformedSrc}
                                width={50}
                                style={{ borderRadius: '29%', padding: '10px' }}
                              />
                              <span>
                                {' '}
                                {product?.node?.title}{' '}
                                {product?.node?.variantTitle && product?.node?.variantTitle !== '-' && '-' + product?.node?.variantTitle}
                              </span>
                            </label>
                          </div>
                        )
                      );
                    })}
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </TailwindModal>
  );
};

export default OrderNowModal;

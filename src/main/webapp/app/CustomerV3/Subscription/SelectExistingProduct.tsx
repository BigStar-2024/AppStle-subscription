import React, { useMemo } from 'react';
import TailwindModal from './TailwindModal';
import { useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { ICustomerPortalSettings } from 'app/shared/model/customer-portal-settings.model';
import { ISubscription } from 'app/shared/model/subscription.model';
import ProductItem from './AddProductModelStep/ProductItem';
import { formatPrice } from 'app/shared/util/customer-utils';

export type SelectProductData = {
  lineId: string;
  title: string;
  imgSrc: string;
  variants: {
    price: number;
  }[];
};

interface SelectExistingProductProps {
  subscriptionEntity?: ISubscription & { [key: string]: any };
  onSelect: (product: SelectProductData) => void;
}

const SelectExistingProduct = (props: SelectExistingProductProps) => {
  const { onSelect } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const customerPortalSettingsEntity: ICustomerPortalSettings & {[key: string]: any} = useSelector((state: IRootState) => state.customerPortalSettings.entity);

  const subscriptionProducts = useMemo(() => {
    return subscriptionEntity?.lines?.edges?.map?.((edge: any) => edgeProductData(edge?.node));
  }, [subscriptionEntity]);

  function edgeProductData(edgeNode: any): SelectProductData {
    return {
      lineId: edgeNode?.id,
      title: edgeNode?.title,
      imgSrc: edgeNode?.variantImage?.transformedSrc,
      variants: [{ price: parseFloat(edgeNode?.lineDiscountedPrice?.amount) * 100 }],
    };
  }

  return (
    <div className={`as-mt-2 as-pt-5 as-grid as-gap-4 as-grid-cols-2 sm:as-pb-6`}>
      {subscriptionProducts.map((productData: SelectProductData) => (
        //@ts-ignore ignore missing props
        <ProductItem
          prdData={productData}
          formatPrice={formatPrice}
          purchaseOption={null}
          customerPortalSettingEntity={customerPortalSettingsEntity}
          selectProduct={onSelect}
          isSwapProductModalOpen
          selectProductButtonText={customerPortalSettingsEntity?.selectExistingProductButtonText || "Select"}
        />
      ))}
    </div>
  );
};

interface SelectExistingProductModalProps extends SelectExistingProductProps {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const SelectExistingProductModal = (props: SelectExistingProductModalProps) => {
  const { onSelect, isOpen, setIsOpen } = props;

  const subscriptionEntity = props.subscriptionEntity ?? useSelector((state: IRootState) => state.subscription.entity);
  const customerPortalSettingsEntity: ICustomerPortalSettings & { [key: string]: any } = useSelector(
    (state: IRootState) => state.customerPortalSettings.entity
  );

  return (
    //@ts-ignore
    <TailwindModal
      open={isOpen}
      setOpen={setIsOpen}
      modalTitle={customerPortalSettingsEntity?.selectProductLabelTextV2 || 'Select Product'}
      fullHeightModal
      ignoreSubscriptionContractFreezeStatus
      className="as-modal-swap-product"
    >
      <SelectExistingProduct subscriptionEntity={subscriptionEntity} onSelect={onSelect} />
    </TailwindModal>
  );
};

export default SelectExistingProductModal;

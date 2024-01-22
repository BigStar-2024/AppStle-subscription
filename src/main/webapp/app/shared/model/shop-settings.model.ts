import { DelayedTaggingUnit } from 'app/shared/model/enumerations/delayed-tagging-unit.model';
import { ShopSettingsOrderStatus } from 'app/shared/model/enumerations/shop-settings-order-status.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';
import { FulfillmentStatus } from 'app/shared/model/enumerations/fulfillment-status.model';

export interface IShopSettings {
  id?: number;
  shop?: string;
  taggingEnabled?: boolean;
  delayTagging?: boolean;
  delayedTaggingValue?: number;
  delayedTaggingUnit?: DelayedTaggingUnit;
  orderStatus?: ShopSettingsOrderStatus;
  paymentStatus?: PaymentStatus;
  fulfillmentStatus?: FulfillmentStatus;
  priceSyncEnabled?: boolean;
  skuSyncEnabled?: boolean;
}

export const defaultValue: Readonly<IShopSettings> = {
  taggingEnabled: false,
  delayTagging: false,
  priceSyncEnabled: false,
  skuSyncEnabled: false
};

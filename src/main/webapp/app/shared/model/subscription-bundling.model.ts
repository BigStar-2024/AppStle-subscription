import { BuildABoxRedirect } from 'app/shared/model/enumerations/build-a-box-redirect.model';
import { ProductViewSettings } from 'app/shared/model/enumerations/product-view-settings.model';
import { BuildABoxType } from 'app/shared/model/enumerations/build-a-box-type.model';

export interface ISubscriptionBundling {
  id?: number;
  shop?: string;
  subscriptionBundlingEnabled?: boolean;
  subscriptionId?: number;
  minProductCount?: number;
  maxProductCount?: number;
  discount?: number;
  uniqueRef?: string;
  bundleRedirect?: BuildABoxRedirect;
  customRedirectURL?: string;
  minOrderAmount?: number;
  tieredDiscount?: any;
  productViewStyle?: ProductViewSettings;
  buildABoxType?: BuildABoxType;
  singleProductSettings?: any;
  subscriptionGroup?: any;
  bundleTopHtml?: any;
  bundleBottomHtml?: any;
  proceedToCheckoutButtonText?: string;
  chooseProductsText?: string;
  name?: string;
  groupName?: string;
  subscriptionBundleLink?: string;
}

export const defaultValue: Readonly<ISubscriptionBundling> = {
  subscriptionBundlingEnabled: false,
  minProductCount: 0,
  maxProductCount: 0,
  discount: 0,
  minOrderAmount: 0,
  bundleRedirect: BuildABoxRedirect.CART,
  productViewStyle: ProductViewSettings.QUICK_ADD,
  buildABoxType: BuildABoxType.CLASSIC
};

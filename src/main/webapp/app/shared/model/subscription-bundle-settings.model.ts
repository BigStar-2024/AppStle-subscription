import { BundleRedirect } from 'app/shared/model/enumerations/bundle-redirect.model';

export interface ISubscriptionBundleSettings {
  id?: number;
  shop?: string;
  selectedFrequencyLabelText?: string;
  addButtonText?: string;
  selectMinimumProductButtonText?: string;
  productsToProceedText?: string;
  proceedToCheckoutButtonText?: string;
  myDeliveryText?: string;
  bundleTopHtml?: any;
  bundleBottomHtml?: any;
  failedToAddTitleText?: string;
  okBtnText?: string;
  failedToAddMsgText?: string;
  buttonColor?: string;
  backgroundColor?: string;
  pageBackgroundColor?: string;
  buttonBackgroundColor?: string;
  ProductTitleFontColor?: string;
  variantNotAvailable?: string;
  bundleRedirect?: BundleRedirect;
  isBundleWithoutScroll?: boolean;
  descriptionLength?: number;
  currencySwitcherClassName?: string;
  productPriceFormatField?: string;
  customRedirectURL?: string;
  viewProduct?: string;
  productDetails?: string;
  editQuantity?: string;
  cart?: string;
  shoppingCart?: string;
  title?: string;
  tieredDiscount?: string;
  subtotal?: string;
  checkoutMessage?: string;
  continueShopping?: string;
  spendAmountGetDiscount?: string;
  buyQuantityGetDiscount?: string;
  removeItem?: string;
  showCompareAtPrice?: boolean;
  enableRedirectToProductPage?: boolean;
  disableProductDescription?: boolean;
  enableDisplayProductVendor?: boolean;
  enableDisplayProductType?: boolean;
  enableCustomAdvancedFields?: boolean;
  hideProductSearchBox?: boolean;
  productFilterConfig?: any;
  enableShowProductBasePrice?: boolean;
  enableProductDetailButton?: boolean;
  enableClearCartSelectedProducts?: boolean;
  enableSkieyBABHeader?: boolean;
  enableOpeningSidebar?: boolean;
  rightSidebarHTML?: any;
  leftSidebarHTML?: any;
  isMergeIntoSingleBABVariantDropdown?: boolean;
}

export const defaultValue: Readonly<ISubscriptionBundleSettings> = {
  isBundleWithoutScroll: false,
  showCompareAtPrice: false,
};

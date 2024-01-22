import { RedirectOption } from 'app/shared/model/enumerations/redirect-option.model';
import { PlacementPosition } from 'app/shared/model/enumerations/placement-position.model';

export interface IBundleSetting {
  id?: number;
  shop?: string;
  showOnProductPage?: boolean;
  showMultipleOnProductPage?: boolean;
  actionButtonColor?: string;
  actionButtonFontColor?: string;
  productTitleColor?: string;
  productPriceColor?: string;
  redirectTo?: RedirectOption;
  showProductPrice?: boolean;
  oneTimeDiscount?: boolean;
  showDiscountInCart?: boolean;
  selector?: string;
  placement?: PlacementPosition;
  customCss?: any;
  variant?: string;
  deliveryFrequency?: string;
  perDelivery?: string;
  discountPopupHeader?: string;
  discountPopupAmount?: string;
  discountPopupCheckoutMessage?: string;
  discountPopupBuy?: string;
  discountPopupNo?: string;
  showDiscountPopup?: boolean;
}

export const defaultValue: Readonly<IBundleSetting> = {
  showOnProductPage: false,
  showMultipleOnProductPage: false,
  showProductPrice: false,
  oneTimeDiscount: false,
  showDiscountInCart: false,
  showDiscountPopup: false
};

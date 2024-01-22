import { PlacementPosition } from 'app/shared/model/enumerations/placement-position.model';

export interface IThemeCode {
  id?: number;
  themeName?: string;
  themeNameFriendly?: string;
  themeStoreId?: number;
  addToCartSelector?: any;
  subscriptionLinkSelector?: any;
  addToCartPlacement?: PlacementPosition;
  subscriptionLinkPlacement?: PlacementPosition;
  priceSelector?: any;
  pricePlacement?: PlacementPosition;
  badgeTop?: any;
  cartRowSelector?: any;
  cartLineItemSelector?: any;
  cartLineItemPerQuantityPriceSelector?: any;
  cartLineItemTotalPriceSelector?: any;
  cartLineItemSellingPlanNameSelector?: any;
  cartSubTotalSelector?: any;
  cartLineItemPriceSelector?: any;
}

export const defaultValue: Readonly<IThemeCode> = {};

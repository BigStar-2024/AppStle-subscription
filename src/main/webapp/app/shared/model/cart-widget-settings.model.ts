import { cartWidgetSettingApproach } from 'app/shared/model/enumerations/cart-widget-setting-approach.model';
import { PlacementPosition } from 'app/shared/model/enumerations/placement-position.model';

export interface ICartWidgetSettings {
  id?: number;
  shop?: string;
  enable_cart_widget_settings?: boolean;
  cartWidgetSettingApproach?: cartWidgetSettingApproach;
  cartRowSelector?: any;
  cartRowPlacement?: PlacementPosition;
  cartLineItemSelector?: any;
  cartLineItemPlacement?: PlacementPosition;
  cartFormSelector?: any;
  appstelCustomeSelector?: any;
}

export const defaultValue: Readonly<ICartWidgetSettings> = {
  enable_cart_widget_settings: false
};

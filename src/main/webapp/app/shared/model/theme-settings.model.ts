import { ShopifyThemeInstallationVersion } from 'app/shared/model/enumerations/shopify-theme-installation-version.model';
import { PlacementPosition } from 'app/shared/model/enumerations/placement-position.model';
import { WidgetTemplateType } from 'app/shared/model/enumerations/widget-template-type.model';

export interface IThemeSettings {
  id?: number;
  shop?: string;
  skip_setting_theme?: boolean;
  themeV2Saved?: boolean;
  themeName?: string;
  shopifyThemeInstallationVersion?: ShopifyThemeInstallationVersion;
  selectedSelector?: any;
  subscriptionLinkSelector?: any;
  customJavascript?: any;
  placement?: PlacementPosition;
  subscriptionLinkPlacement?: PlacementPosition;
  priceSelector?: any;
  pricePlacement?: PlacementPosition;
  badgeTop?: any;
  disableLoadingJquery?: boolean;
  quickViewClickSelector?: any;
  landingPagePriceSelector?: any;
  cartRowSelector?: any;
  cartLineItemSelector?: any;
  cartLineItemPerQuantityPriceSelector?: any;
  cartLineItemTotalPriceSelector?: any;
  cartLineItemSellingPlanNameSelector?: any;
  cartSubTotalSelector?: any;
  cartLineItemPriceSelector?: any;
  enableCartWidgetFeature?: boolean;
  enableSlowScriptLoad?: boolean;
  scriptLoadDelay?: number;
  formatMoneyOverride?: boolean;
  widgetTemplateType?: WidgetTemplateType;
  widgetTemplateHtml?: any;
  cartHiddenAttributesSelector?: string;
  scriptAttributes?: string;
}

export const defaultValue: Readonly<IThemeSettings> = {
  skip_setting_theme: false,
  themeV2Saved: false,
  disableLoadingJquery: false,
  enableCartWidgetFeature: false,
  enableSlowScriptLoad: false,
  formatMoneyOverride: false,
};

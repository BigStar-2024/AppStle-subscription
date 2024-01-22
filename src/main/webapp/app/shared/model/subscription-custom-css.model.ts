export interface ISubscriptionCustomCss {
  id?: number;
  shop?: string;
  customCss?: any;
  customerPoratlCSS?: any;
  bundlingCSS?: any;
  bundlingIframeCSS?: any;
}

export const defaultValue: Readonly<ISubscriptionCustomCss> = {};

export interface IShopAssetUrls {
  id?: number;
  shop?: string;
  vendorJavascript?: string;
  vendorCss?: string;
  customerJavascript?: string;
  customerCss?: string;
  bundleJavascript?: string;
  bundleCss?: string;
}

export const defaultValue: Readonly<IShopAssetUrls> = {};

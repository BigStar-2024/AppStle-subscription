export interface IProductInfo {
  id?: number;
  shop?: string;
  productId?: number;
  productTitle?: string;
  productHandle?: string;
}

export const defaultValue: Readonly<IProductInfo> = {};

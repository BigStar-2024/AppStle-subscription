export interface IVariantInfo {
  id?: number;
  shop?: string;
  productId?: number;
  variantId?: number;
  productTitle?: string;
  variantTitle?: string;
  sku?: string;
  variantPrice?: string;
}

export const defaultValue: Readonly<IVariantInfo> = {};

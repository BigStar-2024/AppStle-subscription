export interface IMembershipDiscountProducts {
  id?: number;
  shop?: string;
  membershipDiscountId?: number;
  productId?: number;
  productTitle?: string;
}

export const defaultValue: Readonly<IMembershipDiscountProducts> = {};

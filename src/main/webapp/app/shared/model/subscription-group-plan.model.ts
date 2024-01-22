export interface ISubscriptionGroupPlan {
  id?: number;
  shop?: string;
  groupName?: string;
  subscriptionId?: number;
  productCount?: number;
  productVariantCount?: number;
  infoJson?: any;
  productIds?: any;
  variantIds?: any;
  variantProductIds?: any;
}

export const defaultValue: Readonly<ISubscriptionGroupPlan> = {};

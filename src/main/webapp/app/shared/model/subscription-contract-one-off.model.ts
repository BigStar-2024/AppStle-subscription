export interface ISubscriptionContractOneOff {
  id?: number;
  shop?: string;
  billingAttemptId?: number;
  subscriptionContractId?: number;
  variantId?: number;
  variantHandle?: string;
  quantity?: number;
  price?: number;
}

export const defaultValue: Readonly<ISubscriptionContractOneOff> = {};

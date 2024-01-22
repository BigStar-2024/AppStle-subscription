export interface IUserSubscriptionContractDetails {
  activeSubscriptions?: number;
  customerId?: number;
  email?: string;
  name?: string;
  nextOrderDate?: string;
}

export const defaultValue: Readonly<IUserSubscriptionContractDetails> = {};

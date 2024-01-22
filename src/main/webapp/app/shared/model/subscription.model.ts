export interface ISubscription {
  subscriptionContracts: SubscriptionContract[];
  pageInfo: PageInfo;
}

export const defaultValue: Readonly<ISubscription> = {
  subscriptionContracts: [],
  pageInfo: { hasNextPage: false, cursor: '' }
};

interface PageInfo {
  cursor?: any;
  hasNextPage: boolean;
}

interface SubscriptionContract {
  id: number;
  createAt: string;
  status: string;
  nextBillingDate: string;
  customer: Customer;
  billingPolicy: BillingPolicy;
  orderCount: number;
  canceledSubscriptionCount: number;
}

interface BillingPolicy {
  interval: string;
  intervalCount: number;
}

interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  displayName: string;
  email: string;
  subscriptionCount: number;
  totalOrderCount: number;
}

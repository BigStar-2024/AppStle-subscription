export interface IAnalytics {
  id?: number;
  shop?: string;
  totalSubscriptions?: number;
  totalOrders?: number;
  totalOrderAmount?: number;
  firstTimeOrders?: number;
  recurringOrders?: number;
  totalCustomers?: number;
}

export const defaultValue: Readonly<IAnalytics> = {};

export const defaultSubscriptionAnalyticsValue = {
  totalSubscriptionAmount: 0.0,
  totalSubscriptionAmountSummary: 0.0,
  totalSubscriptionCount: 0,
  totalSubscriptionCountSummary: 0,
  currency: '$0.00'
};

export const defaultSubscriptionCanceledAnalyticsValue = {
  totalCanceledSubscriptionCount: 0,
  totalCanceledSubscriptionCountSummary: 0
};

export const defaultSubscriptionOrderAnalyticsValue = {
  totalOrderCount: 0,
  totalOrderCountSummary: 0,
  totalOrderAmount: 0.0,
  totalOrderAmountSummary: 0.0
};

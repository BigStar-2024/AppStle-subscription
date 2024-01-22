import { ISubscription } from '../model/subscription.model';
import { ISubscriptionContractDetails } from '../model/subscription-contract-details.model';
import { IRootState } from '../reducers';

export const isOneTimeProduct = line => {
  return line?.node?.customAttributes?.map(ca => ca.key)?.includes('_appstle-one-time-product');
};

export const isFreeProduct = line => {
  return line?.node?.customAttributes?.map(ca => ca.key)?.includes('_appstle-free-product');
};

export const getIsPrepaid = (subscription: ISubscriptionContractDetails | (ISubscription & { [key: string]: any })) => {
  if (subscription) {
    if ('billingPolicyIntervalCount' in subscription && 'deliveryPolicyIntervalCount' in subscription) {
      return subscription.billingPolicyIntervalCount !== subscription.deliveryPolicyIntervalCount;
    } else if ('billingPolicy' in subscription && 'deliveryPolicy' in subscription) {
      return subscription.billingPolicy?.intervalCount !== subscription.deliveryPolicy?.intervalCount;
    }
  }
  return false;
};

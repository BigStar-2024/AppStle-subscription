import { Moment } from 'moment';
import { subscriptionCreatedEmailSentStatus } from 'app/shared/model/enumerations/subscription-created-email-sent-status.model';
import { SubscriptionCreatedSmsSentStatus } from 'app/shared/model/enumerations/subscription-created-sms-sent-status.model';
import { SubscriptionOriginType } from 'app/shared/model/enumerations/subscription-origin-type.model';
import { SubscriptionType } from 'app/shared/model/enumerations/subscription-type.model';

export interface ISubscriptionContractDetails {
  id?: number;
  shop?: string;
  graphSubscriptionContractId?: string;
  subscriptionContractId?: number;
  billingPolicyInterval?: string;
  billingPolicyIntervalCount?: number;
  currencyCode?: string;
  customerId?: number;
  graphCustomerId?: string;
  deliveryPolicyInterval?: string;
  deliveryPolicyIntervalCount?: number;
  status?: string;
  graphOrderId?: string;
  orderId?: number;
  createdAt?: string;
  updatedAt?: string;
  nextBillingDate?: string;
  orderAmount?: number;
  orderName?: string;
  customerName?: string;
  customerEmail?: string;
  subscriptionCreatedEmailSent?: boolean;
  endsAt?: string;
  startsAt?: string;
  subscriptionCreatedEmailSentStatus?: subscriptionCreatedEmailSentStatus;
  minCycles?: number;
  maxCycles?: number;
  customerFirstName?: string;
  customerLastName?: string;
  autoCharge?: boolean;
  importedId?: string;
  stopUpComingOrderEmail?: boolean;
  pausedFromActive?: boolean;
  subscriptionCreatedSmsSentStatus?: SubscriptionCreatedSmsSentStatus;
  phone?: string;
  activatedOn?: string;
  pausedOn?: string;
  cancelledOn?: string;
  contractDetailsJSON?: any;
  cancellationFeedback?: any;
  orderNote?: any;
  orderNoteAttributes?: any;
  allowDeliveryPriceOverride?: boolean;
  disableFixEmptyQueue?: boolean;
  orderAmountUSD?: number;
  originType?: SubscriptionOriginType;
  originalContractId?: number;
  cancellationNote?: string;
  subscriptionType?: SubscriptionType;
  subscriptionTypeIdentifier?: string;
  upcomingEmailBufferDays?: number;
  upcomingEmailTaskUrl?: string;
  contractAmount?: number;
  contractAmountUSD?: number;
}

export const defaultValue: Readonly<ISubscriptionContractDetails> = {
  subscriptionCreatedEmailSent: false,
  autoCharge: false,
  stopUpComingOrderEmail: false,
  pausedFromActive: false,
  allowDeliveryPriceOverride: false,
  disableFixEmptyQueue: false
};

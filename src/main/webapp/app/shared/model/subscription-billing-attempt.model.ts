import { Moment } from 'moment';
import { BillingAttemptStatus } from 'app/shared/model/enumerations/billing-attempt-status.model';
import { transactionFailedEmailSentStatus } from 'app/shared/model/enumerations/transaction-failed-email-sent-status.model';
import { upcomingOrderEmailSentStatus } from 'app/shared/model/enumerations/upcoming-order-email-sent-status.model';
import { UsageChargeStatus } from 'app/shared/model/enumerations/usage-charge-status.model';
import { TransactionFailedSmsSentStatus } from 'app/shared/model/enumerations/transaction-failed-sms-sent-status.model';
import { UpcomingOrderSmsSentStatus } from 'app/shared/model/enumerations/upcoming-order-sms-sent-status.model';
import { SecurityChallengeSentStatus } from 'app/shared/model/enumerations/security-challenge-sent-status.model';

export interface ISubscriptionBillingAttempt {
  id?: number;
  shop?: string;
  billingAttemptId?: string;
  status?: BillingAttemptStatus;
  billingDate?: string;
  contractId?: number;
  attemptCount?: number;
  attemptTime?: string;
  graphOrderId?: string;
  orderId?: number;
  orderAmount?: number;
  orderName?: string;
  retryingNeeded?: boolean;
  transactionFailedEmailSentStatus?: transactionFailedEmailSentStatus;
  upcomingOrderEmailSentStatus?: upcomingOrderEmailSentStatus;
  applyUsageCharge?: boolean;
  recurringChargeId?: number;
  transactionRate?: number;
  usageChargeStatus?: UsageChargeStatus;
  transactionFailedSmsSentStatus?: TransactionFailedSmsSentStatus;
  upcomingOrderSmsSentStatus?: UpcomingOrderSmsSentStatus;
  billingAttemptResponseMessage?: any;
  progressAttemptCount?: number;
  orderNote?: any;
  securityChallengeSentStatus?: SecurityChallengeSentStatus;
  orderAmountUSD?: number;
}

export const defaultValue: Readonly<ISubscriptionBillingAttempt> = {
  retryingNeeded: false,
  applyUsageCharge: false
};

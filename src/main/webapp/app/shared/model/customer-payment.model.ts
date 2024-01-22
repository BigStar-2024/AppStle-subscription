import { Moment } from 'moment';
import { cardExpireEmailSentStatus } from 'app/shared/model/enumerations/card-expire-email-sent-status.model';
import { CardExpireSmsSentStatus } from 'app/shared/model/enumerations/card-expire-sms-sent-status.model';

export interface ICustomerPayment {
  id?: number;
  shop?: string;
  adminGraphqlApiId?: string;
  token?: string;
  customerId?: number;
  adminGraphqlApiCustomerId?: string;
  instrumentType?: string;
  paymentInstrumentLastDigits?: number;
  paymentInstrumentMonth?: number;
  paymentInstrumentYear?: number;
  paymentInstrumentName?: string;
  paymentInstrumentsBrand?: string;
  customerUid?: string;
  cardExpiryNotificationCounter?: number;
  cardExpiryNotificationFirstSent?: string;
  cardExpiryNotificationLastSent?: string;
  cardExpireEmailSentStatus?: cardExpireEmailSentStatus;
  cardExpireSmsSentStatus?: CardExpireSmsSentStatus;
}

export const defaultValue: Readonly<ICustomerPayment> = {};

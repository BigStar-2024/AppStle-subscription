import { Moment } from 'moment';
import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { BasePlan } from 'app/shared/model/enumerations/base-plan.model';
import { PlanType } from 'app/shared/model/enumerations/plan-type.model';
import { BillingType } from 'app/shared/model/enumerations/billing-type.model';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';
import { PaymentPlanEvent } from 'app/shared/model/enumerations/payment-plan-event.model';

export interface IPaymentPlanActivity {
  id?: number;
  shop?: string;
  chargeActivated?: boolean;
  activationDate?: Moment;
  recurringChargeId?: number;
  lastEmailSentToMerchant?: Moment;
  numberOfEmailSentToMerchant?: number;
  basePlan?: BasePlan;
  additionalDetails?: any;
  trialDays?: number;
  price?: number;
  name?: string;
  planType?: PlanType;
  billingType?: BillingType;
  basedOn?: BasedOn;
  features?: any;
  testCharge?: boolean;
  billedDate?: Moment;
  validCharge?: boolean;
  trialEndsOn?: Moment;
  shopFrozen?: boolean;
  paymentPlanEvent?: PaymentPlanEvent;
  planInfo?: IPlanInfo;
}

export const defaultValue: Readonly<IPaymentPlanActivity> = {
  chargeActivated: false,
  testCharge: false,
  validCharge: false,
  shopFrozen: false
};

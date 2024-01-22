import { Moment } from 'moment';
import { IPlanInfo } from 'app/shared/model/plan-info.model';
import { BasePlan } from 'app/shared/model/enumerations/base-plan.model';
import { PlanType } from 'app/shared/model/enumerations/plan-type.model';
import { BillingType } from 'app/shared/model/enumerations/billing-type.model';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';

export interface IPaymentPlan {
  id?: number;
  shop?: string;
  chargeActivated?: boolean;
  activationDate?: string;
  recurringChargeId?: number;
  lastEmailSentToMerchant?: string;
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
  isValidData?: boolean;
  billedDate?: string;
  validCharge?: boolean;
  trialEndsOn?: Moment;
  shopFrozen?: boolean;
  planInfo?: IPlanInfo;
}

export const defaultValue: Readonly<IPaymentPlan> = {
  chargeActivated: false,
  testCharge: false,
  validCharge: false,
  isValidData: false,
  shopFrozen: false
};

export const defaultPaymentPlanInformation = {
  activeSubscriptionCount: null,
  planLimit: null,
  planInfo: null
};

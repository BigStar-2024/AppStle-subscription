import { IPaymentPlan } from 'app/shared/model/payment-plan.model';
import { PlanType } from 'app/shared/model/enumerations/plan-type.model';
import { BillingType } from 'app/shared/model/enumerations/billing-type.model';
import { BasedOn } from 'app/shared/model/enumerations/based-on.model';
import { BasePlan } from 'app/shared/model/enumerations/base-plan.model';

export interface IPlanInfo {
  id?: number;
  name?: string;
  price?: number;
  archived?: boolean;
  planType?: PlanType;
  billingType?: BillingType;
  basedOn?: BasedOn;
  additionalDetails?: any;
  features?: any;
  trialDays?: number;
  basePlan?: BasePlan;
  paymentPlans?: IPaymentPlan[];
}

export const defaultValue: Readonly<IPlanInfo> = {
  archived: false
};

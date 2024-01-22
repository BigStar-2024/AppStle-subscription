import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';
import { DiscountTypeUnit } from 'app/shared/model/enumerations/discount-type-unit.model';
import { OrderBillingType } from './enumerations/order-billing-type.model';

export interface ISubscriptionGroup {
  id: string;
  groupName: string;
  productIds: string;
  variantIds: string;
  subscriptionPlans: SubscriptionPlan[];

  productId?: number;
  accessoryProductIds?: string;
  productCount?: number;
  productVariantCount?: number;
}

export interface SubscriptionPlan {
  id: string | boolean;
  isInitial: boolean;
  planType: OrderBillingType;
  frequencyName: string;
  frequencyCount: number;
  frequencyInterval: FrequencyIntervalUnit;
  frequencyType: 'ON_PURCHASE_DAY' | 'ON_SPECIFIC_DAY';
  billingFrequencyCount?: number;
  billingFrequencyInterval?: FrequencyIntervalUnit;
  discountOffer: string;
  discountType: DiscountTypeUnit;
  discountEnabled: boolean;
  specificDayEnabled: boolean;
  specificDayValue?: number;
  specificMonthValue?: number;
  appstleCycles: appstleCycles[];
  freeTrialEnabled: boolean;
  discountEnabled2Masked: boolean;
  memberOnly: boolean;
  nonMemberOnly: boolean;
  formFieldJson: string;
  cutOff?: number;
  minCycles?: number;
  maxCycles?: number;
  memberInclusiveTags?: string;
  memberExclusiveTags?: string;
}

export interface SubscriptionPlanCustomField {
  type: 'date' | 'text' | 'select';
  label: string;
  required: boolean;
  visible: boolean;
  config?: string;
  enabledDays?: {
    value: string;
    label: string;
  }[];
  nextOrderMinimumThreshold?: number;
  name?: string;
  selectOptions?: string;
}

export interface appstleCycles {
  afterCycle: any;
  discountType: string;
  value: any;
}

export const defaultValue: Readonly<ISubscriptionGroup> = {
  id: '',
  productCount: 0,
  productVariantCount: 0,
  subscriptionPlans: [
    {
      planType: OrderBillingType.PAY_AS_YOU_GO,
      isInitial: true,
      frequencyCount: null,
      frequencyInterval: FrequencyIntervalUnit.MONTH,
      frequencyName: '',
      frequencyType: 'ON_PURCHASE_DAY',
      billingFrequencyInterval: FrequencyIntervalUnit.MONTH,
      discountOffer: '',
      discountType: DiscountTypeUnit.PERCENTAGE,
      discountEnabled: false,
      specificDayEnabled: false,
      id: '',
      appstleCycles: [],
      freeTrialEnabled: false,
      discountEnabled2Masked: false,
      memberOnly: false,
      nonMemberOnly: false,
      formFieldJson: '[]'
    }
  ],
  groupName: '',
  productIds: '[]',
  variantIds: '[]'
};

export const defaultPlanCustomField: Readonly<SubscriptionPlanCustomField> = {
  type: 'date',
  label: '',
  required: false,
  visible: true
};

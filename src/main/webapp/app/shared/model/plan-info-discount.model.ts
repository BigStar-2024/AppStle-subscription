import { Moment } from 'moment';
import { PlanInfoDiscountType } from 'app/shared/model/enumerations/plan-info-discount-type.model';

export interface IPlanInfoDiscount {
  id?: number;
  discountCode?: string;
  description?: any;
  discountType?: PlanInfoDiscountType;
  discount?: number;
  maxDiscountAmount?: number;
  trialDays?: number;
  startDate?: Moment;
  endDate?: Moment;
  archived?: boolean;
}

export const defaultValue: Readonly<IPlanInfoDiscount> = {
  archived: false
};

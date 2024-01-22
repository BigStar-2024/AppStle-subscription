import { Moment } from 'moment';
import { BundleStatus } from 'app/shared/model/enumerations/bundle-status.model';
import { BundleDiscountType } from 'app/shared/model/enumerations/bundle-discount-type.model';
import { BundleLevel } from 'app/shared/model/enumerations/bundle-level.model';
import { BundleDiscountCondition } from 'app/shared/model/enumerations/bundle-discount-condition.model';
import { BundleType } from 'app/shared/model/enumerations/bundle-type.model';

export interface IBundleRule {
  id?: number;
  shop?: string;
  name?: string;
  title?: string;
  description?: string;
  priceSummary?: string;
  actionButtonText?: string;
  actionButtonDescription?: string;
  status?: BundleStatus;
  showBundleWidget?: boolean;
  customerIncludeTags?: string;
  startDate?: string;
  endDate?: string;
  discountType?: BundleDiscountType;
  discountValue?: number;
  bundleLevel?: BundleLevel;
  products?: any;
  variants?: any;
  discountCondition?: BundleDiscountCondition;
  sequenceNo?: number;
  bundleType?: BundleType;
  showCombinedSellingPlan?: boolean;
  selectSubscriptionByDefault?: boolean;
  minimumNumberOfItems?: number;
  maximumNumberOfItems?: number;
  maxQuantity?: number;
}

export const defaultValue: Readonly<IBundleRule> = {
  showBundleWidget: false,
  discountType: BundleDiscountType.PERCENTAGE,
  status: BundleStatus.ACTIVE,
  bundleLevel: BundleLevel.PRODUCT,
  actionButtonText: 'Checkout',
  bundleType: BundleType.CLASSIC,
  showCombinedSellingPlan: false,
  selectSubscriptionByDefault: false,
  minimumNumberOfItems: 1
};

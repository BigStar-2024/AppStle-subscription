export interface IProductSwap {
  id?: number;
  shop?: string;
  sourceVariants?: any;
  destinationVariants?: any;
  updatedFirstOrder?: boolean;
  checkForEveryRecurringOrder?: boolean;
  name?: any;
  changeNextOrderDateBy?: number;
  forBillingCycle?: number;
  carryDiscountForward?: boolean;
}

export const defaultValue: Readonly<IProductSwap> = {
  updatedFirstOrder: false,
  checkForEveryRecurringOrder: false,
  carryDiscountForward: false
};

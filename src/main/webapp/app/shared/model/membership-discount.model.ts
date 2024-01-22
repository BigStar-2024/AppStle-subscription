export interface IMembershipDiscount {
  id?: number;
  shop?: string;
  title?: string;
  discount?: number;
  customerTags?: string;
}

export const defaultValue: Readonly<IMembershipDiscount> = {};

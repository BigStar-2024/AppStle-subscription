export interface IMemberOnly {
  id?: number;
  shop?: string;
  sellingPlanId?: string;
  tags?: string;
}

export const defaultValue: Readonly<IMemberOnly> = {};

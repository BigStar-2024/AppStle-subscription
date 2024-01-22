export interface ISellingPlanMemberInfo {
  id?: number;
  shop?: string;
  subscriptionId?: number;
  sellingPlanId?: number;
  enableMemberInclusiveTag?: boolean;
  memberInclusiveTags?: string;
  enableMemberExclusiveTag?: boolean;
  memberExclusiveTags?: string;
}

export const defaultValue: Readonly<ISellingPlanMemberInfo> = {
  enableMemberInclusiveTag: false,
  enableMemberExclusiveTag: false
};

import { UsergroupRuleAction } from 'app/shared/model/enumerations/usergroup-rule-action.model';

export interface IUsergroupRule {
  id?: number;
  shop?: string;
  forCustomersWithTags?: string;
  forProductsWithTags?: string;
  action?: UsergroupRuleAction;
}

export const defaultValue: Readonly<IUsergroupRule> = {};

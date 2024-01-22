import { Moment } from 'moment';
import { OrderWebhookType } from 'app/shared/model/enumerations/order-webhook-type.model';
import { RemoveTagsExpiresUnitType } from 'app/shared/model/enumerations/remove-tags-expires-unit-type.model';
import { TriggerRuleStatus } from 'app/shared/model/enumerations/trigger-rule-status.model';

export interface ITriggerRule {
  id?: number;
  shop?: string;
  name?: string;
  appendToNote?: boolean;
  webhook?: OrderWebhookType;
  deactivateAfterDate?: string;
  deactivateAfterTime?: string;
  fixedTags?: any;
  dynamicTags?: any;
  removeTags?: any;
  notMatchTags?: any;
  removeTagsExpiresIn?: number;
  removeTagsExpiresInUnit?: RemoveTagsExpiresUnitType;
  handlerData?: any;
  status?: TriggerRuleStatus;
  deactivatedAt?: string;
}

export const defaultValue: Readonly<ITriggerRule> = {
  appendToNote: false,
  webhook: OrderWebhookType.ORDER_CREATE,
  name: '',
  handlerData: {
    criteria: [],
    criteriaMatching: 'and',
    validationError: 0
  },
  fixedTags: [],
  dynamicTags: [],
  removeTags: [],
  notMatchTags: [],
  removeTagsExpiresIn: 0,
  removeTagsExpiresInUnit: RemoveTagsExpiresUnitType.MINUTES,
  status: TriggerRuleStatus.ACTIVE
};

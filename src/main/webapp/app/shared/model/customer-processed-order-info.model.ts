import { Moment } from 'moment';
import { ProcessedOrderInfoStatus } from 'app/shared/model/enumerations/processed-order-info-status.model';
import { ProcessedOrderInfoDelayedTaggingUnit } from 'app/shared/model/enumerations/processed-order-info-delayed-tagging-unit.model';

export interface ICustomerProcessedOrderInfo {
  id?: number;
  shop?: string;
  orderId?: number;
  orderNumber?: number;
  customerNumber?: number;
  topicName?: string;
  processedTime?: string;
  triggerRuleInfoJson?: any;
  attachedTags?: any;
  attachedTagsToNote?: any;
  removedTags?: any;
  removedTagsAfter?: any;
  removedTagsBefore?: any;
  firstName?: string;
  lastName?: string;
  status?: ProcessedOrderInfoStatus;
  delayedTaggingValue?: number;
  delayedTaggingUnit?: ProcessedOrderInfoDelayedTaggingUnit;
}

export const defaultValue: Readonly<ICustomerProcessedOrderInfo> = {};

import { Moment } from 'moment';

export interface IAsyncUpdateEventProcessing {
  id?: number;
  subscriptionContractId?: number;
  lastUpdated?: string;
  tagModelJson?: any;
  firstTimeOrderTags?: string;
}

export const defaultValue: Readonly<IAsyncUpdateEventProcessing> = {};

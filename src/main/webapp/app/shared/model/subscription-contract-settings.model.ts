import { FrequencyIntervalUnit } from 'app/shared/model/enumerations/frequency-interval-unit.model';

export interface ISubscriptionContractSettings {
  id?: number;
  shop?: string;
  productId?: string;
  endsOnCount?: number;
  endsOnInterval?: FrequencyIntervalUnit;
}

export const defaultValue: Readonly<ISubscriptionContractSettings> = {};

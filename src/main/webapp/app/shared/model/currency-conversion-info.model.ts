import { Moment } from 'moment';

export interface ICurrencyConversionInfo {
  id?: number;
  from?: string;
  to?: string;
  storedOn?: Moment;
  currencyRate?: number;
}

export const defaultValue: Readonly<ICurrencyConversionInfo> = {};

import { Moment } from 'moment';

export interface IActivityUpdatesSettings {
  id?: number;
  shop?: string;
  summaryReportEnabled?: boolean;
  summaryReportFrequency?: string;
  summaryReportDeliverToEmail?: string;
  summaryReportTimePeriod?: string;
  summaryReportLastSent?: string;
  summaryReportProcessing?: boolean;
}

export const defaultValue: Readonly<IActivityUpdatesSettings> = {
  summaryReportEnabled: false,
  summaryReportProcessing: false
};

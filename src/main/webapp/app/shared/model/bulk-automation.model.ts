import { Moment } from 'moment';
import { BulkAutomationType } from 'app/shared/model/enumerations/bulk-automation-type.model';

export interface IBulkAutomation {
  id?: number;
  shop?: string;
  automationType?: BulkAutomationType;
  running?: boolean;
  startTime?: Moment;
  endTime?: Moment;
  requestInfo?: any;
  errorInfo?: any;
  currentExecution?: string;
}

export const defaultValue: Readonly<IBulkAutomation> = {
  running: false
};

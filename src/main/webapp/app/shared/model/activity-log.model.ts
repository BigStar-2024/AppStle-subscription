import { Moment } from 'moment';
import { ActivityLogEntityType } from 'app/shared/model/enumerations/activity-log-entity-type.model';
import { ActivityLogEventSource } from 'app/shared/model/enumerations/activity-log-event-source.model';
import { ActivityLogEventType } from 'app/shared/model/enumerations/activity-log-event-type.model';
import { ActivityLogStatus } from 'app/shared/model/enumerations/activity-log-status.model';

export interface IActivityLog {
  id?: number;
  shop?: string;
  entityId?: number;
  entityType?: ActivityLogEntityType;
  eventSource?: ActivityLogEventSource;
  eventType?: ActivityLogEventType;
  status?: ActivityLogStatus;
  createAt?: Moment;
  additionalInfo?: any;
}

export const defaultValue: Readonly<IActivityLog> = {};

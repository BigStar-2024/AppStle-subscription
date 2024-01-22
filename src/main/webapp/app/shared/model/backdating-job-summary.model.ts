import moment from 'moment';
import { JobOrdersType } from 'app/shared/model/enumerations/job-orders-type.model';
import { JobRulesType } from 'app/shared/model/enumerations/job-rules-type.model';
import { BackdatingJobSummaryStatus } from 'app/shared/model/enumerations/backdating-job-summary-status.model';

export interface IBackdatingJobSummary {
  id?: number;
  shop?: string;
  jobOrdersType?: JobOrdersType;
  jobOrdersBeginDate?: string;
  jobOrdersEndDate?: string;
  jobRulesType?: JobRulesType;
  triggerRuleIds?: string;
  applicationChargeId?: number;
  charge?: number;
  ordersCount?: number;
  status?: BackdatingJobSummaryStatus;
  paymentAccepted?: boolean;
  orderMigrationIdentifier?: string;
  totalOrdersCompleted?: number;
  create?: string;
}

export const defaultValue: Readonly<IBackdatingJobSummary> = {
  shop: '',
  jobOrdersType: JobOrdersType.ALL_ORDERS,
  jobOrdersBeginDate: moment(new Date(new Date().setDate(new Date().getDate() - 7))).format('YYYY-MM-DD'),
  jobOrdersEndDate: moment().format('YYYY-MM-DD'),
  jobRulesType: JobRulesType.ALL_RULES,
  triggerRuleIds: '',
  applicationChargeId: 0,
  charge: 0,
  ordersCount: 0,
  status: BackdatingJobSummaryStatus.YET_TO_START,
  paymentAccepted: false,
  orderMigrationIdentifier: '',
  totalOrdersCompleted: 0,
  create: moment(new Date()).format('YYYY-MM-DDTHH:MM:SS')
};

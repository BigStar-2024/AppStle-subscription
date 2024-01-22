import { RetryAttempts } from 'app/shared/model/enumerations/retry-attempts.model';
import { DaysBeforeRetrying } from 'app/shared/model/enumerations/days-before-retrying.model';
import { MaxNumberOfFailures } from 'app/shared/model/enumerations/max-number-of-failures.model';

export interface IDunningManagement {
  id?: number;
  shop?: string;
  retryAttempts?: RetryAttempts;
  daysBeforeRetrying?: DaysBeforeRetrying;
  maxNumberOfFailures?: MaxNumberOfFailures;
}

export const defaultValue: Readonly<IDunningManagement> = {};

import { CancellationTypeStatus } from 'app/shared/model/enumerations/cancellation-type-status.model';
import CancellationReason from './cancellation-reason.model';

export interface ICancellationManagement {
  id?: number;
  shop?: string;
  cancellationType?: CancellationTypeStatus;
  cancellationInstructionsText?: string;
  cancellationReasonsJSON?: string;
  pauseInstructionsText?: string;
  pauseDurationCycle?: number;
  enableDiscountEmail?: boolean;
  discountEmailAddress?: string;
}

export const defaultValue: Readonly<ICancellationManagement> = {};

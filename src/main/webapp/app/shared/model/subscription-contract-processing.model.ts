export interface ISubscriptionContractProcessing {
  id?: number;
  contractId?: number;
  attemptCount?: number;
}

export const defaultValue: Readonly<ISubscriptionContractProcessing> = {};

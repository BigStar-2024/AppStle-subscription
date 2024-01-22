export enum CancellationReasonAction {
  NONE = 0,
  DISCOUNT = 1,
  SWAP = 2,
  GIFT = 3,
  SKIP = 4,
  CHANGE_DATE = 5,
  CHANGE_ADDRESS = 6,
  UPDATE_FREQUENCY = 7,
  PAUSE = 8
}

export default interface CancellationReason {
  cancellationReason: string;
  cancellationAction: CancellationReasonAction;
  cancellationDiscount?: number;
  cancellationPrompt: boolean;
}

export const defaultValue = {
  cancellationReason: '',
  cancellationAction: CancellationReasonAction.NONE,
  cancellationPrompt: true
};
